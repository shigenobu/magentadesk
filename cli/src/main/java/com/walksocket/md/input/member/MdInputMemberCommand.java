package com.walksocket.md.input.member;

import com.google.gson.annotations.Expose;
import com.walksocket.md.MdUtils;
import com.walksocket.md.MdValue;

import java.util.List;

/**
 * input command.
 */
public class MdInputMemberCommand extends MdValue {

  /**
   * command.
   */
  @Expose
  public String command;

  /**
   * timeout.
   */
  @Expose
  public int timeout;

  /**
   * success code list.
   */
  @Expose
  public List<Integer> successCodeList;

  /**
   * constructor.
   * @param command command
   * @param timeout timeout
   * @param successCodeList success code list
   */
  public MdInputMemberCommand(String command, int timeout, List<Integer> successCodeList) {
    this.command = command;
    this.timeout = timeout;
    this.successCodeList = successCodeList;
  }

  /**
   * is valid.
   * @return if valid, return true
   */
  public boolean isValid() {
    if (MdUtils.isNullOrEmpty(command)) {
      return false;
    }
    if (timeout <= 0) {
      return false;
    }
    if (MdUtils.isNullOrEmpty(successCodeList)) {
      return false;
    }
    return true;
  }
}
