package com.walksocket.md.cli.output.member;

import com.google.gson.annotations.Expose;
import com.walksocket.md.cli.http.MdHttpClient;

/**
 * http result.
 */
public class MdOutputMemberHttpResult {

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
   * @param httpResponse http response
   */
  public MdOutputMemberHttpResult(MdHttpClient.MdHttpClientResponse httpResponse) {
    status = httpResponse.getStatus();
    body = httpResponse.getBodyString();
  }
}