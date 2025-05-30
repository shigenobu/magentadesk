package com.walksocket.md.input.member;

import com.google.gson.annotations.Expose;
import com.walksocket.md.MdUtils;
import com.walksocket.md.MdValue;
import java.util.List;

/**
 * input http
 */
public class MdInputMemberHttp extends MdValue {

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
   *
   * @param url               command
   * @param timeout           timeout
   * @param successStatusList success status list
   */
  public MdInputMemberHttp(String url, int timeout, List<Integer> successStatusList) {
    this.url = url;
    this.timeout = timeout;
    this.successStatusList = successStatusList;
  }

  /**
   * is valid before.
   *
   * @return if valid, return true
   */
  public boolean isValidBefore() {
    if (MdUtils.isNullOrEmpty(url) || !url.startsWith("http")) {
      return false;
    }
    if (timeout <= 0) {
      return false;
    }
    return !MdUtils.isNullOrEmpty(successStatusList);
  }

  /**
   * is valid after.
   *
   * @return if valid, return true
   */
  public boolean isValidAfter() {
    if (MdUtils.isNullOrEmpty(url) || !url.startsWith("http")) {
      return false;
    }
    return timeout > 0;
  }
}
