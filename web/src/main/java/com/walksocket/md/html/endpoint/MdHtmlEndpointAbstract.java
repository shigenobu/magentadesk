package com.walksocket.md.html.endpoint;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.walksocket.md.MdLogger;
import com.walksocket.md.MdTemplate;
import com.walksocket.md.MdUtils;
import com.walksocket.md.server.MdServerRequest;
import com.walksocket.md.server.MdServerResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * html endpoint abstract.
 */
abstract public class MdHtmlEndpointAbstract implements HttpHandler {

  private static String basePath;

  public static void setBasePath(String basePath) {
    MdHtmlEndpointAbstract.basePath = basePath;
  }

  protected MdTemplate createTemplate(String path) {
    if (MdUtils.isNullOrEmpty(basePath)) {
      return new MdTemplate(path);
    }
    return new MdTemplate(basePath, path);
  }

  /**
   * request.
   */
  protected MdServerRequest request;

  /**
   * response.
   */
  protected MdServerResponse response;

  /**
   * init.
   * @param exchange exchange
   */
  protected void init(HttpExchange exchange) {
    request = new MdServerRequest(exchange);
    response = new MdServerResponse(exchange);
  }

  protected void render(MdTemplate template) throws IOException {
    sendResponse(200, template.render());
  }

  protected void renderWithLayout(MdTemplate template) throws IOException {
    MdTemplate layout = createTemplate("html/layout.vm");
    layout.assign("content", template.render());
    sendResponse(200, layout.render());
  }

  protected void renderNotFound() throws IOException {
    String data = "<h1>404 Not Found</h1>";
    sendResponse(404, data);
  }

  private void sendResponse(int status, String data) throws IOException {
    response.setStatus(status);
    response.setContentType("text/html");
    response.send(data);
  }
}
