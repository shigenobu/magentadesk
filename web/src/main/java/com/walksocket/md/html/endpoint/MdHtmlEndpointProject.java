package com.walksocket.md.html.endpoint;

import com.walksocket.md.MdTemplate;
import com.walksocket.md.MdUtils;
import com.walksocket.md.db.MdDbRecord;
import com.walksocket.md.html.MdHtmlSaveMode;
import com.walksocket.md.html.MdHtmlStatus;
import com.walksocket.md.server.MdServerRequest;
import com.walksocket.md.server.MdServerResponse;
import com.walksocket.md.sqlite.MdSqliteConnection;
import com.walksocket.md.sqlite.MdSqliteUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * html endpoint project.
 */
public class MdHtmlEndpointProject extends MdHtmlEndpointAbstract {

  @Override
  public void action(MdServerRequest request, MdServerResponse response, MdSqliteConnection con) throws Exception {
    String path = request.getPath();
    if (path.equals("/project/list/")) {
      list(request, response, con);
      return;
    } else if (path.equals("/project/edit/")) {
      edit(request, response, con);
      return;
    } else if (path.equals("/project/save/")) {
      save(request, response, con);
      return;
    } else if (path.equals("/project/delete/")) {
      delete(request, response, con);
      return;
    } else if (path.equals("/project/preset/")) {
      preset(request, response, con);
      return;
    }
    renderOtherWithLayout(response, MdHtmlStatus.NOT_FOUND);
  }

  /**
   * list.
   * @param request request
   * @param response response
   * @param con sqlite connection
   * @throws IOException error
   * @throws SQLException sql error
   */
  private void list(MdServerRequest request, MdServerResponse response, MdSqliteConnection con) throws IOException, SQLException {
    // template
    MdTemplate template = createTemplate("html/project/list.vm");

    String sql = "";

    // select
    sql = "SELECT " +
        "t1.projectId, " +
        "t1.title, " +
        "t1.explanation, " +
        "t1.host, " +
        "t1.port, " +
        "t1.charset, " +
        "t1.dbType, " +
        "t1.baseDatabase, " +
        "t1.compareDatabase, " +
        "ifnull(count(t2.presetId), 0) as presetIdCount " +
        "FROM project as t1 " +
        "LEFT OUTER JOIN projectPreset as t2 " +
        "ON t1.projectId = t2.projectId " +
        "GROUP BY t1.projectId " +
        "ORDER BY t1.projectId DESC";
    List<MdDbRecord> records = con.getRecords(sql);
    template.assign("records", records);

    // render
    renderOkWithLayout(response, template);
  }

  /**
   * edit.
   * @param request request
   * @param response response
   * @param con sqlite connection
   * @throws IOException error
   * @throws SQLException sql error
   */
  private void edit(MdServerRequest request, MdServerResponse response, MdSqliteConnection con) throws IOException, SQLException {
    // template
    MdTemplate template = createTemplate("html/project/edit.vm");

    // param
    String tmpProjectId = request.getQueryParam("projectId");

    String sql = "";

    // select
    MdHtmlSaveMode saveMode = MdHtmlSaveMode.UPDATE;
    int projectId = 0;
    MdDbRecord record = null;
    Map<Integer, String> presetIdMap = new HashMap<>();
    if (!MdUtils.isNullOrEmpty(tmpProjectId)) {
      projectId = Integer.parseInt(tmpProjectId);

      sql = String.format("SELECT * FROM project WHERE projectId = %s", projectId);
      record = con.getRecord(sql);

      sql = String.format("SELECT presetId, no FROM projectPreset WHERE projectId = %s", projectId);
      List<MdDbRecord> projectPresetRecords = con.getRecords(sql);
      for (MdDbRecord r : projectPresetRecords) {
        presetIdMap.put(Integer.parseInt(r.get("no")), r.get("presetId"));
      }
    }
    if (record == null) {
      record = MdDbRecord.createEmptyRecord();
      saveMode = MdHtmlSaveMode.INSERT;
    }
    template.assign("record", record);
    template.assign("presetIdMap", presetIdMap);
    template.assign("saveMode", saveMode);

    // get all preset
    List<MdDbRecord> presetRecords = new ArrayList<>();
    sql = "SELECT presetId, title FROM preset ORDER BY presetId DESC";
    presetRecords = con.getRecords(sql);
    template.assign("presetRecords", presetRecords);

    // render
    renderOkWithLayout(response, template);
  }

  /**
   * save.
   * @param request request
   * @param response response
   * @param con sqlite connection
   * @throws IOException error
   * @throws SQLException sql error
   */
  private void save(MdServerRequest request, MdServerResponse response, MdSqliteConnection con) throws IOException, SQLException {
    // check
    if (!request.isPost()) {
      sendOther(response, MdHtmlStatus.METHOD_NOT_ALLOWED);
      return;
    }

    // param
    String saveMode = request.getBodyParam(MdHtmlSaveMode.getName());
    String title = request.getBodyParam("title");
    if (MdUtils.isNullOrEmpty(title)) {
      sendOther(response, MdHtmlStatus.BAD_REQUEST);
      return;
    }
    String explanation = request.getBodyParam("explanation");

    String host = request.getBodyParam("host");
    if (MdUtils.isNullOrEmpty(host)) {
      sendOther(response, MdHtmlStatus.BAD_REQUEST);
      return;
    }

    String tmpPort = request.getBodyParam("port");
    if (MdUtils.isNullOrEmpty(tmpPort)) {
      sendOther(response, MdHtmlStatus.BAD_REQUEST);
      return;
    }
    int port = Integer.parseInt(tmpPort);

    String user = request.getBodyParam("user");
    if (MdUtils.isNullOrEmpty(user)) {
      sendOther(response, MdHtmlStatus.BAD_REQUEST);
      return;
    }

    String pass = request.getBodyParam("pass");
    if (MdUtils.isNullOrEmpty(pass)) {
      sendOther(response, MdHtmlStatus.BAD_REQUEST);
      return;
    }

    String charset = request.getBodyParam("charset");
    if (MdUtils.isNullOrEmpty(charset)) {
      sendOther(response, MdHtmlStatus.BAD_REQUEST);
      return;
    }

    String dbType = request.getBodyParam("dbType");
    if (MdUtils.isNullOrEmpty(dbType)) {
      sendOther(response, MdHtmlStatus.BAD_REQUEST);
      return;
    }

    String baseDatabase = request.getBodyParam("baseDatabase");
    if (MdUtils.isNullOrEmpty(baseDatabase)) {
      sendOther(response, MdHtmlStatus.BAD_REQUEST);
      return;
    }

    String compareDatabase = request.getBodyParam("compareDatabase");
    if (MdUtils.isNullOrEmpty(compareDatabase)) {
      sendOther(response, MdHtmlStatus.BAD_REQUEST);
      return;
    }
    
    Map<Integer, String> presetIdMap = new HashMap<>();
    for (int i = 0; i < 30; i++) {
      if (MdUtils.isNullOrEmpty(request.getBodyParam(String.format("presetId%s", i)))) {
        continue;
      }
      presetIdMap.put(i, request.getBodyParam(String.format("presetId%s", i)));
    }

    String sql = "";

    // check
    if (presetIdMap.size() > 0) {
      sql = String.format(
          "SELECT count(presetId) as cnt FROM preset WHERE presetId in (%s)",
          presetIdMap.values().stream().collect(Collectors.joining(", ")));
      MdDbRecord rPreset = con.getRecord(sql);
      if (rPreset == null || Integer.parseInt(rPreset.get("cnt")) != presetIdMap.size()) {
        sendOther(response, MdHtmlStatus.BAD_REQUEST);
        return;
      }
    }

    int projectId = 0;
    if (saveMode.equals(MdHtmlSaveMode.INSERT.getValue())) {
      // insert
      sql = String.format(
          "INSERT INTO project " +
              "(title, explanation, host, port, user, pass, charset, dbType, baseDatabase, compareDatabase) " +
              "VALUES " +
              "('%s', '%s', '%s', %s, '%s', '%s', '%s', '%s', '%s', '%s')",
          MdSqliteUtils.quote(title),
          MdSqliteUtils.quote(explanation),
          MdSqliteUtils.quote(host),
          port,
          MdSqliteUtils.quote(user),
          MdSqliteUtils.quote(pass),
          MdSqliteUtils.quote(charset),
          MdSqliteUtils.quote(dbType),
          MdSqliteUtils.quote(baseDatabase),
          MdSqliteUtils.quote(compareDatabase));
      con.execute(sql);

      // id
      sql = "SELECT last_insert_rowid()";
      MdDbRecord record = con.getRecord(sql);
      projectId = Integer.parseInt(record.get("last_insert_rowid()"));

    } else if (saveMode.equals(MdHtmlSaveMode.UPDATE.getValue())) {
      // update
      String tmpProjectId = request.getBodyParam("projectId");
      projectId = Integer.parseInt(tmpProjectId);

      sql = String.format(
          "UPDATE project " +
              "SET " +
              "title = '%s', " +
              "explanation = '%s', " +
              "host = '%s', " +
              "port = %s, " +
              "user = '%s', " +
              "pass = '%s', " +
              "charset = '%s', " +
              "dbType = '%s', " +
              "baseDatabase = '%s', " +
              "compareDatabase = '%s' " +
              "WHERE projectId = %s",
          MdSqliteUtils.quote(title),
          MdSqliteUtils.quote(explanation),
          MdSqliteUtils.quote(host),
          port,
          MdSqliteUtils.quote(user),
          MdSqliteUtils.quote(pass),
          MdSqliteUtils.quote(charset),
          MdSqliteUtils.quote(dbType),
          MdSqliteUtils.quote(baseDatabase),
          MdSqliteUtils.quote(compareDatabase),
          projectId);
      con.execute(sql);
    }

    // delete
    sql = String.format("DELETE FROM projectPreset WHERE projectId = %s", projectId);
    con.execute(sql);

    // insert
    for (Map.Entry<Integer, String> entry : presetIdMap.entrySet()) {
      sql = String.format(
          "INSERT INTO projectPreset " +
              "(projectId, presetId, no) " +
              "VALUES " +
              "(%s, %s, %s)",
          projectId,
          entry.getValue(),
          entry.getKey());
      con.execute(sql);
    }

    // ok
    sendOk(response, String.format("/project/edit/?projectId=%s", projectId));
  }

  /**
   * delete.
   * @param request request
   * @param response response
   * @param con sqlite connection
   * @throws IOException error
   * @throws SQLException sql error
   */
  private void delete(MdServerRequest request, MdServerResponse response, MdSqliteConnection con) throws IOException, SQLException {
    // check
    if (!request.isDelete()) {
      sendOther(response, MdHtmlStatus.METHOD_NOT_ALLOWED);
      return;
    }

    // param
    String tmpProjectId = request.getQueryParam("projectId");
    if (MdUtils.isNullOrEmpty(tmpProjectId)) {
      sendOther(response, MdHtmlStatus.BAD_REQUEST);
      return;
    }
    int projectId = Integer.parseInt(tmpProjectId);

    String sql = "";

    // delete
    sql = String.format("DELETE FROM project WHERE projectId = %s", projectId);
    con.execute(sql);

    sql = String.format("DELETE FROM projectPreset WHERE projectId = %s", projectId);
    con.execute(sql);

    // ok
    sendOk(response, String.format("/project/list/"));
  }

  /**
   * preset.
   * @param request request
   * @param response response
   * @param con sqlite connection
   * @throws IOException error
   * @throws SQLException sql error
   */
  private void preset(MdServerRequest request, MdServerResponse response, MdSqliteConnection con) throws IOException, SQLException {
    // template
    MdTemplate template = createTemplate("html/project/preset.vm");

    // param
    String tmpProjectId = request.getQueryParam("projectId");
    if (MdUtils.isNullOrEmpty(tmpProjectId)) {
      renderOtherWithLayout(response, MdHtmlStatus.BAD_REQUEST);
      return;
    }
    int projectId = Integer.parseInt(tmpProjectId);

    String sql = "";

    // select
    sql = String.format("SELECT * FROM project WHERE projectId = %s", projectId);
    MdDbRecord record = con.getRecord(sql);
    if (record == null) {
      renderOtherWithLayout(response, MdHtmlStatus.NOT_FOUND);
      return;
    }
    template.assign("record", record);

    // select
    sql = String.format(
        "SELECT " +
            "t2.presetId, " +
            "t2.title, " +
            "t2.explanation, " +
            "t2.diffConfigId, " +
            "t2.syncConfigId " +
            "FROM projectPreset as t1 " +
            "JOIN preset as t2 " +
            "ON t1.presetId = t2.presetId " +
            "WHERE t1.projectId = %s " +
            "ORDER BY t1.no ASC",
        projectId);
    List<MdDbRecord> recordPresetList = con.getRecords(sql);
    template.assign("recordPresetList", recordPresetList);

    // get
    Map<String, String> diffTitleMap = new HashMap<>();
    Map<String, String> syncTitleMap = new HashMap<>();
    Set<String> diffConfigIds = new HashSet<>();
    Set<String> syncConfigIds = new HashSet<>();
    for (MdDbRecord r : recordPresetList) {
      diffConfigIds.add(r.get("diffConfigId"));
      syncConfigIds.add(r.get("syncConfigId"));
    }
    if (diffConfigIds.size() > 0) {
      sql = String.format(
          "SELECT diffConfigId, title FROM diffConfig WHERE diffConfigId in (%s)",
          diffConfigIds.stream().collect(Collectors.joining(", ")));
      List<MdDbRecord> rsDiff = con.getRecords(sql);
      for (MdDbRecord r : rsDiff) {
        diffTitleMap.put(r.get("diffConfigId"), r.get("title"));
      }
    }
    if (syncConfigIds.size() > 0) {
      sql = String.format(
          "SELECT syncConfigId, title FROM syncConfig WHERE syncConfigId in (%s)",
          syncConfigIds.stream().collect(Collectors.joining(", ")));
      List<MdDbRecord> rsSync = con.getRecords(sql);
      for (MdDbRecord r : rsSync) {
        syncTitleMap.put(r.get("syncConfigId"), r.get("title"));
      }
    }
    template.assign("diffTitleMap", diffTitleMap);
    template.assign("syncTitleMap", syncTitleMap);

    // render
    renderOkWithLayout(response, template);
  }
}
