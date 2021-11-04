package com.walksocket.md.cli.web.endpoint;

import com.google.gson.annotations.Expose;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.walksocket.md.cli.MdJson;
import com.walksocket.md.cli.MdLogger;
import com.walksocket.md.cli.MdMode;
import com.walksocket.md.cli.output.MdOutputAbstract;
import com.walksocket.md.cli.web.MdWebStatus;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * web endpoint abstract.
 */
abstract public class MdWebEndpointAbstract implements HttpHandler {

  /**
   * header x-execution-id.
   */
  public final static String HEADER_EXECUTION_ID = "x-execution-id";

  /**
   * get mode
   * @param exchange exchange
   * @return mode
   */
  protected MdMode getMode(HttpExchange exchange) {
    String path = exchange.getRequestURI().getPath();
    if (path.contains("/" + MdMode.DIFF.getMode() + "/")) {
      return MdMode.DIFF;
    } else if (path.contains("/" + MdMode.SYNC.getMode() + "/")) {
      return MdMode.SYNC;
    } else if (path.contains("/" + MdMode.MAINTENANCE.getMode() + "/")) {
      return MdMode.MAINTENANCE;
    }
    return null;
  }

  /**
   * send reserved.
   * @param exchange exchange
   * @param executionId execution id
   * @throws IOException error
   */
  public void sendReserved(HttpExchange exchange, String executionId) throws IOException {
    exchange.getResponseHeaders().set(HEADER_EXECUTION_ID, executionId);
    MdLogger.trace(String.format("%s: %s", HEADER_EXECUTION_ID, executionId));
    sendJson(
        exchange,
        MdWebStatus.ACCEPTED.getStatus(),
        new MdWebResponseMessage(MdWebStatus.ACCEPTED));
  }

  /**
   * send processing.
   * @param exchange exchange
   * @param executionId execution id
   * @throws IOException error
   */
  protected void sendProcessing(HttpExchange exchange, String executionId) throws IOException {
    exchange.getResponseHeaders().set(HEADER_EXECUTION_ID, executionId);
    MdLogger.trace(String.format("%s: %s", HEADER_EXECUTION_ID, executionId));
    sendJson(
        exchange,
        MdWebStatus.NO_CONTENT.getStatus(),
        null);
  }

  /**
   * send comlete.
   * @param exchange exchange
   * @param output output
   * @throws IOException error
   */
  protected void sendComplete(HttpExchange exchange, MdOutputAbstract output) throws IOException {
    sendJson(
        exchange,
        MdWebStatus.OK.getStatus(),
        output);
  }

  /**
   * send other.
   * @param exchange exchange
   * @param status status
   * @throws IOException error
   */
  protected void sendOther(HttpExchange exchange, MdWebStatus status) throws IOException {
    sendJson(
        exchange,
        status.getStatus(),
        new MdWebResponseMessage(status));
  }

  /**
   * send json.
   * @param exchange exchange
   * @param status status
   * @param obj obj
   * @throws IOException error
   */
  private void sendJson(HttpExchange exchange, int status, Object obj) throws IOException {
    String json = "";
    int len = 0;
    if (obj != null) {
      json = MdJson.toJsonString(obj);
      len = json.getBytes(StandardCharsets.UTF_8).length;
    }
    exchange.getResponseHeaders().set("Content-Length", String.valueOf(len));
    exchange.getResponseHeaders().set("Content-Type", "application/json; encoding=UTF8");
    exchange.getResponseHeaders().set("Connection", "close");

    if (!json.equals("")) {
      exchange.sendResponseHeaders(status, len);
    } else {
      exchange.sendResponseHeaders(status, -1);
    }

    MdLogger.trace(String.format("%s", status));
    MdLogger.trace(String.format("Content-Length: %s", len));
    MdLogger.trace(String.format("Content-Type: %s", "application/json; encoding=UTF8"));
    MdLogger.trace(String.format("Connection: %s", "close"));

    OutputStream os = exchange.getResponseBody();
    if (!json.equals("")) {
      MdLogger.trace(json);
      os.write(json.getBytes());
    }
    os.close();
  }

  /**
   * web response message.
   */
  public class MdWebResponseMessage {

    /**
     * status.
     */
    @Expose
    public int status;

    /**
     * message.
     */
    @Expose
    public String message;

    /**
     * constructor.
     * @param status status
     */
    public MdWebResponseMessage(MdWebStatus status) {
      this.status = status.getStatus();
      this.message = status.getMessage();
    }
  }
}
