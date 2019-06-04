package com.walksocket.md.filter;

import com.walksocket.md.MdDesk;
import com.walksocket.md.MdInfoSync;
import com.walksocket.md.MdUtils;
import com.walksocket.md.mariadb.MdMariadbConnection;
import com.walksocket.md.mariadb.MdMariadbRecord;
import com.walksocket.md.mariadb.MdMariadbUtils;
import com.walksocket.md.output.MdOutputSync;
import com.walksocket.md.output.member.MdOutputMemberNotReflectedRecordTable;
import com.walksocket.md.output.member.MdOutputMemberReflectedRecordTable;
import com.walksocket.md.output.parts.MdOutputPartsColumn;
import com.walksocket.md.output.parts.MdOutputPartsNotReflectedRecord;
import com.walksocket.md.output.parts.MdOutputPartsRecord;
import com.walksocket.md.output.parts.MdOutputPartsReflectedRecord;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * filter sync reflect.
 */
public class MdFilterSyncReflect extends MdFilterSyncAbstract {

  /**
   * force flag.
   * <pre>
   *   if true, force to delete from compare
   * </pre>
   */
  private boolean force;

  /**
   * desk.
   */
  private MdDesk desk;

  /**
   * constructor.
   * @param con mariadb connection
   * @param force force flag
   */
  public MdFilterSyncReflect(MdMariadbConnection con, boolean force) {
    super(con);

    this.force = force;
    this.desk = new MdDesk(con);
  }

  @Override
  public void filter(List<MdInfoSync> infoList, MdOutputSync outputSync) throws SQLException {
    for (MdInfoSync info : infoList) {
      MdOutputMemberReflectedRecordTable reflectedRecordTable = new MdOutputMemberReflectedRecordTable(info);
      MdOutputMemberNotReflectedRecordTable notReflectedRecordTable = new MdOutputMemberNotReflectedRecordTable(info);

      Map<Long, Boolean> diffSeqReflectedMap = reflectFromBaseToCompare(info);

      List<Long> reflectedDiffSeqs = diffSeqReflectedMap.entrySet().stream().filter(e -> e.getValue()).map(e -> e.getKey()).collect(Collectors.toList());
      if (!MdUtils.isNullOrEmpty(reflectedDiffSeqs)) {
        List<MdOutputPartsRecord> reflectedDiffRecords = desk.getPartRecordsForSync(
            reflectedDiffSeqs,
            info.getColumns());
        for (MdOutputPartsRecord partsRecord : reflectedDiffRecords) {
          reflectedRecordTable.records.add(new MdOutputPartsReflectedRecord(partsRecord));
        }

        outputSync.reflectedRecordTables.add(reflectedRecordTable);
      }

      List<Long> notReflectedDiffSeqs = diffSeqReflectedMap.entrySet().stream().filter(e -> !e.getValue()).map(e -> e.getKey()).collect(Collectors.toList());
      if (!MdUtils.isNullOrEmpty(notReflectedDiffSeqs)) {
        List<MdOutputPartsRecord> notFeflectedDiffRecords = desk.getPartRecordsForSync(
            notReflectedDiffSeqs,
            info.getColumns());
        for (MdOutputPartsRecord partsRecord : notFeflectedDiffRecords) {
          notReflectedRecordTable.records.add(new MdOutputPartsNotReflectedRecord(partsRecord));
        }

        outputSync.notReflectedRecordTables.add(notReflectedRecordTable);
      }
    }
  }

  /**
   * reflect from base to compare
   * @param info info
   * @return map diffSeq and reflect result
   * @throws SQLException sql error
   */
  private Map<Long, Boolean> reflectFromBaseToCompare(MdInfoSync info) throws SQLException {
    Map<Long, Boolean> diffSeqReflectedMap = new LinkedHashMap<>();

    List<MdMariadbRecord> records;
    String sql = "";

    // -----
    // get column
    List<String> primaryBaseColumns = new ArrayList<>();
    List<String> primaryCompareColumns = new ArrayList<>();
    List<String> columnNames = new ArrayList<>();
    List<String> baseColumns = new ArrayList<>();
    List<String> compareColumns = new ArrayList<>();
    for (MdOutputPartsColumn column : info.getColumns()) {
      if (column.isPrimary) {
        primaryBaseColumns.add(
            String.format(
                "COLUMN_GET(`baseValues`, '%s' as %s) as %s",
                column.columnName,
                MdMariadbUtils.getDynamicType(column.columnType).toString(),
                column.columnName));
        primaryCompareColumns.add(
            String.format(
                "COLUMN_GET(`compareValues`, '%s' as %s) as %s",
                column.columnName,
                MdMariadbUtils.getDynamicType(column.columnType).toString(),
                column.columnName));
      }

      columnNames.add(String.format("`%s`", column.columnName));
      baseColumns.add(
          String.format(
              "COLUMN_GET(`baseValues`, '%s' as %s) as %s",
              column.columnName,
              MdMariadbUtils.getDynamicType(column.columnType).toString(),
              column.columnName));
      compareColumns.add(
          String.format(
              "COLUMN_GET(`compareValues`, '%s' as %s) as %s",
              column.columnName,
              MdMariadbUtils.getDynamicType(column.columnType).toString(),
              column.columnName));
    }

    for (Long diffSeq : info.getDiffSeqs()) {
      // init
      diffSeqReflectedMap.put(diffSeq, false);

      // -----
      // check base
      Map<String, String> primaryBaseValues = desk.getPrimaryValues(primaryBaseColumns, diffSeq);

      // force
      if (primaryBaseValues.size() > 0 || force) {
        Map<String, String> primaryCompareValues = desk.getPrimaryValues(primaryCompareColumns, diffSeq);

        // -----
        // delete from compare
        if (primaryCompareValues.size() > 0) {
          List<String> conditions = new ArrayList<>();
          for (Map.Entry<String, String> entry : primaryCompareValues.entrySet()) {
            MdOutputPartsColumn column = info.getColumns().stream()
                .filter(c -> c.isPrimary && c.columnName.equalsIgnoreCase(entry.getKey()))
                .findFirst().get();
            conditions.add(
                String.format(
                    "`%s` = %s",
                    entry.getKey(),
                    convertColumnValue(column.columnType, entry.getValue())));
          }
          sql = String.format(
              "DELETE FROM `%s`.`%s` WHERE %s",
              info.getCompareDatabase(),
              info.getTableName(),
              MdUtils.join(conditions, " AND "));
          con.execute(sql);

          diffSeqReflectedMap.put(diffSeq, true);
        }
      }

      if (primaryBaseValues.size() > 0) {
        // -----
        // insert compare from base
        sql = String.format(
            "INSERT INTO `%s`.`%s` (%s) SELECT %s FROM `magentadesk`.`diffRecord` WHERE `diffSeq` = %s",
            info.getCompareDatabase(),
            info.getTableName(),
            MdUtils.join(columnNames, ", "),
            MdUtils.join(baseColumns, ", "),
            diffSeq);
        con.execute(sql);

        diffSeqReflectedMap.put(diffSeq, true);
      }
    }

    return diffSeqReflectedMap;
  }

  /**
   * convert column value.
   * @param columnType column type
   * @param columnValue column value
   * @return formed column value
   * @throws SQLException sql error
   */
  private String convertColumnValue(String columnType, String columnValue) throws SQLException {
    if (columnValue == null) {
      return "NULL";
    }

    if (MdMariadbUtils.getColmunType(columnType) == MdMariadbUtils.MdMariadbColumnType.STRING) {
      columnValue = String.format("'%s'", MdMariadbUtils.quote(columnValue));
    } else if (MdMariadbUtils.getColmunType(columnType) == MdMariadbUtils.MdMariadbColumnType.BINARY) {
      columnValue = "0x" + columnValue;
    }
    return columnValue;
  }
}
