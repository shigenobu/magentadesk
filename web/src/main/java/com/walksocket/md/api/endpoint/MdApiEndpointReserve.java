package com.walksocket.md.api.endpoint;

import com.walksocket.md.MdJson;
import com.walksocket.md.MdLogger;
import com.walksocket.md.MdMode;
import com.walksocket.md.api.MdApiStatus;
import com.walksocket.md.exception.MdExceptionAbstract;
import com.walksocket.md.input.MdInputAbstract;
import com.walksocket.md.input.MdInputDiff;
import com.walksocket.md.input.MdInputMaintenance;
import com.walksocket.md.input.MdInputSync;
import com.walksocket.md.server.MdServerRequest;
import com.walksocket.md.server.MdServerResponse;
import com.walksocket.md.sqlite.MdSqliteConnection;

/**
 * api endpoint reserve.
 */
public class MdApiEndpointReserve extends MdApiEndpointAbstract {

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

    // make input
    String inputJson = request.getRawBody();
    MdInputAbstract input = null;
    try {
      if (mdMode == MdMode.DIFF) {
        input = MdJson.toObject(inputJson, MdInputDiff.class);
      } else if (mdMode == MdMode.SYNC) {
        input = MdJson.toObject(inputJson, MdInputSync.class);
      } else if (mdMode == MdMode.MAINTENANCE) {
        input = MdJson.toObject(inputJson, MdInputMaintenance.class);
      }
    } catch (Exception e) {
      MdLogger.error(e);
    }
    if (input == null) {
      // bad request
      sendOther(response, MdApiStatus.BAD_REQUEST);
      return;
    }

    // check
    try {
      input.validate();
    } catch (MdExceptionAbstract me) {
      // bad request
      sendOther(response, MdApiStatus.BAD_REQUEST);
      return;
    }

    // reserve execution
    String executionId = reserveExecution(con, mdMode, input);

    // accepted
    response.addHeader(HEADER_EXECUTION_ID, executionId);
    sendReserved(response);
    return;
  }
}
