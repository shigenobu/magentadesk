package com.walksocket.md.filter;

import com.walksocket.md.MdDesk;
import com.walksocket.md.MdInfoDiff;
import com.walksocket.md.MdLogger;
import com.walksocket.md.MdUtils;
import com.walksocket.md.db.MdDbConnection;
import com.walksocket.md.db.MdDbFactory.DbType;
import com.walksocket.md.info.MdInfoDiffColumn;
import com.walksocket.md.output.MdOutputDiff;
import com.walksocket.md.output.member.MdOutputMemberMatchTables;
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
  private final MdDesk desk;

  /**
   * constructor.
   *
   * @param con db connection
   */
  public MdFilterDiffMismatchRecordTables(MdDbConnection con) {
    super(con);

    this.desk = new MdDesk(con);
  }

  @Override
  public void filter(List<MdInfoDiff> baseInfoList, List<MdInfoDiff> compareInfoList,
      MdOutputDiff outputDiff) throws SQLException {
    List<MdInfoDiff> removedBaseInfoList = new ArrayList<>();
    List<MdInfoDiff> removedCompareInfoLIst = new ArrayList<>();

    for (MdInfoDiff baseInfo : baseInfoList) {
      MdInfoDiff compareInfo = compareInfoList
          .stream()
          .filter(i -> i.getTableName().equals(baseInfo.getTableName()))
          .findFirst()
          .get();

      // checksum
      String baseChecksum;
      String compareChecksum;
      try {
        ExecutorService service = Executors.newFixedThreadPool(2);
        CompletableFuture<String> baseFuture = baseInfo.getChecksumFuture(service);
        CompletableFuture<String> compareFuture = compareInfo.getChecksumFuture(service);
        baseChecksum = baseFuture.get(600, TimeUnit.SECONDS);
        compareChecksum = compareFuture.get(600, TimeUnit.SECONDS);
      } catch (Exception e) {
        MdLogger.error(e);
        throw new SQLException(e);
      }
      boolean matchChecksum = !MdUtils.isNullOrEmpty(baseChecksum)
          && !MdUtils.isNullOrEmpty(compareChecksum)
          && baseChecksum.equals(compareChecksum);
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
      outputDiff.mismatchRecordTables.add(
          new MdOutputMemberMismatchRecordTable(baseInfo, partsRecord));

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
   *
   * @param summaryId   summary id
   * @param baseInfo    base info
   * @param compareInfo compare info
   * @throws SQLException sql error
   */
  private void registerDiffRecords(String summaryId, MdInfoDiff baseInfo, MdInfoDiff compareInfo)
      throws SQLException {
    List<String> baseColumnNames = new ArrayList<>();
    List<String> compareColumnNames = new ArrayList<>();
    List<String> baseDynamicColumnNames = new ArrayList<>();
    List<String> compareDynamicColumnNames = new ArrayList<>();
    List<String> hashedPrimaryColumnNames = new ArrayList<>();
    List<String> hashedColumnNames = new ArrayList<>();
    for (MdInfoDiffColumn column : baseInfo.getRealColumns()) {
      baseColumnNames.add(
          String.format(
              "b.%s as mdb_%s",
              column.getColumnName(),
              column.getColumnName()));

      compareColumnNames.add(
          String.format(
              "c.%s as mdc_%s",
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

      hashedColumnNames.add(String.format(
          "MD5(IFNULL(`%s`, '<NULL>'))",
          column.getColumnName()));
    }

    List<String> joinBaseConditions = new ArrayList<>();
    List<String> joinCompareConditions = new ArrayList<>();
    for (MdInfoDiffColumn column : baseInfo.getPrimaryColumns()) {
      String joinBaseCondition = String.format(
          "p.%s = b.%s",
          column.getColumnName(),
          column.getColumnName());
      String joinCompareCondition = String.format(
          "p.%s = c.%s",
          column.getColumnName(),
          column.getColumnName());
      if (column.hasCollation()) {
        joinBaseCondition = String.format("%s collate %s", joinBaseCondition,
            column.getBinaryCollationName());
        joinCompareCondition = String.format("%s collate %s", joinCompareCondition,
            column.getBinaryCollationName());
      }
      joinBaseConditions.add(joinBaseCondition);
      joinCompareConditions.add(joinCompareCondition);

      if (column.hasCollation()) {
        hashedPrimaryColumnNames.add(String.format(
            "`%s` collate %s as %s",
            column.getColumnName(),
            column.getBinaryCollationName(),
            column.getColumnName()));
      } else {
        hashedPrimaryColumnNames.add(String.format(
            "`%s` as %s",
            column.getColumnName(),
            column.getColumnName()));
      }
    }

    String seqExpression;
    String dynamicExpression;
    if (con.getDbType() == DbType.MYSQL) {
      seqExpression = "`magentadesk`.`nextDiffSeq`()";
      dynamicExpression = "JSON_OBJECT";
    } else {
      seqExpression = "nextval(`magentadesk`.`diffSequence`)";
      dynamicExpression = "COLUMN_CREATE";
    }

    String hashedColumns = MdUtils.join(hashedPrimaryColumnNames, ", ");
    if (hashedColumnNames.size() > 0) {
      hashedColumns += ", " + String.format("MD5(CONCAT(%s)) as md_row_hash",
          MdUtils.join(hashedColumnNames, ", "));
    }

    String sql = String.format(
        "INSERT INTO `magentadesk`.`diffRecord` (`summaryId`, `tableName`, `diffSeq`, `baseValues`, `compareValues`) "
            +
            "WITH " +
            "md_b2c_hash AS (" +
            "  SELECT %s FROM `%s`.`%s` %s EXCEPT SELECT %s FROM `%s`.`%s` %s" +
            ")," +
            "md_c2b_hash AS (" +
            "  SELECT %s FROM `%s`.`%s` %s EXCEPT SELECT %s FROM `%s`.`%s` %s" +
            ")," +
            "md_cmn_pk AS (" +
            "  SELECT %s FROM md_b2c_hash UNION SELECT %s FROM md_c2b_hash" +
            ")," +
            "md_full AS (" +
            "  SELECT %s, %s FROM md_cmn_pk as p LEFT OUTER JOIN `%s`.`%s` as b ON %s LEFT OUTER JOIN `%s`.`%s` as c ON %s"
            +
            ") " +
            "SELECT " +
            "  '%s' as summaryId, " +
            "  '%s' as tableName, " +
            "  %s, " +
            "  %s(%s) as baseValues, " +
            "  %s(%s) as compareValues " +
            "FROM " +
            "  md_full",
        // md_b2c_hash
        hashedColumns,
        baseInfo.getDatabase(),
        baseInfo.getTableName(),
        baseInfo.getWhereExpression(),
        hashedColumns,
        compareInfo.getDatabase(),
        compareInfo.getTableName(),
        compareInfo.getWhereExpression(),
        // md_c2b_hash
        hashedColumns,
        compareInfo.getDatabase(),
        compareInfo.getTableName(),
        compareInfo.getWhereExpression(),
        hashedColumns,
        baseInfo.getDatabase(),
        baseInfo.getTableName(),
        baseInfo.getWhereExpression(),
        // md_cmn_pk
        MdUtils.join(hashedPrimaryColumnNames, ", "),
        MdUtils.join(hashedPrimaryColumnNames, ", "),
        // md_full
        MdUtils.join(baseColumnNames, ", "),
        MdUtils.join(compareColumnNames, ", "),
        baseInfo.getDatabase(),
        baseInfo.getTableName(),
        MdUtils.join(joinBaseConditions, " AND "),
        compareInfo.getDatabase(),
        compareInfo.getTableName(),
        MdUtils.join(joinCompareConditions, " AND "),
        // SELECT
        summaryId,
        baseInfo.getTableName(),
        seqExpression,
        dynamicExpression,
        MdUtils.join(baseDynamicColumnNames, ", "),
        dynamicExpression,
        MdUtils.join(compareDynamicColumnNames, ", "));
    con.execute(sql);
  }
}
