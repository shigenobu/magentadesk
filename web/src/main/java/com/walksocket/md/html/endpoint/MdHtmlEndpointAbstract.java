package com.walksocket.md.html.endpoint;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.walksocket.md.MdLogger;
import com.walksocket.md.MdTemplate;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * html endpoint abstract.
 */
abstract public class MdHtmlEndpointAbstract implements HttpHandler {

  protected void renderWithLayout(HttpExchange exchange, String path) throws IOException {
//    MdTemplate template = new MdTemplate(path);
    MdTemplate template = new MdTemplate("/home/furuta/02-development/idea/magentadesk/web/src/main/resources", path);
    String html = template.render();

//    MdTemplate layout = new MdTemplate("html/layout.vm");
    MdTemplate layout = new MdTemplate("/home/furuta/02-development/idea/magentadesk/web/src/main/resources", "html/layout.vm");
    layout.assign("content", html);

    render(exchange, layout.render());
  }

  protected void render(HttpExchange exchange, String data) throws IOException {
    int status = 200;
    int len = data.getBytes(StandardCharsets.UTF_8).length;
    exchange.getResponseHeaders().set("Content-Length", String.valueOf(len));
    exchange.getResponseHeaders().set("Content-Type", "text/html");
    exchange.getResponseHeaders().set("Connection", "close");

    if (!data.equals("")) {
      exchange.sendResponseHeaders(status, len);
    } else {
      exchange.sendResponseHeaders(status, -1);
    }

    MdLogger.trace(String.format("%s", status));
    MdLogger.trace(String.format("Content-Length: %s", len));
    MdLogger.trace(String.format("Content-Type: %s", "text/html"));
    MdLogger.trace(String.format("Connection: %s", "close"));

    OutputStream os = exchange.getResponseBody();
    if (!data.equals("")) {
      MdLogger.trace(data);
      os.write(data.getBytes(StandardCharsets.UTF_8));
    }
    os.close();
  }
}
