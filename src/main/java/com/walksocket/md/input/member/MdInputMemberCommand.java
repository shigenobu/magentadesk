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

  /**
   * constructor.
   * @param command command
   * @param timeout timeout
   */
  public MdInputMemberCommand(String command, int timeout) {
    this.command = command;
    this.timeout = timeout;
  }
}
