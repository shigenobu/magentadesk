package com.walksocket.md.input.member;

import com.google.gson.annotations.Expose;

/**
 * input command.
 */
public class MdInputMemberCommand {

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
}
