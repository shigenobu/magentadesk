package com.walksocket.md.output.member;

import com.google.gson.annotations.Expose;
import com.walksocket.md.MdValue;
import com.walksocket.md.bash.MdBashResult;

/**
 * command result.
 */
public class MdOutputMemberCommandResult extends MdValue {

  /**
   * command.
   */
  @Expose
  public String command;

  /**
   * code.
   */
  @Expose
  public int code;

  /**
   * output.
   */
  @Expose
  public String output;

  /**
   * constructor.
   *
   * @param result bash result
   */
  public MdOutputMemberCommandResult(MdBashResult result) {
    this.command = result.command;
    this.code = result.code;
    this.output = result.output;
  }
}
