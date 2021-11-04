package com.walksocket.md.cli.output.member;

import com.google.gson.annotations.Expose;
import com.walksocket.md.cli.bash.MdBashResult;

/**
 * command result.
 */
public class MdOutputMemberCommandResult {

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
   * @param result bash result
   */
  public MdOutputMemberCommandResult(MdBashResult result) {
    this.command = result.command;
    this.code = result.code;
    this.output = result.output;
  }
}
