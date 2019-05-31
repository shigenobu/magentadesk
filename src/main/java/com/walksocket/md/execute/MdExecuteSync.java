package com.walksocket.md.execute;

import com.walksocket.md.*;
import com.walksocket.md.bash.MdBashCommand;
import com.walksocket.md.bash.MdBashResult;
import com.walksocket.md.bash.MdBashStdin;
import com.walksocket.md.exception.MdExceptionNoExistsDiffSeqs;
import com.walksocket.md.filter.*;
import com.walksocket.md.input.MdInputAbstract;
import com.walksocket.md.input.MdInputSync;
import com.walksocket.md.input.member.MdInputMemberCommand;
import com.walksocket.md.mariadb.MdMariadbConnection;
import com.walksocket.md.mariadb.MdMariadbRecord;
import com.walksocket.md.mariadb.MdMariadbUtils;
import com.walksocket.md.output.MdOutputAbstract;
import com.walksocket.md.output.MdOutputSync;
import com.walksocket.md.output.member.MdOutputMemberCommandResult;

import java.io.File;
import java.util.*;

/**
 * execute sync.
 */
public class MdExecuteSync extends MdExecuteAbstract {

  /**
   * constructor.
   * @param con mariadb connection
   */
  public MdExecuteSync(MdMariadbConnection con) {
    super(con);
  }

  @Override
  public MdOutputAbstract execute(MdInputAbstract input) throws Exception {
    MdInputSync inputSync = (MdInputSync) input;
    MdOutputSync outputSync = new MdOutputSync();
    String sql;
    List<MdMariadbRecord> records;

    // -----
    // read diffSummary
    String baseDatabase = null;
    String compareDatabase = null;

    sql = String.format(
        "SELECT `baseDatabase`, `compareDatabase` " +
            "FROM `magentadesk`.`diffSummary` " +
            "WHERE `summaryId` = '%s'",
        MdMariadbUtils.quote(inputSync.summaryId));
    records = con.getRecords(sql);
    for (MdMariadbRecord record : records) {
      baseDatabase = record.get("baseDatabase");
      compareDatabase = record.get("compareDatabase");
    }
    if (MdUtils.isNullOrEmpty(baseDatabase) || MdUtils.isNullOrEmpty(compareDatabase)) {
      throw new Exception();
    }

    // -----
    // lock
    lock(baseDatabase, compareDatabase);

    // -----
    // summaryId
    outputSync.summaryId = inputSync.summaryId;

    // -----
    // read diffRecord
    if (MdUtils.isNullOrEmpty(inputSync.diffSeqs)) {
      inputSync.diffSeqs = new ArrayList<>();

      // select `diffSeq` from `magentadesk`.`diffRecord`
      sql = String.format("SELECT `diffSeq` FROM `magentadesk`.`diffRecord` WHERE `summaryId` = '%s'",
          MdMariadbUtils.quote(inputSync.summaryId));
      records = con.getRecords(sql);
      for (MdMariadbRecord record : records) {
        long diffSeq = Long.parseLong(record.get("diffSeq"));
        inputSync.diffSeqs.add(diffSeq);
      }
    }
    if (MdUtils.isNullOrEmpty(inputSync.diffSeqs)) {
      throw new MdExceptionNoExistsDiffSeqs();
    }

    // -----
    // get table info
    List<MdInfoSync> infoList = MdInfoSync.createInfoList(con, baseDatabase, compareDatabase, inputSync);

    // -----
    // filter
    List<MdFilterSyncAbstract> filters = Arrays.asList(
        new MdFilterSyncReflect(con, inputSync.force));
    for (MdFilterSyncAbstract filter : filters) {
      filter.filter(infoList, outputSync);
    }

    // -----
    // delete from `magentadesk`.`diffSummary`
    if (inputSync.loose) {
      // delete from `magentadesk`.`diffSummary` WHERE `summaryId`
      sql = String.format("DELETE FROM `magentadesk`.`diffSummary` WHERE `summaryId` = '%s'",
          MdMariadbUtils.quote(inputSync.summaryId));
      con.execute(sql);
    } else {
      // delete from `magentadesk`.`diffSummary` WHERE `baseDatabase`, `compareDatabase`
      sql = String.format("DELETE FROM `magentadesk`.`diffSummary` WHERE `baseDatabase` = '%s' and `compareDatabase` = '%s'",
          baseDatabase,
          compareDatabase);
      con.execute(sql);
    }

    // -----
    // create reflected json file
    String reflectedJsonPath = MdEnv.getMdHome() + File.separator + "reflected_" + outputSync.summaryId + ".json";
    File f = new File(reflectedJsonPath);
    if (!f.exists()) {
      MdFile.writeString(
          reflectedJsonPath,
          MdJson.toJsonString(outputSync.reflectedRecordTables));
    }
    String stdin = MdJson.toJsonString(new MdBashStdin(inputSync.run, reflectedJsonPath));

    // -----
    // execute before commit commands
    for (MdInputMemberCommand cmd : inputSync.commandsBeforeCommit) {
      MdBashCommand command = new MdBashCommand(cmd.command, cmd.timeout);
      command.setStdin(stdin);
      MdBashResult result = MdBash.exec(command);
      if (result != null) {
        outputSync.commandResultsBeforeCommit.add(
            new MdOutputMemberCommandResult(result));
      }
    }

    // -----
    // commit
    if (inputSync.run) {
      con.commit();
    }

    // -----
    // execute after commit commands
    for (MdInputMemberCommand cmd : inputSync.commandsAfterCommit) {
      MdBashCommand command = new MdBashCommand(cmd.command, cmd.timeout);
      command.setStdin(stdin);
      MdBashResult result = MdBash.exec(command);
      if (result != null) {
        outputSync.commandResultsAfterCommit.add(
            new MdOutputMemberCommandResult(result));
      }
    }

    // -----
    // delete reflected json file
    if (f.exists()) {
      f.delete();
    }

    return outputSync;
  }
}
