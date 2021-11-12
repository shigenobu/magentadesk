package com.walksocket.md.html.endpoint;

import com.walksocket.md.MdJson;
import com.walksocket.md.MdTemplate;
import com.walksocket.md.MdUtils;
import com.walksocket.md.db.MdDbRecord;
import com.walksocket.md.html.MdHtmlSaveMode;
import com.walksocket.md.html.MdHtmlStatus;
import com.walksocket.md.input.member.MdInputMemberCommand;
import com.walksocket.md.input.member.MdInputMemberHttp;
import com.walksocket.md.server.MdServerRequest;
import com.walksocket.md.server.MdServerResponse;
import com.walksocket.md.sqlite.MdSqliteConnection;
import com.walksocket.md.sqlite.MdSqliteUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * html endpoint preset.
 */
public class MdHtmlEndpointPreset extends MdHtmlEndpointAbstract {

  @Override
  public void action(MdServerRequest request, MdServerResponse response, MdSqliteConnection con) throws Exception {
    String path = request.getPath();
    if (path.equals("/preset/list/")) {
      list(request, response, con);
      return;
    } else if (path.equals("/preset/edit/")) {
      edit(request, response, con);
      return;
    } else if (path.equals("/preset/save/")) {
      save(request, response, con);
      return;
    } else if (path.equals("/preset/delete/")) {
      delete(request, response, con);
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
    MdTemplate template = createTemplate("html/preset/list.vm");

    String sql = "";

    // select
    sql = "SELECT " +
        "presetId, " +
        "title, " +
        "explanation, " +
        "diffConfigId, " +
        "syncConfigId " +
        "FROM preset " +
        "ORDER BY presetId DESC";
    List<MdDbRecord> records = con.getRecords(sql);
    template.assign("records", records);

    // get
    Map<String, String> diffTitleMap = new HashMap<>();
    Map<String, String> syncTitleMap = new HashMap<>();
    Set<String> diffConfigIds = new HashSet<>();
    Set<String> syncConfigIds = new HashSet<>();
    for (MdDbRecord record : records) {
      diffConfigIds.add(record.get("diffConfigId"));
      syncConfigIds.add(record.get("syncConfigId"));
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
    MdTemplate template = createTemplate("html/preset/edit.vm");

    // param
    String tmpPresetId = request.getQueryParam("presetId");

    String sql = "";

    // select
    MdHtmlSaveMode saveMode = MdHtmlSaveMode.UPDATE;
    int presetId = 0;
    MdDbRecord record = null;
    if (!MdUtils.isNullOrEmpty(tmpPresetId)) {
      presetId = Integer.parseInt(tmpPresetId);

      sql = String.format("SELECT * FROM preset WHERE presetId = %s", presetId);
      record = con.getRecord(sql);
    }
    if (record == null) {
      record = MdDbRecord.createEmptyRecord();
      saveMode = MdHtmlSaveMode.INSERT;
    }
    template.assign("record", record);
    template.assign("saveMode", saveMode);

    // get all diff
    List<MdDbRecord> diffRecords = new ArrayList<>();
    sql = "SELECT diffConfigId, title FROM diffConfig ORDER BY diffConfigId DESC";
    diffRecords = con.getRecords(sql);
    template.assign("diffRecords", diffRecords);

    // get all sync
    List<MdDbRecord> syncRecords = new ArrayList<>();
    sql = "SELECT syncConfigId, title FROM syncConfig ORDER BY syncConfigId DESC";
    syncRecords = con.getRecords(sql);
    template.assign("syncRecords", syncRecords);

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

    String tmpDiffConfigId = request.getBodyParam("diffConfigId");
    if (MdUtils.isNullOrEmpty(tmpDiffConfigId)) {
      sendOther(response, MdHtmlStatus.BAD_REQUEST);
      return;
    }
    int diffConfigId = Integer.parseInt(tmpDiffConfigId);

    String tmpSyncConfigId = request.getBodyParam("syncConfigId");
    if (MdUtils.isNullOrEmpty(tmpSyncConfigId)) {
      sendOther(response, MdHtmlStatus.BAD_REQUEST);
      return;
    }
    int syncConfigId = Integer.parseInt(tmpSyncConfigId);

    String sql = "";

    // check
    sql = String.format("SELECT diffConfigId FROM diffConfig WHERE diffConfigId = %s", diffConfigId);
    MdDbRecord rDiff = con.getRecord(sql);
    if (rDiff == null) {
      sendOther(response, MdHtmlStatus.BAD_REQUEST);
      return;
    }

    sql = String.format("SELECT syncConfigId FROM syncConfig WHERE syncConfigId = %s", syncConfigId);
    MdDbRecord rSync = con.getRecord(sql);
    if (rSync == null) {
      sendOther(response, MdHtmlStatus.BAD_REQUEST);
      return;
    }

    int presetId = 0;
    if (saveMode.equals(MdHtmlSaveMode.INSERT.getValue())) {
      // insert
      sql = String.format(
          "INSERT INTO preset " +
              "(title, explanation, diffConfigId, syncConfigId) " +
              "VALUES " +
              "('%s', '%s', %s, %s)",
          MdSqliteUtils.quote(title),
          MdSqliteUtils.quote(explanation),
          diffConfigId,
          syncConfigId);
      con.execute(sql);

      // id
      sql = "SELECT last_insert_rowid()";
      MdDbRecord record = con.getRecord(sql);
      presetId = Integer.parseInt(record.get("last_insert_rowid()"));

    } else if (saveMode.equals(MdHtmlSaveMode.UPDATE.getValue())) {
      // update
      String tmpPresetId = request.getBodyParam("presetId");
      presetId = Integer.parseInt(tmpPresetId);

      sql = String.format(
          "UPDATE preset " +
              "SET " +
              "title = '%s', " +
              "explanation = '%s', " +
              "diffConfigId = %s, " +
              "syncConfigId = %s " +
              "WHERE presetId = %s",
          MdSqliteUtils.quote(title),
          MdSqliteUtils.quote(explanation),
          diffConfigId,
          syncConfigId,
          presetId);
      con.execute(sql);
    }

    // ok
    sendOk(response, String.format("/preset/edit/?presetId=%s", presetId));
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
    String tmpPresetId = request.getQueryParam("presetId");
    if (MdUtils.isNullOrEmpty(tmpPresetId)) {
      sendOther(response, MdHtmlStatus.BAD_REQUEST);
      return;
    }
    int presetId = Integer.parseInt(tmpPresetId);

    String sql = "";

    // check
    sql = String.format("SELECT projectId FROM projectPreset WHERE presetId = %s", presetId);
    MdDbRecord record = con.getRecord(sql);
    if (record != null) {
      sendOther(response, MdHtmlStatus.CONFLICT);
      return;
    }

    // delete
    sql = String.format("DELETE FROM preset WHERE presetId = %s", presetId);
    con.execute(sql);

    // ok
    sendOk(response, String.format("/preset/list/"));
  }
}
