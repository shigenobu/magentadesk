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

      // pre action
      preAction(request, response, con);

      // action
      action(request, response, con);

      // post action
      postAction(request, response, con);

      // commit
      con.commit();

    } catch (Exception e) {
      MdLogger.error(e);

      // error
      error(request, response);
    }

    // last
    last(request, response);
  }

  /**
   * preAction.
   * @param request request
   * @param response response
   * @param con sqlite connection
   * @throws Exception error
   */
  public void preAction(MdServerRequest request, MdServerResponse response, MdSqliteConnection con) {
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
   * postAction.
   * @param request request
   * @param response response
   * @param con sqlite connection
   * @throws Exception error
   */
  public void postAction(MdServerRequest request, MdServerResponse response, MdSqliteConnection con) {
  }

  /**
   * error.
   * @param request request
   * @param response response
   */
  abstract public void error(MdServerRequest request, MdServerResponse response);

  /**
   * last.
   * @param request request
   * @param response response
   */
  abstract public void last(MdServerRequest request, MdServerResponse response);
}
