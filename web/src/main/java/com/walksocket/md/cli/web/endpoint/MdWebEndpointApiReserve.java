package com.walksocket.md.cli.web.endpoint;

import com.sun.net.httpserver.HttpExchange;
import com.walksocket.md.cli.*;
import com.walksocket.md.cli.exception.MdExceptionAbstract;
import com.walksocket.md.cli.input.MdInputAbstract;
import com.walksocket.md.cli.input.MdInputDiff;
import com.walksocket.md.cli.input.MdInputMaintenance;
import com.walksocket.md.cli.input.MdInputSync;
import com.walksocket.md.cli.sqlite.MdSqliteConnection;
import com.walksocket.md.cli.sqlite.MdSqliteUtils;
import com.walksocket.md.cli.web.MdWebState;
import com.walksocket.md.cli.web.MdWebStatus;

import java.io.IOException;

/**
 * web endpoint api reserve.
 */
public class MdWebEndpointApiReserve extends MdWebEndpointAbstract {

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    MdLogger.trace(exchange.getRequestURI());

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
      sendOther(exchange, MdWebStatus.BAD_REQUEST);
      return;
    }

    // check
    try {
      input.validate();
    } catch (MdExceptionAbstract me) {
      // bad request
      sendOther(exchange, MdWebStatus.BAD_REQUEST);
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
          MdSqliteUtils.quote(MdWebState.RESERVED.getState()),
          MdSqliteUtils.quote(MdJson.toJsonString(input)),
          MdDate.timestamp());
      con.execute(sql);

      //commit
      con.commit();

    } catch (Exception e) {
      MdLogger.error(e);

      // error
      sendOther(exchange, MdWebStatus.INTERNAL_SERVER_ERROR);
      return;
    }

    // accepted
    sendReserved(exchange, executionId);
    return;
  }
}
