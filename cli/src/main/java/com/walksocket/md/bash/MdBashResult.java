package com.walksocket.md.bash;

import com.google.gson.annotations.Expose;

/**
 * bash result.
 */
public class MdBashResult {

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

  @Override
  public String toString() {
    return "MdBashResult{" +
        "command='" + command + '\'' +
        ", code=" + code +
        ", output='" + output + '\'' +
        '}';
  }
}
