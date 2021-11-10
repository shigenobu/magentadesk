package com.walksocket.md.html.endpoint;

import com.sun.net.httpserver.HttpExchange;
import com.walksocket.md.MdLogger;
import com.walksocket.md.MdTemplate;

import java.io.IOException;

public class MdHtmlEndpointPreset extends MdHtmlEndpointAbstract {
  @Override
  public void handle(HttpExchange exchange) throws IOException {
    init(exchange);
    MdLogger.trace(getClass().getSimpleName() + ":" + request.getPath());

    String path = request.getPath();
    if (path.equals("/preset/list/")) {
      list();
      return;
    } else if (path.equals("/preset/edit/")) {
      edit();
      return;
    } else if (path.equals("/preset/save/")) {

    } else if (path.equals("/preset/remove/")) {

    }
    renderNotFound();
  }

  private void list() throws IOException {
    MdTemplate template = createTemplate("html/preset/list.vm");
    renderWithLayout(template);
  }

  private void edit() throws IOException {
    MdTemplate template = createTemplate("html/preset/edit.vm");
    renderWithLayout(template);
  }
}
