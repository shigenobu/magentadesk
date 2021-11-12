package com.walksocket.md.html.endpoint;

import com.walksocket.md.MdJson;
import com.walksocket.md.MdUtils;
import com.walksocket.md.html.MdHtmlDesk;
import com.walksocket.md.html.MdHtmlStatus;
import com.walksocket.md.input.MdInputDiff;
import com.walksocket.md.input.MdInputMaintenance;
import com.walksocket.md.input.MdInputSync;
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
    String path = request.getPath();
    MdHtmlDesk desk = new MdHtmlDesk(con);
    if (path.equals("/json/maintenance/")) {
      maintenance(request, response, desk);
      return;
    } else if (path.equals("/json/diff/")) {
      diff(request, response, desk);
      return;
    } else if (path.equals("/json/sync/")) {
      sync(request, response, desk);
      return;
    }
    sendOther(response, MdHtmlStatus.NOT_FOUND);
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
    String json = MdJson.toJsonStringFriendly(input);

    // send
    sendOkJson(response, json);
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
    MdInputDiff input = new MdInputDiff();
    desk.makeDiffInput(projectId, presetId, input);
    String json = MdJson.toJsonStringFriendly(input);

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
    String json = MdJson.toJsonStringFriendly(input);

    // send
    sendOkJson(response, json);
  }
}
