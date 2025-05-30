package com.walksocket.md.output.member;

import com.google.gson.annotations.Expose;
import com.walksocket.md.MdValue;
import com.walksocket.md.http.MdHttpClient;

/**
 * http result.
 */
public class MdOutputMemberHttpResult extends MdValue {

  /**
   * url.
   */
  @Expose
  public String url;

  /**
   * status.
   */
  @Expose
  public int status;

  /**
   * body.
   */
  @Expose
  public String body;

  /**
   * constructor.
   *
   * @param url          url
   * @param httpResponse http response
   */
  public MdOutputMemberHttpResult(String url, MdHttpClient.MdHttpClientResponse httpResponse) {
    this.url = url;
    status = httpResponse.getStatus();
    body = httpResponse.getBodyString();
  }
}
