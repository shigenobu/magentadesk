package com.walksocket.md.filter;

import com.walksocket.md.MdDesk;
import com.walksocket.md.MdInfoDiff;
import com.walksocket.md.MdLogger;
import com.walksocket.md.MdUtils;
import com.walksocket.md.db.MdDbConnection;
import com.walksocket.md.db.MdDbFactory.DbType;
import com.walksocket.md.output.MdOutputDiff;
import com.walksocket.md.output.member.MdOutputMemberMatchTables;
import com.walksocket.md.info.MdInfoDiffColumn;
import com.walksocket.md.output.member.MdOutputMemberMismatchRecordTable;
import com.walksocket.md.output.parts.MdOutputPartsRecord;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * filter diff mismatch record.
 */
public class MdFilterDiffMismatchRecordTables extends MdFilterDiffAbstract {

  /**
   * desk.
   */
  private MdDesk desk;

  /**
   * constructor.
   * @param con db connection
   */
  public MdFilterDiffMismatchRecordTables(MdDbConnection con) {
    super(con);

    this.desk = new MdDesk(con);
  }

  @Override
  public void filter(List<MdInfoDiff> baseInfoList, List<MdInfoDiff> compareInfoList, MdOutputDiff outputDiff) throws SQLException {
    List<MdInfoDiff> removedBaseInfoList = new ArrayList<>();
    List<MdInfoDiff> removedCompareInfoLIst = new ArrayList<>();

    for (MdInfoDiff baseInfo : baseInfoList) {
      MdInfoDiff compareInfo = compareInfoList
          .stream()
          .filter(i -> i.getTableName().equals(baseInfo.getTableName()))
          .findFirst()
          .get();

      // checksum
      String baseChecksum = null;
      String compareChecksum = null;
      try {
        ExecutorService service = Executors.newFixedThreadPool(2);
        CompletableFuture<String> baseFuture = baseInfo.getChecksumFuture(service);
        CompletableFuture<String> compareFuture = compareInfo.getChecksumFuture(service);
        baseChecksum = baseFuture.get(600, TimeUnit.SECONDS);
        compareChecksum = compareFuture.get(600, TimeUnit.SECONDS);
      } catch (Exception e) {
        MdLogger.error(e);
        new SQLException(e);
      }
      boolean matchChecksum = false;
      if (!MdUtils.isNullOrEmpty(baseChecksum)
          && !MdUtils.isNullOrEmpty(compareChecksum)
          && baseChecksum.equals(compareChecksum)) {
        matchChecksum = true;
      }
      if (MdUtils.isNullOrEmpty(baseChecksum)
          && MdUtils.isNullOrEmpty(compareChecksum)) {
        matchChecksum = true;
      }
      if (matchChecksum) {
        // add matches
        outputDiff.matchTables.add(
            new MdOutputMemberMatchTables(baseInfo, compareInfo));

        removedBaseInfoList.add(baseInfo);
        removedCompareInfoLIst.add(compareInfo);
        continue;
      }

      // register
      registerDiffRecords(
          outputDiff.summaryId,
          baseInfo,
          compareInfo);

      // get
      List<MdOutputPartsRecord> partsRecord = desk.getPartRecordsForDiff(
          outputDiff.summaryId,
          baseInfo,
          compareInfo);
      if (MdUtils.isNullOrEmpty(partsRecord)) {
        // add matches
        outputDiff.matchTables.add(
            new MdOutputMemberMatchTables(baseInfo, compareInfo));

        removedBaseInfoList.add(baseInfo);
        removedCompareInfoLIst.add(compareInfo);
        continue;
      }

      // add mismatch
      outputDiff.mismatchRecordTables.add(new MdOutputMemberMismatchRecordTable(baseInfo, partsRecord));

      removedBaseInfoList.add(baseInfo);
      removedCompareInfoLIst.add(compareInfo);
    }
    for (MdInfoDiff info : removedBaseInfoList) {
      baseInfoList.remove(info);
    }
    for (MdInfoDiff info : removedCompareInfoLIst) {
      compareInfoList.remove(info);
    }

    outputDiff.mismatchRecordTables.sort(Comparator.comparing(t -> t.tableName));
    outputDiff.matchTables.sort(Comparator.comparing(t -> t.tableName));
  }

  /**
   * register diff records.
   * @param summaryId summary id
   * @param baseInfo base info
   * @param compareInfo compare info
   * @throws SQLException sql error
   */
  private void registerDiffRecords(String summaryId, MdInfoDiff baseInfo, MdInfoDiff compareInfo) throws SQLException {
    List<String> baseColumnNames = new ArrayList<>();
    List<String> compareColumnNames = new ArrayList<>();
    List<String> baseDynamicColumnNames = new ArrayList<>();
    List<String> compareDynamicColumnNames = new ArrayList<>();
    List<String> realColumnNames = new ArrayList<>();
    for (MdInfoDiffColumn column : baseInfo.getRealColumns()) {
      baseColumnNames.add(
          String.format(
              "md_b2c.cl_%s as mdb_%s",
              column.getColumnName(),
              column.getColumnName()));

      compareColumnNames.add(
          String.format(
              "md_c2b.cl_%s as mdc_%s",
              column.getColumnName(),
              column.getColumnName()));

      baseDynamicColumnNames.add(
          String.format("'%s', mdb_%s",
              column.getColumnName(),
              column.getColumnName()));

      compareDynamicColumnNames.add(
          String.format("'%s', mdc_%s",
              column.getColumnName(),
              column.getColumnName()));

      if (column.hasCollation()) {
        realColumnNames.add(String.format(
            "`%s` collate %s as cl_%s",
            column.getColumnName(),
            column.getBinaryCollationName(),
            column.getColumnName()));
      } else {
        realColumnNames.add(String.format(
            "`%s` as cl_%s",
            column.getColumnName(),
            column.getColumnName()));
      }
    }

    List<String> conditions = new ArrayList<>();
    for (MdInfoDiffColumn column : baseInfo.getPrimaryColumns()) {
      String condition = String.format(
          "md_b2c.cl_%s = md_c2b.cl_%s",
          column.getColumnName(),
          column.getColumnName());
      if (column.hasCollation()) {
        condition = String.format("%s collate %s", condition, column.getBinaryCollationName());
      }
      conditions.add(condition);
    }

    String seqExpression = "";
    String dynamicExpression = "";
    String additionalExpression = "";
    if (con.getDbType() == DbType.MYSQL) {
      seqExpression = "`magentadesk`.`nextDiffSeq`()";
      dynamicExpression = "JSON_OBJECT";
      additionalExpression = String.format(
          "WHERE JSON_OBJECT(%s) != JSON_OBJECT(%s)",
          MdUtils.join(baseDynamicColumnNames, ", "),
          MdUtils.join(compareDynamicColumnNames, ", ")
          );

    } else {
      seqExpression = "nextval(`magentadesk`.`diffSequence`)";
      dynamicExpression = "COLUMN_CREATE";
    }

    String sql = String.format(
        "INSERT INTO `magentadesk`.`diffRecord` (`summaryId`, `tableName`, `diffSeq`, `baseValues`, `compareValues`) " +
            "WITH " +
            "md_b2c AS (" +
            "  SELECT %s FROM `%s`.`%s` %s EXCEPT SELECT %s FROM `%s`.`%s` %s" +
            ")," +
            "md_c2b AS (" +
            "  SELECT %s FROM `%s`.`%s` %s EXCEPT SELECT %s FROM `%s`.`%s` %s" +
            ")," +
            "md_left AS (" +
            "  SELECT %s, %s FROM md_b2c LEFT OUTER JOIN md_c2b ON %s" +
            ")," +
            "md_right AS (" +
            "  SELECT %s, %s FROM md_b2c RIGHT OUTER JOIN md_c2b ON %s" +
            "), " +
            "md_full AS (" +
            "  SELECT * FROM md_left UNION SELECT * FROM md_right" +
            ") " +
            "SELECT " +
            "  '%s' as summaryId, " +
            "  '%s' as tableName, " +
            "  %s, " +
            "  %s(%s) as baseValues, " +
            "  %s(%s) as compareValues " +
            "FROM " +
            "  md_full " +
            "%s",
        // md_b2c
        MdUtils.join(realColumnNames, ", "),
        baseInfo.getDatabase(),
        baseInfo.getTableName(),
        baseInfo.getWhereExpression(),
        MdUtils.join(realColumnNames, ", "),
        compareInfo.getDatabase(),
        compareInfo.getTableName(),
        compareInfo.getWhereExpression(),
        // md_c2b
        MdUtils.join(realColumnNames, ", "),
        compareInfo.getDatabase(),
        compareInfo.getTableName(),
        compareInfo.getWhereExpression(),
        MdUtils.join(realColumnNames, ", "),
        baseInfo.getDatabase(),
        baseInfo.getTableName(),
        baseInfo.getWhereExpression(),
        // md_left
        MdUtils.join(baseColumnNames, ", "),
        MdUtils.join(compareColumnNames, ", "),
        MdUtils.join(conditions, " AND "),
        // md_right
        MdUtils.join(baseColumnNames, ", "),
        MdUtils.join(compareColumnNames, ", "),
        MdUtils.join(conditions, " AND "),
        // SELECT
        summaryId,
        baseInfo.getTableName(),
        seqExpression,
        dynamicExpression,
        MdUtils.join(baseDynamicColumnNames, ", "),
        dynamicExpression,
        MdUtils.join(compareDynamicColumnNames, ", "),
        additionalExpression);
    con.execute(sql);
  }
}
