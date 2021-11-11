package com.walksocket.md.html.endpoint;

import com.walksocket.md.MdTemplate;
import com.walksocket.md.html.MdHtmlStatus;
import com.walksocket.md.server.MdServerRequest;
import com.walksocket.md.server.MdServerResponse;
import com.walksocket.md.sqlite.MdSqliteConnection;

import java.io.IOException;

/**
 * html endpoint preset.
 */
public class MdHtmlEndpointPreset extends MdHtmlEndpointAbstract {

  @Override
  public void action(MdServerRequest request, MdServerResponse response, MdSqliteConnection con) throws Exception {
    String path = request.getPath();
    if (path.equals("/preset/list/")) {
      list(request, response, con);
      return;
    } else if (path.equals("/preset/edit/")) {
      edit(request, response, con);
      return;
    } else if (path.equals("/preset/save/")) {

    } else if (path.equals("/preset/remove/")) {

    }
    renderOtherWithLayout(response, MdHtmlStatus.NOT_FOUND);
  }

  private void list(MdServerRequest request, MdServerResponse response, MdSqliteConnection con) throws IOException {
    // template
    MdTemplate template = createTemplate("html/preset/list.vm");

    // render
    renderOkWithLayout(response, template);
  }

  private void edit(MdServerRequest request, MdServerResponse response, MdSqliteConnection con) throws IOException {
    // template
    MdTemplate template = createTemplate("html/preset/edit.vm");

    // render
    renderOkWithLayout(response, template);
  }
}
