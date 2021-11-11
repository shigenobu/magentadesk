package com.walksocket.md.server;

import com.sun.net.httpserver.HttpExchange;
import com.walksocket.md.MdLogger;
import com.walksocket.md.MdUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class MdServerResponse {

  /**
   * exchange.
   */
  private HttpExchange exchange;

  /**
   * status.
   */
  private int status;

  /**
   * headers.
   */
  private Map<String, String> headers = new HashMap<>();

  /**
   * sent response.
   */
  private boolean sentResponse = false;

  /**
   * constructor.
   * @param exchange exchange
   */
  public MdServerResponse(HttpExchange exchange) {
    this.exchange = exchange;

    // init
    setStatus(200);
    setContentLength(0);
    setContentType("application/octet-stream");
    setCacheControl("no-store, no-cache");
    setConnection("close");
  }

  /**
   * set status.
   * @param status status
   */
  public void setStatus(int status) {
    this.status = status;
  }

  /**
   * set content type.
   * @param contentType content type
   */
  public void setContentType(String contentType) {
    headers.put("Content-Type", contentType);
  }

  /**
   * set content length.
   * @param contentLength content length
   */
  public void setContentLength(int contentLength) {
    headers.put("Content-Length", String.valueOf(contentLength));
  }

  /**
   * set cache control.
   * @param cacheControl cache control
   */
  public void setCacheControl(String cacheControl) {
    headers.put("Cache-Control", cacheControl);
  }

  /**
   * set connection.
   * @param connection connection
   */
  public void setConnection(String connection) {
    headers.put("Connection", connection);
  }

  /**
   * add header.
   * @param name name
   * @param value value
   */
  public void addHeader(String name, String value) {
    headers.put(name, value);
  }

  /**
   * send.
   * @param body body string
   * @throws IOException error
   */
  public void send(String body) throws IOException {
    if (MdUtils.isNullOrEmpty(body)) {
      send(new byte[0]);
    } else {
      send(body.getBytes(StandardCharsets.UTF_8));
    }
  }

  /**
   * send.
   * @param body body bytes
   * @throws IOException error
   */
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

    // sent
    sentResponse = true;
  }

  /**
   * is sent response.
   * @return if true, response is sent
   */
  public boolean isSentResponse() {
    return sentResponse;
  }
}
