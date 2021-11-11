package com.walksocket.md.api.endpoint;

import com.walksocket.md.MdJson;
import com.walksocket.md.MdMode;
import com.walksocket.md.MdUtils;
import com.walksocket.md.api.MdApiState;
import com.walksocket.md.api.MdApiStatus;
import com.walksocket.md.db.MdDbRecord;
import com.walksocket.md.output.MdOutputDiff;
import com.walksocket.md.output.MdOutputMaintenance;
import com.walksocket.md.output.MdOutputSync;
import com.walksocket.md.server.MdServerRequest;
import com.walksocket.md.server.MdServerResponse;
import com.walksocket.md.sqlite.MdSqliteConnection;
import com.walksocket.md.sqlite.MdSqliteUtils;

import java.util.List;

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

    // select
    String sql = String.format(
        "SELECT mode, state, output " +
            "FROM execution " +
            "WHERE executionId = '%s'",
        MdSqliteUtils.quote(executionId));
    List<MdDbRecord> records = con.getRecords(sql);
    if (records.isEmpty()) {
      // conflict
      sendOther(response, MdApiStatus.CONFLICT);
      return;
    }
    String mode = records.get(0).get("mode");
    String state = records.get(0).get("state");
    String output = records.get(0).get("output");
    if (!mode.equals(mdMode.getMode())) {
      // conflict
      sendOther(response, MdApiStatus.CONFLICT);
      return;
    }

    if (state.equals(MdApiState.RESERVED.getState()) || state.equals(MdApiState.PROCESSING.getState())) {
      // no content
      response.addHeader(HEADER_EXECUTION_ID, executionId);
      sendProcessing(response);
      return;
    } else if (state.equals(MdApiState.COMPLETE.getState()) && output != null) {
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
