package com.walksocket.md.html;

/**
 * html status.
 */
public enum MdHtmlStatus {

  /**
   * ok.
   */
  OK(200, "OK"),

  /**
   * bad request.
   */
  BAD_REQUEST(400, "Bad Request"),

  /**
   * not found.
   */
  NOT_FOUND(404, "Not Found"),

  /**
   * method not allowed.
   */
  METHOD_NOT_ALLOWED(405, "Method Not Allowed"),

  /**
   * conflict.
   */
  CONFLICT(409, "Conflict"),

  /**
   * internal server error.
   */
  INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
  ;

  /**
   * status.
   */
  private int status;

  /**
   * message.
   */
  private String message;

  /**
   * constructor.
   * @param status status
   * @param message message
   */
  MdHtmlStatus(int status, String message) {
    this.status = status;
    this.message = message;
  }

  /**
   * get status.
   * @return status
   */
  public int getStatus() {
    return status;
  }

  /**
   * get message.
   * @return message
   */
  public String getMessage() {
    return message;
  }

  /**
   * get html message.
   * @return html message
   */
  public String getHtmlMessage() {
    return String.format("<h1>%s %s</h1>", status, message);
  }
}
