package com.walksocket.md.html.endpoint;

import com.walksocket.md.MdTemplate;
import com.walksocket.md.server.MdServerRequest;
import com.walksocket.md.server.MdServerResponse;
import com.walksocket.md.sqlite.MdSqliteConnection;

/**
 * html endpoint index.
 */
public class MdHtmlEndpointIndex extends MdHtmlEndpointAbstract {

  @Override
  public void action(MdServerRequest request, MdServerResponse response, MdSqliteConnection con) throws Exception {
    MdTemplate template = createTemplate("html/index.vm");
    renderOkWithLayout(response, template);
  }
}
