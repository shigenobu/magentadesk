package com.walksocket.md.html.endpoint;

import com.sun.net.httpserver.HttpExchange;
import com.walksocket.md.MdLogger;

import java.io.IOException;

public class MdHtmlEndpointSyncConfig extends MdHtmlEndpointAbstract {
  @Override
  public void handle(HttpExchange exchange) throws IOException {
    MdLogger.trace(getClass().getSimpleName() + ":" + exchange.getRequestURI());

    String path = exchange.getRequestURI().getPath();
    if (path.equals("/syncConfig/list/")) {
      list(exchange);
      return;
    } else if (path.equals("/syncConfig/edit/")) {
      edit(exchange);
      return;
    } else if (path.equals("/syncConfig/save/")) {

    } else if (path.equals("/syncConfig/remove/")) {

    }
    renderNotFound(exchange);
  }

  private void list(HttpExchange exchange) throws IOException {
    renderWithLayout(exchange, "html/syncConfig/list.vm");
  }

  private void edit(HttpExchange exchange) throws IOException {
    renderWithLayout(exchange, "html/syncConfig/edit.vm");
  }
}
