package com.walksocket.md.api.endpoint;

import com.google.gson.annotations.Expose;
import com.walksocket.md.*;
import com.walksocket.md.api.MdApiStatus;
import com.walksocket.md.output.MdOutputAbstract;
import com.walksocket.md.server.MdServerRequest;
import com.walksocket.md.server.MdServerResponse;

import java.io.IOException;

/**
 * api endpoint abstract.
 */
abstract public class MdApiEndpointAbstract extends MdEndpointAbstract {

  /**
   * header x-execution-id.
   */
  public final static String HEADER_EXECUTION_ID = "x-execution-id";

  /**
   * get mode
   * @return mode
   */
  protected MdMode getMode(MdServerRequest request) {
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

  @Override
  public void error(MdServerRequest request, MdServerResponse response) {
    try {
      sendOther(response, MdApiStatus.INTERNAL_SERVER_ERROR);
    } catch (IOException e) {
      MdLogger.error(e);
    }
  }

  @Override
  public void last(MdServerRequest request, MdServerResponse response) {
    try {
      if (!response.isSentResponse()) {
        sendOther(response, MdApiStatus.INTERNAL_SERVER_ERROR);
      }
    } catch (IOException e) {
      MdLogger.error(e);
    }
  }

  /**
   * send reserved.
   * @throws IOException error
   */
  public void sendReserved(MdServerResponse response) throws IOException {
    sendJson(
        response,
        MdApiStatus.ACCEPTED,
        new MdApiResponseMessage(MdApiStatus.ACCEPTED));
  }

  /**
   * send processing.
   * @throws IOException error
   */
  protected void sendProcessing(MdServerResponse response) throws IOException {
    sendJson(
        response,
        MdApiStatus.NO_CONTENT,
        null);
  }

  /**
   * send comlete.
   * @param output output
   * @throws IOException error
   */
  protected void sendComplete(MdServerResponse response, MdOutputAbstract output) throws IOException {
    sendJson(
        response,
        MdApiStatus.OK,
        output);
  }

  /**
   * send other.
   * @param status status
   * @throws IOException error
   */
  protected void sendOther(MdServerResponse response, MdApiStatus status) throws IOException {
    sendJson(
        response,
        status,
        new MdApiResponseMessage(status));
  }

  /**
   * send json.
   * @param status status
   * @param obj obj
   * @throws IOException error
   */
  private void sendJson(MdServerResponse response, MdApiStatus status, Object obj) throws IOException {
    String json = "";
    if (obj != null) {
      if (MdEnv.isPretty()) {
        json = MdJson.toJsonStringFriendly(obj);
      } else {
        json = MdJson.toJsonString(obj);
      }
    }
    response.setStatus(status.getStatus());
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
