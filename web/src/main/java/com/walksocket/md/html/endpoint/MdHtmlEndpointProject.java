package com.walksocket.md.html.endpoint;

import com.sun.net.httpserver.HttpExchange;
import com.walksocket.md.MdLogger;
import com.walksocket.md.MdTemplate;

import java.io.IOException;

public class MdHtmlEndpointProject extends MdHtmlEndpointAbstract {
  @Override
  public void handle(HttpExchange exchange) throws IOException {
    init(exchange);
    MdLogger.trace(getClass().getSimpleName() + ":" + request.getPath());

    String path = request.getPath();
    if (path.equals("/project/list/")) {
      list();
      return;
    } else if (path.equals("/project/edit/")) {
      edit();
      return;
    } else if (path.equals("/project/save/")) {

    } else if (path.equals("/project/remove/")) {

    } else if (path.equals("/project/preset/")) {
      preset();
      return;
    }
    renderNotFound();
  }

  private void list() throws IOException {
    MdTemplate template = createTemplate("html/project/list.vm");
    renderWithLayout(template);
  }

  private void edit() throws IOException {
    MdTemplate template = createTemplate("html/project/edit.vm");
    renderWithLayout(template);
  }

  private void preset() throws IOException {
    MdTemplate template = createTemplate("html/project/preset.vm");
    renderWithLayout(template);
  }
}
