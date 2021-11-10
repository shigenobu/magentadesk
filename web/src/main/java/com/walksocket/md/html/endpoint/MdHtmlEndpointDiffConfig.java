package com.walksocket.md.html.endpoint;

import com.sun.net.httpserver.HttpExchange;
import com.walksocket.md.MdLogger;
import com.walksocket.md.MdTemplate;

import java.io.IOException;

public class MdHtmlEndpointDiffConfig extends MdHtmlEndpointAbstract {
  @Override
  public void handle(HttpExchange exchange) throws IOException {
    init(exchange);
    MdLogger.trace(getClass().getSimpleName() + ":" + request.getPath());

    String path = request.getPath();
    if (path.equals("/diffConfig/list/")) {
      list();
      return;
    } else if (path.equals("/diffConfig/edit/")) {
      edit();
      return;
    } else if (path.equals("/diffConfig/save/")) {

    } else if (path.equals("/diffConfig/remove/")) {

    }
    renderNotFound();
  }

  private void list() throws IOException {
    MdTemplate template = createTemplate("html/diffConfig/list.vm");
    renderWithLayout(template);
  }

  private void edit() throws IOException {
    MdTemplate template = createTemplate("html/diffConfig/edit.vm");
    renderWithLayout(template);
  }
}
