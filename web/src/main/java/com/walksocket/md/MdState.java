package com.walksocket.md;

/**
 * state.
 */
public enum MdState {

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
  private final String state;

  /**
   * constructor.
   *
   * @param state state
   */
  MdState(String state) {
    this.state = state;
  }

  /**
   * get state.
   *
   * @return state
   */
  public String getState() {
    return state;
  }
}
