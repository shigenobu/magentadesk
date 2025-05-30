package com.walksocket.md.input.member;

import com.google.gson.annotations.Expose;
import com.walksocket.md.MdValue;

/**
 * input condition.
 */
public class MdInputMemberCondition extends MdValue {

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

  /**
   * constructor.
   */
  public MdInputMemberCondition() {
  }

  /**
   * constructor.
   *
   * @param tableName  table name
   * @param expression expression
   */
  public MdInputMemberCondition(String tableName, String expression) {
    this.tableName = tableName;
    this.expression = expression;
  }
}
