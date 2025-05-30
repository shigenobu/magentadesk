package com.walksocket.md.html;

/**
 * html save mode.
 */
public enum MdHtmlSaveMode {

  /**
   * insert.
   */
  INSERT("INSERT", "btn-insert"),

  /**
   * update.
   */
  UPDATE("UPDATE", "btn-update"),
  ;

  /**
   * get name.
   *
   * @return name
   */
  public static String getName() {
    return "saveMode";
  }

  /**
   * value.
   */
  private final String value;

  /**
   * css.
   */
  private final String css;

  /**
   * constructor.
   *
   * @param value value
   * @param css   css
   */
  MdHtmlSaveMode(String value, String css) {
    this.value = value;
    this.css = css;
  }

  /**
   * get value.
   *
   * @return value
   */
  public String getValue() {
    return value;
  }

  /**
   * get css.
   *
   * @return css
   */
  public String getCss() {
    return css;
  }
}
