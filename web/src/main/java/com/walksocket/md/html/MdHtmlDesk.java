package com.walksocket.md.html;

import com.walksocket.md.MdJson;
import com.walksocket.md.db.MdDbRecord;
import com.walksocket.md.input.MdInputAbstract;
import com.walksocket.md.input.MdInputDiff;
import com.walksocket.md.input.MdInputMaintenance;
import com.walksocket.md.input.MdInputSync;
import com.walksocket.md.input.member.MdInputMemberCommand;
import com.walksocket.md.input.member.MdInputMemberCondition;
import com.walksocket.md.input.member.MdInputMemberHttp;
import com.walksocket.md.input.member.MdInputMemberOption;
import com.walksocket.md.sqlite.MdSqliteConnection;

import java.sql.SQLException;
import java.util.Arrays;

/**
 * html desk.
 */
public class MdHtmlDesk {

  /**
   * sqlite connection.
   */
  private MdSqliteConnection con;

  /**
   * project record.
   */
  private MdDbRecord projectRecord;

  /**
   * constructor.
   * @param con sqlite connection
   */
  public MdHtmlDesk(MdSqliteConnection con) {
    this.con = con;
  }

  /**
   * get project record.
   * @param projectId projectId
   * @return project record
   * @throws SQLException sql error
   */
  public MdDbRecord getProjectRecord(int projectId) throws SQLException {
    String sql = "";
    if (projectRecord == null) {
      sql = String.format("SELECT * FROM project WHERE projectId = %s", projectId);
      projectRecord = con.getRecord(sql);
      if (projectRecord == null) {
        throw new SQLException(String.format("Not exists by %s", projectId));
      }
    }
    return projectRecord;
  }

  /**
   * make common input.
   * @param projectId projectId
   * @param input input
   * @throws SQLException sql error
   */
  private void makeCommonInput(int projectId, MdInputAbstract input) throws SQLException {
    String sql = "";

    // get project record
    projectRecord = getProjectRecord(projectId);

    // make
    input.host = projectRecord.get("host");
    input.port = Integer.parseInt(projectRecord.get("port"));
    input.user = projectRecord.get("user");
    input.pass = projectRecord.get("pass");
    input.charset = projectRecord.get("charset");
  }

  /**
   * make maintenance input.
   * @param projectId projectId
   * @param input input
   * @throws SQLException sql error
   */
  public void makeMaintenanceInput(int projectId, MdInputMaintenance input) throws SQLException {
    // common
    makeCommonInput(projectId, input);

    // maintenance
    input.baseDatabase = projectRecord.get("baseDatabase");
    input.compareDatabase = projectRecord.get("compareDatabase");
  }

  /**
   * make diff input.
   * @param projectId projectId
   * @param presetId presetId
   * @param input input
   * @throws SQLException sql error
   */
  public void makeDiffInput(int projectId, int presetId, MdInputDiff input) throws SQLException {
    // common
    makeCommonInput(projectId, input);

    // diff
    input.baseDatabase = projectRecord.get("baseDatabase");
    input.compareDatabase = projectRecord.get("compareDatabase");

    String sql = "";

    // select
    sql = String.format(
        "SELECT " +
          "t4.option," +
          "t4.conditions " +
          "FROM " +
          "project as t1 " +
          "join " +
          "projectPreset as t2 " +
          "on t1.projectId = t2.projectId " +
          "join " +
          "preset as t3 " +
          "on t2.presetId = t3.presetId " +
          "join " +
          "diffConfig as t4 " +
          "on t3.diffConfigId = t4.diffConfigId " +
          "WHERE " +
          "t1.projectId = %s " +
          "AND " +
          "t3.presetId = %s ",
        projectId,
        presetId);
    MdDbRecord record = con.getRecord(sql);
    if (record == null) {
      throw new SQLException(String.format("Not exists by %s %s", projectId, presetId));
    }
    input.option = MdJson.toObject(record.get("option"), MdInputMemberOption.class);
    input.conditions = Arrays.asList(MdJson.toObject(record.get("conditions"), MdInputMemberCondition[].class));
  }

  /**
   * make sync input.
   * @param projectId projectId
   * @param presetId presetId
   * @param input input
   * @throws SQLException sql error
   */
  public void makeSyncInput(int projectId, int presetId, MdInputSync input) throws SQLException {
    // common
    makeCommonInput(projectId, input);

    String sql = "";

    // select
    sql = String.format(
        "SELECT " +
            "t4.commandsBeforeCommit, " +
            "t4.commandsAfterCommit, " +
            "t4.httpCallbackBeforeCommit, " +
            "t4.httpCallbackAfterCommit " +
            "FROM " +
            "project as t1 " +
            "join " +
            "projectPreset as t2 " +
            "on t1.projectId = t2.projectId " +
            "join " +
            "preset as t3 " +
            "on t2.presetId = t3.presetId " +
            "join " +
            "syncConfig as t4 " +
            "on t3.syncConfigId = t4.syncConfigId " +
            "WHERE " +
            "t1.projectId = %s " +
            "AND " +
            "t3.presetId = %s ",
        projectId,
        presetId);
    MdDbRecord record = con.getRecord(sql);
    if (record == null) {
      throw new SQLException(String.format("Not exists by %s %s", projectId, presetId));
    }
    input.commandsBeforeCommit = Arrays.asList(MdJson.toObject(record.get("commandsBeforeCommit"), MdInputMemberCommand[].class));
    input.commandsAfterCommit = Arrays.asList(MdJson.toObject(record.get("commandsAfterCommit"), MdInputMemberCommand[].class));
    input.httpCallbackBeforeCommit = Arrays.asList(MdJson.toObject(record.get("httpCallbackBeforeCommit"), MdInputMemberHttp[].class));
    input.httpCallbackAfterCommit = Arrays.asList(MdJson.toObject(record.get("httpCallbackAfterCommit"), MdInputMemberHttp[].class));
  }
}
