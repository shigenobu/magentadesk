package com.walksocket.md.html.endpoint;

import com.walksocket.md.MdJson;
import com.walksocket.md.MdLogger;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * html endpoint sync config.
 */
public class MdHtmlEndpointSyncConfig extends MdHtmlEndpointAbstract {

  @Override
  public void action(MdServerRequest request, MdServerResponse response, MdSqliteConnection con) throws Exception {
    String path = request.getPath();
    if (path.equals("/syncConfig/list/")) {
      list(request, response, con);
      return;
    } else if (path.equals("/syncConfig/edit/")) {
      edit(request, response, con);
      return;
    } else if (path.equals("/syncConfig/save/")) {
      save(request, response, con);
      return;
    } else if (path.equals("/syncConfig/delete/")) {

    }
    renderOtherWithLayout(response, MdHtmlStatus.NOT_FOUND);
  }

  private void list(MdServerRequest request, MdServerResponse response, MdSqliteConnection con) throws IOException, SQLException {
    // template
    MdTemplate template = createTemplate("html/syncConfig/list.vm");

    // select
    String sql = "SELECT " +
        "syncConfigId, " +
        "title, " +
        "explanation, " +
        "json_array_length(commandsBeforeCommit) as commandsBeforeCommitCount, " +
        "json_array_length(commandsAfterCommit) as commandsAfterCommitCount, " +
        "json_array_length(httpCallbackBeforeCommit) as httpCallbackBeforeCommitCount, " +
        "json_array_length(httpCallbackAfterCommit) as httpCallbackAfterCommitCount " +
        "FROM syncConfig " +
        "ORDER BY syncConfigId DESC";
    List<MdDbRecord> records = con.getRecords(sql);
    template.assign("records", records);

    // render
    renderOkWithLayout(response, template);
  }

  private void edit(MdServerRequest request, MdServerResponse response, MdSqliteConnection con) throws IOException, SQLException {
    // template
    MdTemplate template = createTemplate("html/syncConfig/edit.vm");

    // param
    String tmpSyncConfigId = request.getQueryParam("syncConfigId");

    // select
    MdHtmlSaveMode saveMode = MdHtmlSaveMode.UPDATE;
    int syncConfigId = 0;
    MdDbRecord record = null;
    if (!MdUtils.isNullOrEmpty(tmpSyncConfigId)) {
      syncConfigId = Integer.parseInt(tmpSyncConfigId);

      String sql = String.format("SELECT * FROM syncConfig where syncConfigId = %s", syncConfigId);
      record = con.getRecord(sql);
    }
    if (record == null) {
      record = MdDbRecord.createEmptyRecord();
      saveMode = MdHtmlSaveMode.INSERT;
    }
    template.assign("record", record);
    template.assign("saveMode", saveMode);

    int idx = 0;

    // commandsBeforeCommit
    List<MdInputMemberCommand> commandsBeforeCommit = new ArrayList<>();
    commandsBeforeCommit.add(0, new MdInputMemberCommand("", 30, Arrays.asList(0, 0, 0)));
    commandsBeforeCommit.add(1, new MdInputMemberCommand("", 30, Arrays.asList(0, 0, 0)));
    commandsBeforeCommit.add(2, new MdInputMemberCommand("", 30, Arrays.asList(0, 0, 0)));
    idx = 0;
    if (!MdUtils.isNullOrEmpty(record.getOrEmpty("commandsBeforeCommit"))) {
      for (MdInputMemberCommand c : MdJson.toObject(record.getOrEmpty("commandsBeforeCommit"), MdInputMemberCommand[].class)) {
        if (c != null) {
          commandsBeforeCommit.remove(idx);
          commandsBeforeCommit.add(idx, c);
          idx++;
        }
      }
    }
    template.assign("commandsBeforeCommit", commandsBeforeCommit);

    // commandsBeforeCommit
    List<MdInputMemberCommand> commandsAfterCommit = new ArrayList<>();
    commandsAfterCommit.add(0, new MdInputMemberCommand("", 30, Arrays.asList(0, 0, 0)));
    commandsAfterCommit.add(1, new MdInputMemberCommand("", 30, Arrays.asList(0, 0, 0)));
    commandsAfterCommit.add(2, new MdInputMemberCommand("", 30, Arrays.asList(0, 0, 0)));
    idx = 0;
    if (!MdUtils.isNullOrEmpty(record.getOrEmpty("commandsAfterCommit"))) {
      for (MdInputMemberCommand c : MdJson.toObject(record.getOrEmpty("commandsAfterCommit"), MdInputMemberCommand[].class)) {
        if (c != null) {
          commandsAfterCommit.remove(idx);
          commandsAfterCommit.add(idx, c);
          idx++;
        }
      }
    }
    template.assign("commandsAfterCommit", commandsAfterCommit);

    // httpCallbackBeforeCommit
    List<MdInputMemberHttp> httpCallbackBeforeCommit = new ArrayList<>();
    httpCallbackBeforeCommit.add(0, new MdInputMemberHttp("", 30, Arrays.asList(200, 200, 200)));
    httpCallbackBeforeCommit.add(1, new MdInputMemberHttp("", 30, Arrays.asList(200, 200, 200)));
    httpCallbackBeforeCommit.add(2, new MdInputMemberHttp("", 30, Arrays.asList(200, 200, 200)));
    idx = 0;
    if (!MdUtils.isNullOrEmpty(record.getOrEmpty("httpCallbackBeforeCommit"))) {
      for (MdInputMemberHttp h : MdJson.toObject(record.getOrEmpty("httpCallbackBeforeCommit"), MdInputMemberHttp[].class)) {
        if (h != null) {
          httpCallbackBeforeCommit.remove(idx);
          httpCallbackBeforeCommit.add(idx, h);
          idx++;
        }
      }
    }
    template.assign("httpCallbackBeforeCommit", httpCallbackBeforeCommit);

    // httpCallbackAfterCommit
    List<MdInputMemberHttp> httpCallbackAfterCommit = new ArrayList<>();
    httpCallbackAfterCommit.add(0, new MdInputMemberHttp("", 30, Arrays.asList(200, 200, 200)));
    httpCallbackAfterCommit.add(1, new MdInputMemberHttp("", 30, Arrays.asList(200, 200, 200)));
    httpCallbackAfterCommit.add(2, new MdInputMemberHttp("", 30, Arrays.asList(200, 200, 200)));
    idx = 0;
    if (!MdUtils.isNullOrEmpty(record.getOrEmpty("httpCallbackAfterCommit"))) {
      for (MdInputMemberHttp h : MdJson.toObject(record.getOrEmpty("httpCallbackAfterCommit"), MdInputMemberHttp[].class)) {
        if (h != null) {
          httpCallbackAfterCommit.remove(idx);
          httpCallbackAfterCommit.add(idx, h);
          idx++;
        }
      }
    }
    template.assign("httpCallbackAfterCommit", httpCallbackAfterCommit);

    // render
    renderOkWithLayout(response, template);
  }

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

    int syncConfigId = 0;
    String sql = "";
    if (saveMode.equals(MdHtmlSaveMode.INSERT.getValue())) {
      // insert
      sql = String.format(
          "INSERT INTO syncConfig (title, explanation) VALUES ('%s', '%s')",
          MdSqliteUtils.quote(title),
          MdSqliteUtils.quote(explanation));
      con.execute(sql);

      // id
      sql = "SELECT last_insert_rowid()";
      MdDbRecord record = con.getRecord(sql);
      syncConfigId = Integer.parseInt(record.get("last_insert_rowid()"));

    } else if (saveMode.equals(MdHtmlSaveMode.UPDATE.getValue())) {
      // update
      String tmpSyncConfigId = request.getBodyParam("syncConfigId");
      syncConfigId = Integer.parseInt(tmpSyncConfigId);


    }

    // ok
    sendOk(response, String.format("/syncConfig/edit/?syncConfigId=%s", syncConfigId));
  }
}
