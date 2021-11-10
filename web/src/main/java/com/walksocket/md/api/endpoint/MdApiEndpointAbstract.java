package com.walksocket.md.api.endpoint;

import com.google.gson.annotations.Expose;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.walksocket.md.MdEnv;
import com.walksocket.md.MdJson;
import com.walksocket.md.MdMode;
import com.walksocket.md.output.MdOutputAbstract;
import com.walksocket.md.api.MdApiStatus;
import com.walksocket.md.server.MdServerRequest;
import com.walksocket.md.server.MdServerResponse;

import java.io.IOException;

/**
 * api endpoint abstract.
 */
abstract public class MdApiEndpointAbstract implements HttpHandler {

  /**
   * header x-execution-id.
   */
  public final static String HEADER_EXECUTION_ID = "x-execution-id";

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

  /**
   * get mode
   * @return mode
   */
  protected MdMode getMode() {
    String path = request.getPath();
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
   * @param executionId execution id
   * @throws IOException error
   */
  public void sendReserved(String executionId) throws IOException {
    response.addHeader(HEADER_EXECUTION_ID, executionId);
    sendJson(
        MdApiStatus.ACCEPTED.getStatus(),
        new MdApiResponseMessage(MdApiStatus.ACCEPTED));
  }

  /**
   * send processing.
   * @param executionId execution id
   * @throws IOException error
   */
  protected void sendProcessing(String executionId) throws IOException {
    response.addHeader(HEADER_EXECUTION_ID, executionId);
    sendJson(
        MdApiStatus.NO_CONTENT.getStatus(),
        null);
  }

  /**
   * send comlete.
   * @param output output
   * @throws IOException error
   */
  protected void sendComplete(MdOutputAbstract output) throws IOException {
    sendJson(
        MdApiStatus.OK.getStatus(),
        output);
  }

  /**
   * send other.
   * @param status status
   * @throws IOException error
   */
  protected void sendOther(MdApiStatus status) throws IOException {
    sendJson(
        status.getStatus(),
        new MdApiResponseMessage(status));
  }

  /**
   * send json.
   * @param status status
   * @param obj obj
   * @throws IOException error
   */
  private void sendJson(int status, Object obj) throws IOException {
    String json = "";
    if (obj != null) {
      if (MdEnv.isPretty()) {
        json = MdJson.toJsonStringFriendly(obj);
      } else {
        json = MdJson.toJsonString(obj);
      }
    }
    response.setStatus(status);
    response.setContentType("application/json; encoding=UTF8");
    response.send(json);
  }

  /**
   * api response message.
   */
  public class MdApiResponseMessage {

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
    public MdApiResponseMessage(MdApiStatus status) {
      this.status = status.getStatus();
      this.message = status.getMessage();
    }
  }
}
