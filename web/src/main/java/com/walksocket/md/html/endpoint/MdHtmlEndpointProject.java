package com.walksocket.md.html.endpoint;

import com.sun.net.httpserver.HttpExchange;
import com.walksocket.md.MdLogger;

import java.io.IOException;

public class MdHtmlEndpointProject extends MdHtmlEndpointAbstract {
  @Override
  public void handle(HttpExchange exchange) throws IOException {
    MdLogger.trace(getClass().getSimpleName() + ":" + exchange.getRequestURI());

    String path = exchange.getRequestURI().getPath();
    if (path.equals("/project/list/")) {
      list(exchange);
      return;
    } else if (path.equals("/project/edit/")) {
      edit(exchange);
      return;
    } else if (path.equals("/project/save/")) {

    } else if (path.equals("/project/remove/")) {

    } else if (path.equals("/project/preset/")) {
      preset(exchange);
      return;
    }
    renderNotFound(exchange);
  }

  private void list(HttpExchange exchange) throws IOException {
    renderWithLayout(exchange, "html/project/list.vm");
  }

  private void edit(HttpExchange exchange) throws IOException {
    renderWithLayout(exchange, "html/project/edit.vm");
  }

  private void preset(HttpExchange exchange) throws IOException {
    renderWithLayout(exchange, "html/project/preset.vm");
  }
}
