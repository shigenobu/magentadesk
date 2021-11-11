package com.walksocket.md.html.endpoint;

import com.walksocket.md.MdTemplate;
import com.walksocket.md.html.MdHtmlStatus;
import com.walksocket.md.server.MdServerRequest;
import com.walksocket.md.server.MdServerResponse;
import com.walksocket.md.sqlite.MdSqliteConnection;

import java.io.IOException;

/**
 * html endpoint project.
 */
public class MdHtmlEndpointProject extends MdHtmlEndpointAbstract {

  @Override
  public void action(MdServerRequest request, MdServerResponse response, MdSqliteConnection con) throws Exception {
    String path = request.getPath();
    if (path.equals("/project/list/")) {
      list(request, response, con);
      return;
    } else if (path.equals("/project/edit/")) {
      edit(request, response, con);
      return;
    } else if (path.equals("/project/save/")) {

    } else if (path.equals("/project/remove/")) {

    }
    renderOtherWithLayout(response, MdHtmlStatus.NOT_FOUND);
  }

  private void list(MdServerRequest request, MdServerResponse response, MdSqliteConnection con) throws IOException {
    // template
    MdTemplate template = createTemplate("html/project/list.vm");

    // render
    renderOkWithLayout(response, template);
  }

  private void edit(MdServerRequest request, MdServerResponse response, MdSqliteConnection con) throws IOException {
    // template
    MdTemplate template = createTemplate("html/project/edit.vm");

    // render
    renderOkWithLayout(response, template);
  }
}
