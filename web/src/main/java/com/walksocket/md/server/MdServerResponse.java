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
   * body.
   */
  private byte[] body = new byte[0];

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
   * get status.
   * @return status
   */
  public int getStatus() {
    return status;
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
   * set body.
   * @param body body string
   */
  public void setBody(String body) {
    this.body = body.getBytes(StandardCharsets.UTF_8);
  }

  /**
   * set body.
   * @param body body bytes
   */
  public void setBody(byte[] body) {
    this.body = body;
  }

  /**
   * get body
   * @return body bytes.
   */
  public byte[] getBody() {
    return body;
  }

  /**
   * send.
   * @throws IOException error
   */
  public void send() throws IOException {
    if (sentResponse) {
      return;
    }

    // len
    int len = 0;
    if (!MdUtils.isNullOrEmpty(body)) {
      len = body.length;
      setContentLength(len);
    }

    // header
    for (Map.Entry<String, String> entry : headers.entrySet()) {
      exchange.getResponseHeaders().set(entry.getKey(), entry.getValue());
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
