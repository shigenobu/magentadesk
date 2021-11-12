package com.walksocket.md.input.member;

import com.google.gson.annotations.Expose;
import com.walksocket.md.MdValue;

/**
 * input relation.
 */
public class MdInputMemberRelation extends MdValue {

  /**
   * from.
   */
  @Expose
  public String from;

  /**
   * to.
   */
  @Expose
  public String to;

  /**
   * constructor.
   */
  public MdInputMemberRelation() {}

  /**
   * constructor.
   * @param from from
   * @param to to
   */
  public MdInputMemberRelation(String from, String to) {
    this.from = from;
    this.to = to;
  }
}
