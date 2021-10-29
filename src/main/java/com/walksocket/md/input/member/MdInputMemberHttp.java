package com.walksocket.md.input.member;

import com.google.gson.annotations.Expose;

/**
 * input http
 */
public class MdInputMemberHttp {

  /**
   * url.
   */
  @Expose
  public String url;

  /**
   * timeout.
   */
  @Expose
  public int timeout;

  /**
   * constructor.
   * @param url command
   * @param timeout timeout
   */
  public MdInputMemberHttp(String url, int timeout) {
    this.url = url;
    this.timeout = timeout;
  }
}
