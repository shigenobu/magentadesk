package com.walksocket.md;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.walksocket.md.server.MdServerRequest;
import com.walksocket.md.server.MdServerResponse;
import com.walksocket.md.sqlite.MdSqliteConnection;

import java.io.IOException;

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

      // commit
      con.commit();

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
}
