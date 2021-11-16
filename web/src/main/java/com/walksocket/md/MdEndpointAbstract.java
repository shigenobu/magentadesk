package com.walksocket.md;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.walksocket.md.db.MdDbRecord;
import com.walksocket.md.input.MdInputAbstract;
import com.walksocket.md.server.MdServerRequest;
import com.walksocket.md.server.MdServerResponse;
import com.walksocket.md.sqlite.MdSqliteConnection;
import com.walksocket.md.sqlite.MdSqliteUtils;

import java.io.IOException;
import java.sql.SQLException;

/**
 * endpoint abstract.
 */
abstract public class MdEndpointAbstract implements HttpHandler {

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    MdServerRequest request = new MdServerRequest(exchange);
    MdServerResponse response = new MdServerResponse(exchange);
    MdLogger.trace(getClass().getSimpleName() + ":" + request.getPath());

    try (MdSqliteConnection con = new MdSqliteConnection()) {
      // begin
      con.begin();

      // action
      action(request, response, con);

      if (response.getStatus() >= 200 && response.getStatus() < 400) {
        // commit
        con.commit();
      }

    } catch (Exception e) {
      MdLogger.error(e);

      // error
      error(request, response);
    }

    // send response
    response.send();
  }

  /**
   * action.
   * @param request request
   * @param response response
   * @param con sqlite connection
   * @throws Exception error
   */
  abstract public void action(MdServerRequest request, MdServerResponse response, MdSqliteConnection con) throws Exception;

  /**
   * error.
   * @param request request
   * @param response response
   */
  abstract public void error(MdServerRequest request, MdServerResponse response);

  /**
   * get mode
   * @return mode
   */
  protected MdMode getMode(MdServerRequest request) {
    String path = request.getPath();
    if (path.contains("/" + MdMode.DIFF.getMode() + "/")) {
      return MdMode.DIFF;
    } else if (path.contains("/" + MdMode.SYNC.getMode() + "/")) {
      return MdMode.SYNC;
    } else if (path.contains("/" + MdMode.MAINTENANCE.getMode() + "/")) {
      return MdMode.MAINTENANCE;
    }
    return null;
  }

  /**
   * reserve execution.
   * @param con sqlite connection
   * @param mdMode mode
   * @param input input
   * @return execution id
   * @throws SQLException sql error
   */
  protected String reserveExecution(MdSqliteConnection con, MdMode mdMode, MdInputAbstract input) throws SQLException {
    // executionId
    String executionId = MdUtils.randomString();

    // insert
    String sql = String.format(
        "INSERT INTO execution " +
            "(executionId, mode, state, input, output, created) " +
            "VALUES " +
            "('%s', '%s', '%s', '%s', null, %s)",
        MdSqliteUtils.quote(executionId),
        MdSqliteUtils.quote(mdMode.getMode()),
        MdSqliteUtils.quote(MdState.RESERVED.getState()),
        MdSqliteUtils.quote(MdJson.toJsonString(input)),
        MdDate.timestamp());
    con.execute(sql);

    return executionId;
  }

  /**
   * check execution.
   * @param con sqlite connection
   * @param executionId executionId
   * @return record or null
   * @throws SQLException sqlerror
   */
  protected MdDbRecord checkExecution(MdSqliteConnection con, String executionId) throws SQLException {
    String sql = String.format(
        "SELECT mode, state, input, output " +
            "FROM execution " +
            "WHERE executionId = '%s'",
        MdSqliteUtils.quote(executionId));
    return con.getRecord(sql);
  }
}
