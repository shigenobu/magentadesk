package com.walksocket.md;

import com.walksocket.md.db.MdDbConnection;
import com.walksocket.md.db.MdDbFactory.DbType;
import com.walksocket.md.db.MdDbRecord;
import com.walksocket.md.info.MdInfoDiffColumn;
import com.walksocket.md.mariadb.MdMariadbUtils;
import com.walksocket.md.mysql.MdMysqlUtils;
import com.walksocket.md.output.parts.MdOutputPartsColumn;
import com.walksocket.md.output.parts.MdOutputPartsRecord;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * desk.
 */
public class MdDesk {

  /**
   * db connection.
   */
  private final MdDbConnection con;

  /**
   * constructor.
   *
   * @param con db connection
   */
  public MdDesk(MdDbConnection con) {
    this.con = con;
  }

  /**
   * get primary values.
   *
   * @param primaryColumns primary columns
   * @param diffSeq        diff seq
   * @return primary map values
   * @throws SQLException sql error
   */
  public Map<String, String> getPrimaryValues(List<String> primaryColumns, long diffSeq)
      throws SQLException {
    List<MdDbRecord> records;
    String sql;

    Map<String, String> primaryValues = new LinkedHashMap<>();
    sql = String.format(
        "SELECT %s FROM `magentadesk`.`diffRecord` WHERE `diffSeq` = %s",
        MdUtils.join(primaryColumns, ", "),
        diffSeq);
    records = con.getRecords(sql);
    for (MdDbRecord record : records) {
      for (Map.Entry<String, String> entry : record.getAll().entrySet()) {
        if (entry.getValue() != null) {
          primaryValues.put(entry.getKey(), entry.getValue());
        }
      }
    }

    return primaryValues;
  }

  /**
   * get part records for diff.
   *
   * @param summaryId   summary id
   * @param baseInfo    base info
   * @param compareInfo compare info
   * @return part records
   * @throws SQLException sql error
   */
  public List<MdOutputPartsRecord> getPartRecordsForDiff(String summaryId, MdInfoDiff baseInfo,
      MdInfoDiff compareInfo) throws SQLException {
    // get base columns
    List<String> baseColumns = new ArrayList<>();
    for (MdInfoDiffColumn column : baseInfo.getRealColumns()) {
      if (con.getDbType() == DbType.MYSQL) {
        if (MdMysqlUtils.isBinary(column.getColumnType())) {
          // select from_base64(substring('base64:type15:YWJj', locate(':', 'base64:type15:YWJj', 8) + 1));
          baseColumns.add(
              String.format(
                  // returning char
                  "FROM_BASE64(SUBSTRING(JSON_VALUE(`baseValues`, '$.%s' returning %s), LOCATE(':', JSON_VALUE(`baseValues`, '$.%s' returning %s), 8) + 1)) as mdb_%s",
                  column.getColumnName(),
                  MdMysqlUtils.getReturningType(column.getColumnType()).toString(),
                  column.getColumnName(),
                  MdMysqlUtils.getReturningType(column.getColumnType()).toString(),
                  column.getColumnName()));
        } else {
          baseColumns.add(
              String.format(
                  "JSON_VALUE(`baseValues`, '$.%s' returning %s) as mdb_%s",
                  column.getColumnName(),
                  MdMysqlUtils.getReturningType(column.getColumnType()).toString(),
                  column.getColumnName()));
        }
      } else {
        baseColumns.add(
            String.format(
                "COLUMN_GET(`baseValues`, '%s' as %s) as mdb_%s",
                column.getColumnName(),
                MdMariadbUtils.getDynamicType(column.getColumnType()).toString(),
                column.getColumnName()));
      }
    }

    // get compare columns
    List<String> compareColumns = new ArrayList<>();
    for (MdInfoDiffColumn column : compareInfo.getRealColumns()) {
      if (con.getDbType() == DbType.MYSQL) {
        if (MdMysqlUtils.isBinary(column.getColumnType())) {
          // select from_base64(substring('base64:type15:YWJj', locate(':', 'base64:type15:YWJj', 8) + 1));
          compareColumns.add(
              String.format(
                  // returning char
                  "FROM_BASE64(SUBSTRING(JSON_VALUE(`compareValues`, '$.%s' returning %s), LOCATE(':', JSON_VALUE(`compareValues`, '$.%s' returning %s), 8) + 1)) as mdc_%s",
                  column.getColumnName(),
                  MdMysqlUtils.getReturningType(column.getColumnType()).toString(),
                  column.getColumnName(),
                  MdMysqlUtils.getReturningType(column.getColumnType()).toString(),
                  column.getColumnName()));
        } else {
          compareColumns.add(
              String.format(
                  "JSON_VALUE(`compareValues`, '$.%s' returning %s) as mdc_%s",
                  column.getColumnName(),
                  MdMysqlUtils.getReturningType(column.getColumnType()).toString(),
                  column.getColumnName()));
        }
      } else {
        compareColumns.add(
            String.format(
                "COLUMN_GET(`compareValues`, '%s' as %s) as mdc_%s",
                column.getColumnName(),
                MdMariadbUtils.getDynamicType(column.getColumnType()).toString(),
                column.getColumnName()));
      }
    }

    // get
    String sql = String.format(
        "SELECT `diffSeq`, %s, %s FROM `magentadesk`.`diffRecord` WHERE `summaryId` = '%s' and `tableName` = '%s'",
        MdUtils.join(baseColumns, ", "),
        MdUtils.join(compareColumns, ", "),
        MdDbUtils.quote(summaryId),
        MdDbUtils.quote(baseInfo.getTableName()));
    return getPartRecords(sql);
  }

  /**
   * get part records for sync.
   *
   * @param diffSeqs diff seqs.
   * @param columns  columns
   * @return part records
   * @throws SQLException sql error
   */
  public List<MdOutputPartsRecord> getPartRecordsForSync(List<Long> diffSeqs,
      List<MdOutputPartsColumn> columns) throws SQLException {
    // get columns
    List<String> baseColumns = new ArrayList<>();
    List<String> compareColumns = new ArrayList<>();
    for (MdOutputPartsColumn column : columns) {
      if (con.getDbType() == DbType.MYSQL) {
        if (MdMysqlUtils.isBinary(column.columnType)) {
          baseColumns.add(
              String.format(
                  "FROM_BASE64(SUBSTRING(JSON_VALUE(`baseValues`, '$.%s' returning %s), LOCATE(':', JSON_VALUE(`baseValues`, '$.%s' returning %s), 8) + 1)) as mdb_%s",
                  column.columnName,
                  MdMysqlUtils.getReturningType(column.columnType).toString(),
                  column.columnName,
                  MdMysqlUtils.getReturningType(column.columnType).toString(),
                  column.columnName));
          compareColumns.add(
              String.format(
                  "FROM_BASE64(SUBSTRING(JSON_VALUE(`compareValues`, '$.%s' returning %s), LOCATE(':', JSON_VALUE(`compareValues`, '$.%s' returning %s), 8) + 1)) as mdc_%s",
                  column.columnName,
                  MdMysqlUtils.getReturningType(column.columnType).toString(),
                  column.columnName,
                  MdMysqlUtils.getReturningType(column.columnType).toString(),
                  column.columnName));
        } else {
          baseColumns.add(
              String.format(
                  "JSON_VALUE(`baseValues`, '$.%s' returning %s) as mdb_%s",
                  column.columnName,
                  MdMysqlUtils.getReturningType(column.columnType).toString(),
                  column.columnName));
          compareColumns.add(
              String.format(
                  "JSON_VALUE(`compareValues`, '$.%s' returning %s) as mdc_%s",
                  column.columnName,
                  MdMysqlUtils.getReturningType(column.columnType).toString(),
                  column.columnName));
        }
      } else {
        baseColumns.add(
            String.format(
                "COLUMN_GET(`baseValues`, '%s' as %s) as mdb_%s",
                column.columnName,
                MdMariadbUtils.getDynamicType(column.columnType).toString(),
                column.columnName));
        compareColumns.add(
            String.format(
                "COLUMN_GET(`compareValues`, '%s' as %s) as mdc_%s",
                column.columnName,
                MdMariadbUtils.getDynamicType(column.columnType).toString(),
                column.columnName));
      }
    }

    String sql = String.format(
        "SELECT `diffSeq`, %s, %s " +
            "FROM `magentadesk`.`diffRecord` " +
            "WHERE `diffSeq` in (%s)",
        MdUtils.join(baseColumns, ", "),
        MdUtils.join(compareColumns, ", "),
        MdUtils.join(diffSeqs, ", "));
    return getPartRecords(sql);
  }

  /**
   * get part records.
   *
   * @param sql sql
   * @return part records
   * @throws SQLException sql error
   */
  private List<MdOutputPartsRecord> getPartRecords(String sql) throws SQLException {
    List<MdOutputPartsRecord> partsRecords = new ArrayList<>();

    List<MdDbRecord> records = con.getRecords(sql);
    for (MdDbRecord record : records) {
      long diffSeq = Long.parseLong(record.get("diffSeq"));

      List<String> baseValues = new ArrayList<>();
      List<String> compareValues = new ArrayList<>();
      for (Map.Entry<String, String> entry : record.getAll().entrySet()) {
        String mdColumnName = entry.getKey();
        String md = mdColumnName.substring(0, 3);
        String columnValue = entry.getValue();

        if (!MdUtils.isNullOrEmpty(columnValue)
            && columnValue.getBytes(StandardCharsets.UTF_8).length > MdEnv.getLimitLength()) {
          // if length over MD_LIMIT_LENGTH, column value is to hash.
          columnValue = String.format("[HASH]%s", MdUtils.getHash(columnValue));
        }

        if (md.equals("mdb")) {
          baseValues.add(columnValue);
        } else if (md.equals("mdc")) {
          compareValues.add(columnValue);
        }
      }

      partsRecords.add(new MdOutputPartsRecord(diffSeq, baseValues, compareValues));
    }

    return partsRecords;
  }
}
