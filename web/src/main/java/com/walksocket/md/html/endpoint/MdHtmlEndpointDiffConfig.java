package com.walksocket.md.html.endpoint;

import com.sun.net.httpserver.HttpExchange;
import com.walksocket.md.MdLogger;

import java.io.IOException;

public class MdHtmlEndpointDiffConfig extends MdHtmlEndpointAbstract {
  @Override
  public void handle(HttpExchange exchange) throws IOException {
    MdLogger.trace(getClass().getSimpleName() + ":" + exchange.getRequestURI());

    String path = exchange.getRequestURI().getPath();
    if (path.equals("/diffConfig/list/")) {
      list(exchange);
      return;
    } else if (path.equals("/diffConfig/edit/")) {
      edit(exchange);
      return;
    } else if (path.equals("/diffConfig/save/")) {

    } else if (path.equals("/diffConfig/remove/")) {

    }
    renderNotFound(exchange);
  }

  private void list(HttpExchange exchange) throws IOException {
    renderWithLayout(exchange, "html/diffConfig/list.vm");
  }

  private void edit(HttpExchange exchange) throws IOException {
    renderWithLayout(exchange, "html/diffConfig/edit.vm");
  }
}
