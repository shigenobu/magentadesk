package com.walksocket.md;

/**
 * mode enum.
 */
public enum MdMode {

  /**
   * diff.
   */
  DIFF("diff"),

  /**
   * sync.
   */
  SYNC("sync"),

  /**
   * maintenance.
   */
  MAINTENANCE("maintenance"),

  /**
   * web
   */
  WEB("web"),
  ;

  /**
   * mode.
   */
  private String mode;

  /**
   * constructor.
   * @param mode mode
   */
  MdMode(String mode) {
    this.mode = mode;
  }

  /**
   * get mode.
   * @return mode
   */
  public String getMode() {
    return mode;
  }
}
