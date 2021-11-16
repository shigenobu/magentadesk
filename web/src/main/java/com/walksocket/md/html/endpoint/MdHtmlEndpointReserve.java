package com.walksocket.md.html.endpoint;

import com.walksocket.md.MdLogger;
import com.walksocket.md.MdMode;
import com.walksocket.md.MdTemplate;
import com.walksocket.md.MdUtils;
import com.walksocket.md.db.MdDbRecord;
import com.walksocket.md.exception.MdExceptionAbstract;
import com.walksocket.md.html.MdHtmlDesk;
import com.walksocket.md.html.MdHtmlStatus;
import com.walksocket.md.input.MdInputAbstract;
import com.walksocket.md.input.MdInputDiff;
import com.walksocket.md.input.MdInputMaintenance;
import com.walksocket.md.input.MdInputSync;
import com.walksocket.md.server.MdServerRequest;
import com.walksocket.md.server.MdServerResponse;
import com.walksocket.md.sqlite.MdSqliteConnection;

import java.util.stream.Collectors;

/**
 * html endpoint reserve.
 */
public class MdHtmlEndpointReserve extends MdHtmlEndpointAbstract {

  @Override
  public void action(MdServerRequest request, MdServerResponse response, MdSqliteConnection con) throws Exception {
    // template
    MdTemplate template = createTemplate("html/reserve/index.vm");

    // method check
    if (!request.isPost()) {
      // method not allowed
      renderOtherWithLayout(response, MdHtmlStatus.METHOD_NOT_ALLOWED);
      return;
    }

    // get mode
    MdMode mdMode = getMode(request);
    if (mdMode == null) {
      // not found
      renderOtherWithLayout(response, MdHtmlStatus.NOT_FOUND);
      return;
    }
    template.assign("mdMode", mdMode);

    // param
    String tmpProjectId = request.getBodyParam("projectId");
    if (MdUtils.isNullOrEmpty(tmpProjectId)) {
      renderOtherWithLayout(response, MdHtmlStatus.BAD_REQUEST);
      return;
    }
    int projectId = Integer.parseInt(tmpProjectId);
    template.assign("projectId", projectId);

    int presetId = 0;
    if (mdMode == MdMode.DIFF || mdMode == MdMode.SYNC) {
      String tmpPresetId = request.getBodyParam("presetId");
      if (MdUtils.isNullOrEmpty(tmpPresetId)) {
        renderOtherWithLayout(response, MdHtmlStatus.BAD_REQUEST);
        return;
      }
      presetId = Integer.parseInt(tmpPresetId);
    }
    template.assign("presetId", presetId);

    // make input
    MdInputAbstract input = null;
    MdHtmlDesk desk = new MdHtmlDesk(con);
    try {
      // projectRecord
      MdDbRecord projectRecord = desk.getProjectRecord(projectId);
      template.assign("projectRecord", projectRecord);

      if (mdMode == MdMode.DIFF) {
        MdInputDiff inputDiff = new MdInputDiff();
        desk.makeDiffInput(projectId, presetId, inputDiff);

        input = inputDiff;
      } else if (mdMode == MdMode.SYNC) {
        MdInputSync inputSync = new MdInputSync();
        desk.makeSyncInput(projectId, presetId, inputSync);

        // add param
        inputSync.summaryId = request.getBodyParam("summaryId");
        inputSync.diffSeqs = request.getBodyParams("diffSeqs[]").stream().map(s -> Long.parseLong(s)).collect(Collectors.toList());
        inputSync.run = Boolean.parseBoolean(request.getBodyParam("run"));
        inputSync.force = Boolean.parseBoolean(request.getBodyParam("force"));
        inputSync.loose = Boolean.parseBoolean(request.getBodyParam("loose"));

        input = inputSync;
      } else if (mdMode == MdMode.MAINTENANCE) {
        MdInputMaintenance inputMaintenance = new MdInputMaintenance();
        desk.makeMaintenanceInput(projectId, inputMaintenance);

        // add param
        inputMaintenance.maintenance = request.getBodyParam("maintenance");

        input = inputMaintenance;
      }
    } catch (Exception e) {
      MdLogger.error(e);
    }
    if (input == null) {
      // bad request
      renderOtherWithLayout(response, MdHtmlStatus.BAD_REQUEST);
      return;
    }

    // check
    try {
      input.validate();
    } catch (MdExceptionAbstract me) {
      // bad request
      renderOtherWithLayout(response, MdHtmlStatus.BAD_REQUEST);
      return;
    }
    template.assign("input", input);

    // reserve execution
    String executionId = reserveExecution(con, mdMode, input);
    template.assign("executionId", executionId);

    // render
    renderOkWithLayout(response, template);
  }
}
