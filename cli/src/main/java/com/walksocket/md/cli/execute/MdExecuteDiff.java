package com.walksocket.md.cli.execute;

import com.walksocket.md.cli.MdInfoDiff;
import com.walksocket.md.cli.MdJson;
import com.walksocket.md.cli.MdLogger;
import com.walksocket.md.cli.MdUtils;
import com.walksocket.md.cli.filter.*;
import com.walksocket.md.cli.input.MdInputAbstract;
import com.walksocket.md.cli.input.MdInputDiff;
import com.walksocket.md.cli.mariadb.MdMariadbConnection;
import com.walksocket.md.cli.mariadb.MdMariadbUtils;
import com.walksocket.md.cli.output.MdOutputAbstract;
import com.walksocket.md.cli.output.MdOutputDiff;
import com.walksocket.md.cli.output.member.MdOutputMemberMismatchRecordTable;

import java.util.Arrays;
import java.util.List;

/**
 * execute diff.
 */
public class MdExecuteDiff extends MdExecuteAbstract {

  /**
   * constructor.
   * @param con mariadb connection
   */
  public MdExecuteDiff(MdMariadbConnection con) {
    super(con);
  }

  @Override
  public MdOutputAbstract execute(MdInputAbstract input) throws Exception {
    MdInputDiff inputDiff = (MdInputDiff) input;
    MdOutputDiff outputDiff = new MdOutputDiff();
    String sql = "";

    // -----
    // lock
    lock(inputDiff.baseDatabase, inputDiff.compareDatabase);

    // -----
    // check maintenance
    checkMaintenance(inputDiff.baseDatabase, inputDiff.compareDatabase);

    // -----
    // summaryId
    outputDiff.summaryId = MdUtils.randomString();

    // -----
    // insert diffSummary
    sql = String.format(
        "INSERT INTO `magentadesk`.`diffSummary` " +
            "(`summaryId`," +
            "`baseDatabase`," +
            "`compareDatabase`) " +
            "VALUES " +
            "('%s'," +
            "'%s'," +
            "'%s')",
        MdMariadbUtils.quote(outputDiff.summaryId),
        MdMariadbUtils.quote(inputDiff.baseDatabase),
        MdMariadbUtils.quote(inputDiff.compareDatabase));
    con.execute(sql);

    // -----
    // get table info
    List<MdInfoDiff> baseInfoList = MdInfoDiff.createInfoList(
        con,
        inputDiff.baseDatabase,
        inputDiff.option,
        inputDiff.conditions);
    List<MdInfoDiff> compareInfoList = MdInfoDiff.createInfoList(
        con,
        inputDiff.compareDatabase,
        inputDiff.option,
        inputDiff.conditions);

    // -----
    // filter
    List<MdFilterDiffAbstract> filters = Arrays.asList(
        new MdFilterDiffExistsOnlyBaseTables(con),
        new MdFilterDiffExistsOnlyCompareTables(con),
        new MdFilterDiffForceExcludeTables(con),
        new MdFilterDiffIncorrectDefinitionTables(con),
        new MdFilterDiffMismatchDefinitionTables(con),
        new MdFilterDiffMismatchRecordTables(con));
    for (MdFilterDiffAbstract filter : filters) {
      filter.filter(baseInfoList, compareInfoList, outputDiff);
    }
    if (!MdUtils.isNullOrEmpty(baseInfoList) || !MdUtils.isNullOrEmpty(compareInfoList)) {
      MdLogger.error(String.format("left base info list:%s", baseInfoList));
      MdLogger.error(String.format("left compare info list:%s", compareInfoList));
      throw new RuntimeException("Error diff filter.");
    }

    // -----
    // insert diffTable
    for (MdOutputMemberMismatchRecordTable mismatchRecordTable : outputDiff.mismatchRecordTables) {
      sql = String.format(
          "INSERT INTO `magentadesk`.`diffTable` " +
              "(`summaryId`, " +
              "`tableName`, " +
              "`tableComment`, " +
              "`columns`) " +
              "VALUES " +
              "('%s', '%s', '%s', '%s')",
          MdMariadbUtils.quote(outputDiff.summaryId),
          MdMariadbUtils.quote(mismatchRecordTable.tableName),
          MdMariadbUtils.quote(mismatchRecordTable.tableComment),
          MdMariadbUtils.quote(MdJson.toJsonString(mismatchRecordTable.columns))
      );
      con.execute(sql);
    }

    // -----
    // commit
    con.commit();

    return outputDiff;
  }
}
