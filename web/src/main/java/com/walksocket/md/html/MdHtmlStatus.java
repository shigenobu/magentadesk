package com.walksocket.md.html;

public enum MdHtmlStatus {

  OK(200, "OK"),

  BAD_REQUEST(400, "Bad Request"),

  NOT_FOUND(404, "Not Found"),

  METHOD_NOT_ALLOWED(405, "Method Not Allowed"),

  CONFLICT(409, "Conflict"),

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
