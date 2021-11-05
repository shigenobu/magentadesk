package com.walksocket.md.web.endpoint;

import com.sun.net.httpserver.HttpExchange;
import com.walksocket.md.MdJson;
import com.walksocket.md.MdLogger;
import com.walksocket.md.MdMode;
import com.walksocket.md.db.MdDbRecord;
import com.walksocket.md.output.MdOutputDiff;
import com.walksocket.md.output.MdOutputMaintenance;
import com.walksocket.md.output.MdOutputSync;
import com.walksocket.md.sqlite.MdSqliteConnection;
import com.walksocket.md.sqlite.MdSqliteUtils;
import com.walksocket.md.web.MdWebState;
import com.walksocket.md.web.MdWebStatus;

import java.io.IOException;
import java.util.List;

/**
 * web endpoint api check.
 */
public class MdWebEndpointApiCheck extends MdWebEndpointAbstract {

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    MdLogger.trace(exchange.getRequestURI());

    // method check
    if (!isPost(exchange)) {
      // method not allowed
      sendOther(exchange, MdWebStatus.METHOD_NOT_ALLOWED);
      return;
    }

    // get mode
    MdMode mdMode = getMode(exchange);

    // get execution id
    List<String> executionIds = exchange.getRequestHeaders().get(HEADER_EXECUTION_ID);
    if (executionIds == null || executionIds.isEmpty() || executionIds.get(0).equals("")) {
      // bad request
      sendOther(exchange, MdWebStatus.BAD_REQUEST);
      return;
    }
    String executionId = executionIds.get(0);

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
        sendOther(exchange, MdWebStatus.CONFLICT);
        return;
      }
      String mode = records.get(0).get("mode");
      String state = records.get(0).get("state");
      String output = records.get(0).get("output");
      if (!mode.equals(mdMode.getMode())) {
        // conflict
        sendOther(exchange, MdWebStatus.CONFLICT);
        return;
      }

      if (state.equals(MdWebState.RESERVED.getState()) || state.equals(MdWebState.PROCESSING.getState())) {
        // no content
        sendProcessing(exchange, executionId);
        return;
      } else if (state.equals(MdWebState.COMPLETE.getState()) && output != null) {
        // ok
        if (mdMode == MdMode.DIFF) {
          sendComplete(exchange, MdJson.toObject(output, MdOutputDiff.class));
        } else if (mdMode == MdMode.SYNC) {
          sendComplete(exchange, MdJson.toObject(output, MdOutputSync.class));
        } else if (mdMode == MdMode.MAINTENANCE) {
          sendComplete(exchange, MdJson.toObject(output, MdOutputMaintenance.class));
        }
        return;
      }

      //commit
      con.commit();

    } catch (Exception e) {
      MdLogger.error(e);
    }

    // error
    sendOther(exchange, MdWebStatus.INTERNAL_SERVER_ERROR);
    return;
  }
}
