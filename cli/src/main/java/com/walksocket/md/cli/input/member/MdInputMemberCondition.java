package com.walksocket.md.cli.input.member;

import com.google.gson.annotations.Expose;

/**
 * input condition.
 */
public class MdInputMemberCondition {

  /**
   * command.
   */
  @Expose
  public String tableName;

  /**
   * command.
   */
  @Expose
  public String expression;
}
