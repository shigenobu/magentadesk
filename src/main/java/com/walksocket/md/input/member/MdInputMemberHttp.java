package com.walksocket.md.input.member;

import com.google.gson.annotations.Expose;

import java.util.List;

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
   * success status list.
   */
  @Expose
  public List<Integer> successStatusList;

  /**
   * constructor.
   * @param url command
   * @param timeout timeout
   * @param successStatusList success status list
   */
  public MdInputMemberHttp(String url, int timeout, List<Integer> successStatusList) {
    this.url = url;
    this.timeout = timeout;
    this.successStatusList = successStatusList;
  }
}
