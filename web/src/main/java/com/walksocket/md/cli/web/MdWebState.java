package com.walksocket.md.cli.web;

/**
 * web state.
 */
public enum MdWebState {

  /**
   * reserved.
   */
  RESERVED("reserved"),

  /**
   * processing.
   */
  PROCESSING("processing"),

  /**
   * complete.
   */
  COMPLETE("complete"),

  /**
   * error.
   */
  ERROR("error"),
  ;

  /**
   * state.
   */
  private String state;

  /**
   * constructor.
   * @param state state
   */
  MdWebState(String state) {
    this.state = state;
  }

  /**
   * get state.
   * @return state
   */
  public String getState() {
    return state;
  }
}
