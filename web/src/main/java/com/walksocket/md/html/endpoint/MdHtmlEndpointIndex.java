package com.walksocket.md.html.endpoint;

import com.sun.net.httpserver.HttpExchange;
import com.walksocket.md.MdLogger;
import com.walksocket.md.MdTemplate;

import java.io.IOException;

public class MdHtmlEndpointIndex extends MdHtmlEndpointAbstract {
  @Override
  public void handle(HttpExchange exchange) throws IOException {
    init(exchange);
    MdLogger.trace(getClass().getSimpleName() + ":" + request.getPath());

    MdTemplate template = createTemplate("html/index.vm");
    renderWithLayout(template);
  }
}
