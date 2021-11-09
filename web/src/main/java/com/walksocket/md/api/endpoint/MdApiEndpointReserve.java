package com.walksocket.md.api.endpoint;

import com.sun.net.httpserver.HttpExchange;
import com.walksocket.md.*;
import com.walksocket.md.exception.MdExceptionAbstract;
import com.walksocket.md.input.MdInputAbstract;
import com.walksocket.md.input.MdInputDiff;
import com.walksocket.md.input.MdInputMaintenance;
import com.walksocket.md.input.MdInputSync;
import com.walksocket.md.sqlite.MdSqliteConnection;
import com.walksocket.md.sqlite.MdSqliteUtils;
import com.walksocket.md.api.MdApiState;
import com.walksocket.md.api.MdApiStatus;

import java.io.IOException;

/**
 * api endpoint reserve.
 */
public class MdApiEndpointReserve extends MdApiEndpointAbstract {

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    MdLogger.trace(getClass().getSimpleName() + ":" + exchange.getRequestURI());

    // method check
    if (!isPost(exchange)) {
      // method not allowed
      sendOther(exchange, MdApiStatus.METHOD_NOT_ALLOWED);
      return;
    }

    // get mode
    MdMode mdMode = getMode(exchange);

    // make input
    String inputJson = MdFile.readString(exchange.getRequestBody());
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
      sendOther(exchange, MdApiStatus.BAD_REQUEST);
      return;
    }

    // check
    try {
      input.validate();
    } catch (MdExceptionAbstract me) {
      // bad request
      sendOther(exchange, MdApiStatus.BAD_REQUEST);
      return;
    }

    // executionId
    String executionId = MdUtils.randomString();

    // db
    try (MdSqliteConnection con = new MdSqliteConnection()) {
      // begin
      con.begin();

      // insert
      String sql = String.format(
          "INSERT INTO execution " +
              "(executionId, mode, state, input, output, created) " +
              "VALUES " +
              "('%s', '%s', '%s', '%s', null, %s)",
          MdSqliteUtils.quote(executionId),
          MdSqliteUtils.quote(mdMode.getMode()),
          MdSqliteUtils.quote(MdApiState.RESERVED.getState()),
          MdSqliteUtils.quote(MdJson.toJsonString(input)),
          MdDate.timestamp());
      con.execute(sql);

      //commit
      con.commit();

    } catch (Exception e) {
      MdLogger.error(e);

      // error
      sendOther(exchange, MdApiStatus.INTERNAL_SERVER_ERROR);
      return;
    }

    // accepted
    sendReserved(exchange, executionId);
    return;
  }
}
