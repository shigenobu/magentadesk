package com.walksocket.md.api.endpoint;

import com.sun.net.httpserver.HttpExchange;
import com.walksocket.md.*;
import com.walksocket.md.exception.MdExceptionAbstract;
import com.walksocket.md.input.MdInputAbstract;
import com.walksocket.md.input.MdInputDiff;
import com.walksocket.md.input.MdInputMaintenance;
import com.walksocket.md.input.MdInputSync;
import com.walksocket.md.server.MdServerRequest;
import com.walksocket.md.server.MdServerResponse;
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
      sendOther(MdApiStatus.BAD_REQUEST);
      return;
    }

    // check
    try {
      input.validate();
    } catch (MdExceptionAbstract me) {
      // bad request
      sendOther(MdApiStatus.BAD_REQUEST);
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
      sendOther(MdApiStatus.INTERNAL_SERVER_ERROR);
      return;
    }

    // accepted
    sendReserved(executionId);
    return;
  }
}
