package com.walksocket.md.api.endpoint;

import com.walksocket.md.MdJson;
import com.walksocket.md.MdMode;
import com.walksocket.md.MdState;
import com.walksocket.md.MdUtils;
import com.walksocket.md.api.MdApiStatus;
import com.walksocket.md.db.MdDbRecord;
import com.walksocket.md.output.MdOutputDiff;
import com.walksocket.md.output.MdOutputMaintenance;
import com.walksocket.md.output.MdOutputSync;
import com.walksocket.md.server.MdServerRequest;
import com.walksocket.md.server.MdServerResponse;
import com.walksocket.md.sqlite.MdSqliteConnection;

/**
 * api endpoint check.
 */
public class MdApiEndpointCheck extends MdApiEndpointAbstract {

  @Override
  public void action(MdServerRequest request, MdServerResponse response, MdSqliteConnection con) throws Exception {
    // method check
    if (!request.isPost()) {
      // method not allowed
      sendOther(response, MdApiStatus.METHOD_NOT_ALLOWED);
      return;
    }

    // get mode
    MdMode mdMode = getMode(request);
    if (mdMode == null) {
      // not found
      sendOther(response, MdApiStatus.NOT_FOUND);
      return;
    }

    // get execution id
    String executionId = request.getHeader(HEADER_EXECUTION_ID);
    if (MdUtils.isNullOrEmpty(executionId)) {
      // bad request
      sendOther(response, MdApiStatus.BAD_REQUEST);
      return;
    }

    // get execution
    MdDbRecord record = checkExecution(con, executionId);
    if (record == null) {
      // conflict
      sendOther(response, MdApiStatus.CONFLICT);
      return;
    }
    String mode = record.get("mode");
    String state = record.get("state");
    String output = record.get("output");
    if (!mode.equals(mdMode.getMode())) {
      // conflict
      sendOther(response, MdApiStatus.CONFLICT);
      return;
    }

    if (state.equals(MdState.RESERVED.getState()) || state.equals(MdState.PROCESSING.getState())) {
      // no content
      response.addHeader(HEADER_EXECUTION_ID, executionId);
      sendProcessing(response);
      return;
    } else if (state.equals(MdState.COMPLETE.getState()) && output != null) {
      // ok
      if (mdMode == MdMode.DIFF) {
        sendComplete(response, MdJson.toObject(output, MdOutputDiff.class));
      } else if (mdMode == MdMode.SYNC) {
        sendComplete(response, MdJson.toObject(output, MdOutputSync.class));
      } else if (mdMode == MdMode.MAINTENANCE) {
        sendComplete(response, MdJson.toObject(output, MdOutputMaintenance.class));
      }
      return;
    }
  }
}
