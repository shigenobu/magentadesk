package com.walksocket.md.html.endpoint;

import com.walksocket.md.MdJson;
import com.walksocket.md.MdTemplate;
import com.walksocket.md.MdUtils;
import com.walksocket.md.db.MdDbRecord;
import com.walksocket.md.html.MdHtmlSaveMode;
import com.walksocket.md.html.MdHtmlStatus;
import com.walksocket.md.input.member.MdInputMemberCondition;
import com.walksocket.md.input.member.MdInputMemberOption;
import com.walksocket.md.input.member.MdInputMemberRelation;
import com.walksocket.md.server.MdServerRequest;
import com.walksocket.md.server.MdServerResponse;
import com.walksocket.md.sqlite.MdSqliteConnection;
import com.walksocket.md.sqlite.MdSqliteUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * html endpoint diff config.
 */
public class MdHtmlEndpointDiffConfig extends MdHtmlEndpointAbstract {

  @Override
  public void action(MdServerRequest request, MdServerResponse response, MdSqliteConnection con) throws Exception {
    String path = request.getPath();
    if (path.equals("/diffConfig/list/")) {
      list(request, response, con);
      return;
    } else if (path.equals("/diffConfig/edit/")) {
      edit(request, response, con);
      return;
    } else if (path.equals("/diffConfig/save/")) {
      save(request, response, con);
      return;
    } else if (path.equals("/diffConfig/delete/")) {
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
    MdTemplate template = createTemplate("html/diffConfig/list.vm");

    // select
    String sql = "SELECT " +
        "diffConfigId, " +
        "title, " +
        "explanation, " +
        "ifnull(json_array_length(json_extract(option, '$.includeTableLikePatterns')), 0) as includeTableLikePatternsCount, " +
        "ifnull(json_array_length(json_extract(option, '$.excludeTableLikePatterns')), 0) as excludeTableLikePatternsCount, " +
        "ifnull(json_extract(option, '$.ignoreAutoIncrement'), 0) as ignoreAutoIncrement, " +
        "ifnull(json_extract(option, '$.ignoreComment'), 0) as ignoreComment, " +
        "ifnull(json_extract(option, '$.ignorePartitions'), 0) as ignorePartitions, " +
        "ifnull(json_extract(option, '$.ignoreDefaultForSequence'), 0) as ignoreDefaultForSequence, " +
        "ifnull(json_array_length(conditions), 0) as conditionsCount, " +
        "ifnull(json_array_length(relations), 0) as relationsCount " +
        "FROM diffConfig " +
        "ORDER BY diffConfigId DESC";
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
    MdTemplate template = createTemplate("html/diffConfig/edit.vm");

    // param
    String tmpDiffConfigId = request.getQueryParam("diffConfigId");

    // select
    MdHtmlSaveMode saveMode = MdHtmlSaveMode.UPDATE;
    int diffConfigId = 0;
    MdDbRecord record = null;
    if (!MdUtils.isNullOrEmpty(tmpDiffConfigId)) {
      diffConfigId = Integer.parseInt(tmpDiffConfigId);

      String sql = String.format("SELECT * FROM diffConfig WHERE diffConfigId = %s", diffConfigId);
      record = con.getRecord(sql);
    }
    if (record == null) {
      record = MdDbRecord.createEmptyRecord();
      saveMode = MdHtmlSaveMode.INSERT;
    }
    template.assign("record", record);
    template.assign("saveMode", saveMode);

    // option
    MdInputMemberOption option = new MdInputMemberOption();
    if (!MdUtils.isNullOrEmpty(record.getOrEmpty("option"))) {
      option = MdJson.toObject(record.get("option"), MdInputMemberOption.class);
    }
    while (option.includeTableLikePatterns.size() < 5) {
      option.includeTableLikePatterns.add(option.includeTableLikePatterns.size(), "");
    }
    for (int i = 0; i < 5; i++) {
      try {
        option.includeTableLikePatterns.get(i);
      } catch (IndexOutOfBoundsException e) {
        option.includeTableLikePatterns.add(i, "");
      }
      try {
        option.excludeTableLikePatterns.get(i);
      } catch (IndexOutOfBoundsException e) {
        option.excludeTableLikePatterns.add(i, "");
      }
    }
    template.assign("option", option);

    int idx = 0;

    // conditions
    List<MdInputMemberCondition> conditions = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      conditions.add(i, new MdInputMemberCondition());
    }
    idx = 0;
    if (!MdUtils.isNullOrEmpty(record.getOrEmpty("conditions"))) {
      for (MdInputMemberCondition c : MdJson.toObject(record.get("conditions"), MdInputMemberCondition[].class)) {
        if (c != null) {
          conditions.remove(idx);
          conditions.add(idx, c);
          idx++;
        }
      }
    }
    template.assign("conditions", conditions);

    // relations
    List<MdInputMemberRelation> relations = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      relations.add(i, new MdInputMemberRelation());
    }
    idx = 0;
    if (!MdUtils.isNullOrEmpty(record.getOrEmpty("relations"))) {
      for (MdInputMemberRelation c : MdJson.toObject(record.getOrEmpty("relations"), MdInputMemberRelation[].class)) {
        if (c != null) {
          relations.remove(idx);
          relations.add(idx, c);
          idx++;
        }
      }
    }
    template.assign("relations", relations);

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

    int idx = 0;

    // option
    MdInputMemberOption option = new MdInputMemberOption();
    for (String p : request.getBodyParams("includeTableLikePatterns[]")) {
      if (MdUtils.isNullOrEmpty(p)) {
        continue;
      }
      option.includeTableLikePatterns.add(p);
    }
    for (String p : request.getBodyParams("excludeTableLikePatterns[]")) {
      if (MdUtils.isNullOrEmpty(p)) {
        continue;
      }
      option.excludeTableLikePatterns.add(p);
    }
    option.ignoreAutoIncrement = Boolean.parseBoolean(request.getBodyParam("ignoreAutoIncrement"));
    option.ignoreComment = Boolean.parseBoolean(request.getBodyParam("ignoreComment"));
    option.ignorePartitions = Boolean.parseBoolean(request.getBodyParam("ignorePartitions"));
    option.ignoreDefaultForSequence = Boolean.parseBoolean(request.getBodyParam("ignoreDefaultForSequence"));
    String optionJson = MdJson.toJsonString(option);

    // conditions
    List<MdInputMemberCondition> conditions = new ArrayList<>();
    idx = 0;
    for (int i = 0; i < 5; i++) {
      if (MdUtils.isNullOrEmpty(request.getBodyParam(String.format("conditions_tableName%s", i)))) {
        continue;
      }
      String tableName = request.getBodyParam(String.format("conditions_tableName%s", i));
      if (MdUtils.isNullOrEmpty(request.getBodyParam(String.format("conditions_expression%s", i)))) {
        continue;
      }
      String expression = request.getBodyParam(String.format("conditions_expression%s", i));

      conditions.add(new MdInputMemberCondition(
          tableName,
          expression
      ));
    }
    String conditionsJson = MdJson.toJsonString(conditions.toArray());

    // conditions
    List<MdInputMemberRelation> relations = new ArrayList<>();
    idx = 0;
    for (int i = 0; i < 5; i++) {
      if (MdUtils.isNullOrEmpty(request.getBodyParam(String.format("relations_from%s", i)))) {
        continue;
      }
      String from = request.getBodyParam(String.format("relations_from%s", i));
      if (MdUtils.isNullOrEmpty(request.getBodyParam(String.format("relations_to%s", i)))) {
        continue;
      }
      String to = request.getBodyParam(String.format("relations_to%s", i));

      relations.add(new MdInputMemberRelation(
          from,
          to
      ));
    }
    String relationsJson = MdJson.toJsonString(relations.toArray());

    int diffConfigId = 0;
    String sql = "";
    if (saveMode.equals(MdHtmlSaveMode.INSERT.getValue())) {
      // insert
      sql = String.format(
          "INSERT INTO diffConfig " +
              "(title, explanation, option, conditions, relations) " +
              "VALUES " +
              "('%s', '%s', '%s', '%s', '%s')",
          MdSqliteUtils.quote(title),
          MdSqliteUtils.quote(explanation),
          MdSqliteUtils.quote(optionJson),
          MdSqliteUtils.quote(conditionsJson),
          MdSqliteUtils.quote(relationsJson));
      con.execute(sql);

      // id
      sql = "SELECT last_insert_rowid()";
      MdDbRecord record = con.getRecord(sql);
      diffConfigId = Integer.parseInt(record.get("last_insert_rowid()"));

    } else if (saveMode.equals(MdHtmlSaveMode.UPDATE.getValue())) {
      // update
      String tmpSyncConfigId = request.getBodyParam("diffConfigId");
      diffConfigId = Integer.parseInt(tmpSyncConfigId);

      sql = String.format(
          "UPDATE diffConfig " +
              "SET " +
              "title = '%s', " +
              "explanation = '%s', " +
              "option = '%s', " +
              "conditions = '%s', " +
              "relations = '%s' " +
              "WHERE diffConfigId = %s",
          MdSqliteUtils.quote(title),
          MdSqliteUtils.quote(explanation),
          MdSqliteUtils.quote(optionJson),
          MdSqliteUtils.quote(conditionsJson),
          MdSqliteUtils.quote(relationsJson),
          diffConfigId);
      con.execute(sql);
    }

    // ok
    sendOk(response, String.format("/diffConfig/edit/?diffConfigId=%s", diffConfigId));
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
    String tmpDiffConfigId = request.getQueryParam("diffConfigId");
    if (MdUtils.isNullOrEmpty(tmpDiffConfigId)) {
      sendOther(response, MdHtmlStatus.BAD_REQUEST);
      return;
    }
    int diffConfigId = Integer.parseInt(tmpDiffConfigId);

    String sql = "";

    // check
    sql = String.format("SELECT presetId FROM preset WHERE diffConfigId = %s", diffConfigId);
    MdDbRecord record = con.getRecord(sql);
    if (record != null) {
      sendOther(response, MdHtmlStatus.CONFLICT);
      return;
    }

    // delete
    sql = String.format("DELETE FROM diffConfig WHERE diffConfigId = %s", diffConfigId);
    con.execute(sql);

    // ok
    sendOk(response, String.format("/diffConfig/list/"));
  }
}
