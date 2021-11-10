package com.walksocket.md.html.endpoint;

import com.sun.net.httpserver.HttpExchange;
import com.walksocket.md.MdLogger;
import com.walksocket.md.MdTemplate;
import com.walksocket.md.MdUtils;
import com.walksocket.md.db.MdDbRecord;
import com.walksocket.md.sqlite.MdSqliteConnection;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class MdHtmlEndpointSyncConfig extends MdHtmlEndpointAbstract {
  @Override
  public void handle(HttpExchange exchange) throws IOException {
    init(exchange);
    MdLogger.trace(getClass().getSimpleName() + ":" + request.getPath());

    try (MdSqliteConnection con = new MdSqliteConnection()) {
      // begin
      con.begin();

      String path = request.getPath();
      if (path.equals("/syncConfig/list/")) {
        list(con);
        return;
      } else if (path.equals("/syncConfig/edit/")) {
        edit(con);
        return;
      } else if (path.equals("/syncConfig/save/")) {

      } else if (path.equals("/syncConfig/remove/")) {

      }
      renderNotFound();

      // commit
      con.commit();

    } catch (Exception e) {
      MdLogger.error(e);
    }
  }

  private void list(MdSqliteConnection con) throws IOException, SQLException {
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
    renderWithLayout(template);
  }

  private void edit(MdSqliteConnection con) throws IOException, SQLException {
    // template
    MdTemplate template = createTemplate("html/syncConfig/edit.vm");

    // param
    String tmpSyncConfigId = request.getQueryParam("syncConfigId");

    // select
    int syncConfigId = 0;
    MdDbRecord record = new MdDbRecord();
    if (!MdUtils.isNullOrEmpty(tmpSyncConfigId)) {
      syncConfigId = Integer.parseInt(tmpSyncConfigId);

      String sql = String.format("SELECT * FROM syncConfig where syncConfigId = %s", syncConfigId);
      record = con.getRecord(sql);
    }
    template.assign("record", record);

    // render
    renderWithLayout(template);
  }
}
