package com.walksocket.md.html.endpoint;

import com.walksocket.md.*;
import com.walksocket.md.db.MdDbRecord;
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

/**
 * html endpoint check.
 */
public class MdHtmlEndpointCheck extends MdHtmlEndpointAbstract {

  @Override
  public void action(MdServerRequest request, MdServerResponse response, MdSqliteConnection con) throws Exception {
    // method check
    if (!request.isPut()) {
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

    String executionId = request.getQueryParam("executionId");
    if (MdUtils.isNullOrEmpty(executionId)) {
      sendOther(response, MdHtmlStatus.BAD_REQUEST);
      return;
    }

    // get execution
    MdDbRecord record = checkExecution(con, executionId);
    if (record == null) {
      // conflict
      sendOther(response, MdHtmlStatus.CONFLICT);
      return;
    }
    String mode = record.get("mode");
    String state = record.get("state");
    String input = record.get("input");
    String output = record.get("output");
    if (!mode.equals(mdMode.getMode())) {
      // conflict
      sendOther(response, MdHtmlStatus.CONFLICT);
      return;
    }

    if (state.equals(MdState.RESERVED.getState()) || state.equals(MdState.PROCESSING.getState())) {
      // no content
      MdTemplate template = createTemplate("html/reserve/wait.vm");
      renderOk(response, template);
      return;
    } else if (state.equals(MdState.COMPLETE.getState()) && output != null) {
      // ok
      MdTemplate template = createTemplate(String.format("html/check/%s.vm", mdMode.getMode()));
      template.assign("projectId", projectId);
      template.assign("presetId", presetId);
      template.assign("executionId", executionId);

      if (mdMode == MdMode.DIFF) {
        template.assign("input", MdJson.toObject(input, MdInputDiff.class));
        template.assign("output", MdJson.toObject(output, MdOutputDiff.class));
      } else if (mdMode == MdMode.SYNC) {
        template.assign("input", MdJson.toObject(input, MdInputSync.class));
        template.assign("output", MdJson.toObject(output, MdOutputSync.class));
      } else if (mdMode == MdMode.MAINTENANCE) {
        template.assign("input", MdJson.toObject(input, MdInputMaintenance.class));
        template.assign("output", MdJson.toObject(output, MdOutputMaintenance.class));
      }
      renderOk(response, template);
      return;
    }
  }
}
