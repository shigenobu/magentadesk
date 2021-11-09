package com.walksocket.md.html.endpoint;

import com.sun.net.httpserver.HttpExchange;
import com.walksocket.md.MdLogger;

import java.io.IOException;

public class MdHtmlEndpointIndex extends MdHtmlEndpointAbstract {
  @Override
  public void handle(HttpExchange exchange) throws IOException {
    MdLogger.trace(getClass().getSimpleName() + ":" + exchange.getRequestURI());

    renderWithLayout(exchange, "html/index.vm");
  }
}
