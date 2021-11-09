package com.walksocket.md.html.endpoint;

import com.sun.net.httpserver.HttpExchange;
import com.walksocket.md.MdLogger;

import java.io.IOException;

public class MdHtmlEndpointPreset extends MdHtmlEndpointAbstract {
  @Override
  public void handle(HttpExchange exchange) throws IOException {
    MdLogger.trace(getClass().getSimpleName() + ":" + exchange.getRequestURI());

    String path = exchange.getRequestURI().getPath();
    if (path.equals("/preset/list/")) {
      list(exchange);
      return;
    } else if (path.equals("/preset/edit/")) {
      edit(exchange);
      return;
    } else if (path.equals("/preset/save/")) {

    } else if (path.equals("/preset/remove/")) {

    }
    renderNotFound(exchange);
  }

  private void list(HttpExchange exchange) throws IOException {
    renderWithLayout(exchange, "html/preset/list.vm");
  }

  private void edit(HttpExchange exchange) throws IOException {
    renderWithLayout(exchange, "html/preset/edit.vm");
  }
}
