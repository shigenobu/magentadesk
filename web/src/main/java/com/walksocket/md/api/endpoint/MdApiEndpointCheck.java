package com.walksocket.md.api.endpoint;

import com.sun.net.httpserver.HttpExchange;
import com.walksocket.md.MdJson;
import com.walksocket.md.MdLogger;
import com.walksocket.md.MdMode;
import com.walksocket.md.MdUtils;
import com.walksocket.md.api.MdApiState;
import com.walksocket.md.api.MdApiStatus;
import com.walksocket.md.db.MdDbRecord;
import com.walksocket.md.output.MdOutputDiff;
import com.walksocket.md.output.MdOutputMaintenance;
import com.walksocket.md.output.MdOutputSync;
import com.walksocket.md.sqlite.MdSqliteConnection;
import com.walksocket.md.sqlite.MdSqliteUtils;

import java.io.IOException;
import java.util.List;

/**
 * api endpoint check.
 */
public class MdApiEndpointCheck extends MdApiEndpointAbstract {

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    init(exchange);
    MdLogger.trace(getClass().getSimpleName() + ":" + request.getPath());

    // method check
    if (!request.isPost()) {
      // method not allowed
      sendOther(MdApiStatus.METHOD_NOT_ALLOWED);
      return;
    }

    // get mode
    MdMode mdMode = getMode();
    if (mdMode == null) {
      // not found
      sendOther(MdApiStatus.NOT_FOUND);
      return;
    }

    // get execution id
    String executionId = request.getHeader(HEADER_EXECUTION_ID);
    if (MdUtils.isNullOrEmpty(executionId)) {
      // bad request
      sendOther(MdApiStatus.BAD_REQUEST);
      return;
    }

    // select
    try (MdSqliteConnection con = new MdSqliteConnection()) {
      // begin
      con.begin();

      // insert
      String sql = String.format(
          "SELECT mode, state, output " +
              "FROM execution " +
              "WHERE executionId = '%s'",
          MdSqliteUtils.quote(executionId));
      List<MdDbRecord> records = con.getRecords(sql);
      if (records.isEmpty()) {
        // conflict
        sendOther(MdApiStatus.CONFLICT);
        return;
      }
      String mode = records.get(0).get("mode");
      String state = records.get(0).get("state");
      String output = records.get(0).get("output");
      if (!mode.equals(mdMode.getMode())) {
        // conflict
        sendOther(MdApiStatus.CONFLICT);
        return;
      }

      if (state.equals(MdApiState.RESERVED.getState()) || state.equals(MdApiState.PROCESSING.getState())) {
        // no content
        sendProcessing(executionId);
        return;
      } else if (state.equals(MdApiState.COMPLETE.getState()) && output != null) {
        // ok
        if (mdMode == MdMode.DIFF) {
          sendComplete(MdJson.toObject(output, MdOutputDiff.class));
        } else if (mdMode == MdMode.SYNC) {
          sendComplete(MdJson.toObject(output, MdOutputSync.class));
        } else if (mdMode == MdMode.MAINTENANCE) {
          sendComplete(MdJson.toObject(output, MdOutputMaintenance.class));
        }
        return;
      }

      //commit
      con.commit();

    } catch (Exception e) {
      MdLogger.error(e);
    }

    // error
    sendOther(MdApiStatus.INTERNAL_SERVER_ERROR);
    return;
  }
}
