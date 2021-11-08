package com.walksocket.md.api;

/**
 * api state.
 */
public enum MdApiState {

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
  MdApiState(String state) {
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
