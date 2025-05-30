package com.walksocket.md.html.endpoint;

import com.google.gson.annotations.Expose;
import com.walksocket.md.MdEndpointAbstract;
import com.walksocket.md.MdEnv;
import com.walksocket.md.MdJson;
import com.walksocket.md.MdLogger;
import com.walksocket.md.MdTemplate;
import com.walksocket.md.MdUtils;
import com.walksocket.md.html.MdHtmlStatus;
import com.walksocket.md.server.MdServerRequest;
import com.walksocket.md.server.MdServerResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * html endpoint abstract.
 */
abstract public class MdHtmlEndpointAbstract extends MdEndpointAbstract {

  /**
   * base path.
   */
  private static String basePath;

  /**
   * set base path.
   *
   * @param basePath base path
   */
  public static void setBasePath(String basePath) {
    MdHtmlEndpointAbstract.basePath = basePath;
  }

  /**
   * get base path.
   *
   * @return base path
   */
  public static String getBasePath() {
    return MdHtmlEndpointAbstract.basePath;
  }

  /**
   * create template.
   *
   * @param path path
   * @return template
   */
  protected MdTemplate createTemplate(String path) {
    if (MdUtils.isNullOrEmpty(basePath)) {
      return new MdTemplate(path);
    }
    return new MdTemplate(basePath, path);
  }

  @Override
  public void error(MdServerRequest request, MdServerResponse response) {
    try {
      renderOtherWithLayout(response, MdHtmlStatus.INTERNAL_SERVER_ERROR);
    } catch (IOException e) {
      MdLogger.error(e);
    }
  }

  /**
   * render ok.
   *
   * @param response response
   * @param template template
   * @throws IOException error
   */
  protected void renderOk(MdServerResponse response, MdTemplate template) throws IOException {
    sendHtml(response, MdHtmlStatus.OK, template.render());
  }

  /**
   * render ok with layout.
   *
   * @param response response
   * @param template template
   * @throws IOException error
   */
  protected void renderOkWithLayout(MdServerResponse response, MdTemplate template)
      throws IOException {
    MdTemplate layout = createTemplate("html/layout.vm");
    layout.assign("content", template.render());
    sendHtml(response, MdHtmlStatus.OK, layout.render());
  }

  /**
   * render other.
   *
   * @param response response
   * @param status   status
   * @throws IOException error
   */
  protected void renderOther(MdServerResponse response, MdHtmlStatus status) throws IOException {
    sendHtml(response, status, status.getHtmlMessage());
  }

  /**
   * render other with layout.
   *
   * @param response response
   * @param status   status
   * @throws IOException error
   */
  protected void renderOtherWithLayout(MdServerResponse response, MdHtmlStatus status)
      throws IOException {
    MdTemplate layout = createTemplate("html/layout.vm");
    layout.assign("content", status.getHtmlMessage());
    sendHtml(response, status, layout.render());
  }

  /**
   * send ok.
   *
   * @param location location
   * @throws IOException error
   */
  protected void sendOk(MdServerResponse response, String location) throws IOException {
    sendJson(
        response,
        MdHtmlStatus.OK,
        new MdHtmlResponseMessage(MdHtmlStatus.OK, location));
  }

  /**
   * send other.
   *
   * @param status status
   * @throws IOException error
   */
  protected void sendOther(MdServerResponse response, MdHtmlStatus status) throws IOException {
    sendJson(
        response,
        status,
        new MdHtmlResponseMessage(status));
  }

  /**
   * send ok json.
   *
   * @param response response
   * @param json     json
   */
  protected void sendOkJson(MdServerResponse response, String json) {
    response.setStatus(MdHtmlStatus.OK.getStatus());
    response.setContentType("application/json; encoding=UTF8");
    response.setBody(json);
  }

  /**
   * send json.
   *
   * @param status status
   * @param obj    obj
   * @throws IOException error
   */
  private void sendJson(MdServerResponse response, MdHtmlStatus status, Object obj)
      throws IOException {
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
    response.setBody(Objects.requireNonNull(json));
  }

  /**
   * send html.
   *
   * @param response response
   * @param status   status
   * @param body     body
   * @throws IOException error
   */
  private void sendHtml(MdServerResponse response, MdHtmlStatus status, String body)
      throws IOException {
    response.setStatus(status.getStatus());
    response.setContentType("text/html; charset=UTF-8");
    response.setBody(body);
  }

  /**
   * html response message.
   */
  public static class MdHtmlResponseMessage {

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
     * location.
     */
    @Expose
    public String location;

    /**
     * constructor.
     *
     * @param status status
     */
    public MdHtmlResponseMessage(MdHtmlStatus status) {
      this.status = status.getStatus();
      this.message = status.getMessage();
    }

    /**
     * constructor.
     *
     * @param status   status
     * @param location location
     */
    public MdHtmlResponseMessage(MdHtmlStatus status, String location) {
      this.status = status.getStatus();
      this.message = status.getMessage();
      this.location = location;
    }
  }
}
