package com.walksocket.md.html.endpoint;

import com.walksocket.md.MdJson;
import com.walksocket.md.MdMode;
import com.walksocket.md.MdUtils;
import com.walksocket.md.db.MdDbRecord;
import com.walksocket.md.html.MdHtmlDesk;
import com.walksocket.md.html.MdHtmlStatus;
import com.walksocket.md.input.MdInputDiff;
import com.walksocket.md.input.MdInputMaintenance;
import com.walksocket.md.input.MdInputSync;
import com.walksocket.md.output.MdOutputDiff;
import com.walksocket.md.output.MdOutputMaintenance;
import com.walksocket.md.output.MdOutputSync;
import com.walksocket.md.server.MdServerRequest;
import com.walksocket.md.server.MdServerResponse;
import com.walksocket.md.sqlite.MdSqliteConnection;

import java.io.IOException;
import java.sql.SQLException;

/**
 * html endpoint json.
 */
public class MdHtmlEndpointJson extends MdHtmlEndpointAbstract {

  @Override
  public void action(MdServerRequest request, MdServerResponse response, MdSqliteConnection con) throws Exception {
    // method check
    if (!request.isGet()) {
      // method not allowed
      sendOther(response, MdHtmlStatus.METHOD_NOT_ALLOWED);
      return;
    }

    // get mode
    MdMode mdMode = getMode(request);
    if (mdMode == null) {
      // not found
      sendOther(response, MdHtmlStatus.NOT_FOUND);
      return;
    }

    MdHtmlDesk desk = new MdHtmlDesk(con);
    if (mdMode == MdMode.DIFF) {
      diff(request, response, desk);
      return;
    } else if (mdMode == MdMode.SYNC) {
      sync(request, response, desk);
      return;
    } else if (mdMode == MdMode.MAINTENANCE) {
      maintenance(request, response, desk);
      return;
    }
    sendOther(response, MdHtmlStatus.NOT_FOUND);
  }

  /**
   * is mostly.
   * @param request request
   * @return if mostly, true
   */
  private boolean isMostly(MdServerRequest request) {
    return request.getPath().contains("/mostly/");
  }

  /**
   * is really inpu.
   * @param request request
   * @return if really input, true
   */
  private boolean isReallyInput(MdServerRequest request) {
    return request.getPath().contains("/input/");
  }

  /**
   * diff.
   * @param request request
   * @param response response
   * @param desk desk
   * @throws IOException IO error
   * @throws SQLException sql error
   */
  private void diff(MdServerRequest request, MdServerResponse response, MdHtmlDesk desk) throws IOException, SQLException {
    // json
    String json = "";
    if (isMostly(request)) {
      // param
      String tmpProjectId = request.getQueryParam("projectId");
      if (MdUtils.isNullOrEmpty(tmpProjectId)) {
        sendOther(response, MdHtmlStatus.BAD_REQUEST);
        return;
      }
      int projectId = Integer.parseInt(tmpProjectId);

      String tmpPresetId = request.getQueryParam("presetId");
      if (MdUtils.isNullOrEmpty(tmpPresetId)) {
        sendOther(response, MdHtmlStatus.BAD_REQUEST);
        return;
      }
      int presetId = Integer.parseInt(tmpPresetId);

      MdInputDiff input = new MdInputDiff();
      desk.makeDiffInput(projectId, presetId, input);
      json = MdJson.toJsonStringFriendly(input);
    } else {
      // param
      String executionId = request.getQueryParam("executionId");
      if (MdUtils.isNullOrEmpty(executionId)) {
        sendOther(response, MdHtmlStatus.BAD_REQUEST);
        return;
      }

      // input or output
      MdDbRecord record = checkExecution(desk.getConnection(), executionId);
      if (record == null || !record.get("mode").equals(MdMode.DIFF.getMode())) {
        sendOther(response, MdHtmlStatus.CONFLICT);
        return;
      }
      if (isReallyInput(request)) {
        json = MdJson.toJsonStringFriendly(MdJson.toObject(record.get("input"), MdInputDiff.class));
      } else {
        json = MdJson.toJsonStringFriendly(MdJson.toObject(record.get("output"), MdOutputDiff.class));
      }
    }

    // send
    sendOkJson(response, json);
  }

  /**
   * sync.
   * @param request request
   * @param response response
   * @param desk desk
   * @throws IOException IO error
   * @throws SQLException sql error
   */
  private void sync(MdServerRequest request, MdServerResponse response, MdHtmlDesk desk) throws SQLException, IOException {
    // json
    String json = "";
    if (isMostly(request)) {
      // param
      String tmpProjectId = request.getQueryParam("projectId");
      if (MdUtils.isNullOrEmpty(tmpProjectId)) {
        sendOther(response, MdHtmlStatus.BAD_REQUEST);
        return;
      }
      int projectId = Integer.parseInt(tmpProjectId);

      String tmpPresetId = request.getQueryParam("presetId");
      if (MdUtils.isNullOrEmpty(tmpPresetId)) {
        sendOther(response, MdHtmlStatus.BAD_REQUEST);
        return;
      }
      int presetId = Integer.parseInt(tmpPresetId);

      // input
      MdInputSync input = new MdInputSync();
      desk.makeSyncInput(projectId, presetId, input);
      json = MdJson.toJsonStringFriendly(input);
    } else {
      // param
      String executionId = request.getQueryParam("executionId");
      if (MdUtils.isNullOrEmpty(executionId)) {
        sendOther(response, MdHtmlStatus.BAD_REQUEST);
        return;
      }

      // input or output
      MdDbRecord record = checkExecution(desk.getConnection(), executionId);
      if (record == null || !record.get("mode").equals(MdMode.SYNC.getMode())) {
        sendOther(response, MdHtmlStatus.CONFLICT);
        return;
      }
      if (isReallyInput(request)) {
        json = MdJson.toJsonStringFriendly(MdJson.toObject(record.get("input"), MdInputSync.class));
      } else {
        json = MdJson.toJsonStringFriendly(MdJson.toObject(record.get("output"), MdOutputSync.class));
      }
    }

    // send
    sendOkJson(response, json);
  }

  /**
   * maintenance.
   * @param request request
   * @param response response
   * @param desk desk
   * @throws IOException IO error
   * @throws SQLException sql error
   */
  private void maintenance(MdServerRequest request, MdServerResponse response, MdHtmlDesk desk) throws IOException, SQLException {
    // json
    String json = "";
    if (isMostly(request)) {
      // param
      String tmpProjectId = request.getQueryParam("projectId");
      if (MdUtils.isNullOrEmpty(tmpProjectId)) {
        sendOther(response, MdHtmlStatus.BAD_REQUEST);
        return;
      }
      int projectId = Integer.parseInt(tmpProjectId);

      // input
      MdInputMaintenance input = new MdInputMaintenance();
      desk.makeMaintenanceInput(projectId, input);
      json = MdJson.toJsonStringFriendly(input);
    } else {
      // param
      String executionId = request.getQueryParam("executionId");
      if (MdUtils.isNullOrEmpty(executionId)) {
        sendOther(response, MdHtmlStatus.BAD_REQUEST);
        return;
      }

      // input or output
      MdDbRecord record = checkExecution(desk.getConnection(), executionId);
      if (record == null || !record.get("mode").equals(MdMode.MAINTENANCE.getMode())) {
        sendOther(response, MdHtmlStatus.CONFLICT);
        return;
      }
      if (isReallyInput(request)) {
        json = MdJson.toJsonStringFriendly(MdJson.toObject(record.get("input"), MdInputMaintenance.class));
      } else {
        json = MdJson.toJsonStringFriendly(MdJson.toObject(record.get("output"), MdOutputMaintenance.class));
      }
    }

    // send
    sendOkJson(response, json);
  }
}
