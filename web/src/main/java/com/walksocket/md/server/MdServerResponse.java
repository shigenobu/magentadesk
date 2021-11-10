package com.walksocket.md.server;

import com.sun.net.httpserver.HttpExchange;
import com.walksocket.md.MdLogger;
import com.walksocket.md.MdUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MdServerResponse {

  private HttpExchange exchange;

  private int status;

  private Map<String, String> headers = new HashMap<>();

  public MdServerResponse(HttpExchange exchange) {
    this.exchange = exchange;

    // init
    status = 200;
    headers.put("Content-Length", "0");
    headers.put("Content-Type", "text/html");
    headers.put("Cache-Control", "no-store, no-cache");
    headers.put("Connection", "close");
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public void setContentType(String contentType) {
    headers.put("Content-Type", contentType);
  }

  public void setContentLength(int contentLength) {
    headers.put("Content-Length", String.valueOf(contentLength));
  }

  public void setCacheControl(String cacheControl) {
    headers.put("Cache-Control", cacheControl);
  }

  public void setConnection(String connection) {
    headers.put("Connection", connection);
  }

  public void addHeader(String name, String value) {
    headers.put(name, value);
  }

  public void send(String body) throws IOException {
    if (MdUtils.isNullOrEmpty(body)) {
      send(new byte[0]);
    } else {
      send(body.getBytes(StandardCharsets.UTF_8));
    }
  }

  public void send(byte[] body) throws IOException {
    // len
    int len = 0;
    if (!MdUtils.isNullOrEmpty(body)) {
      len = body.length;
      setContentLength(len);
    }

    // header
    for (Map.Entry<String, String> entry : headers.entrySet()) {
      exchange.getResponseHeaders().set(entry.getKey(), entry.getValue());
      MdLogger.trace(String.format("%s: %s", entry.getKey(), entry.getValue()));
    }

    // status
    if (len > 0) {
      exchange.sendResponseHeaders(status, len);
    } else {
      exchange.sendResponseHeaders(status, -1);
    }

    // body
    OutputStream os = exchange.getResponseBody();
    if (len > 0) {
      os.write(body);
    }
    os.close();
  }
}
