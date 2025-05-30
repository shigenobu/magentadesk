package com.walksocket.md.api;

/**
 * api status.
 */
public enum MdApiStatus {

  /**
   * ok when complete.
   */
  OK(200, "OK"),

  /**
   * accepted when reserved.
   */
  ACCEPTED(202, "Accepted"),

  /**
   * no content when processing.
   */
  NO_CONTENT(204, "No Content"),

  /**
   * bad request when request is invalid.
   */
  BAD_REQUEST(400, "Bad Request"),

  /**
   * not found when mode is null.
   */
  NOT_FOUND(404, "Not Found"),

  /**
   * method not allowed when method is not POST.
   */
  METHOD_NOT_ALLOWED(405, "Method Not Allowed"),

  /**
   * conflict when no output exists.
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
  private final int status;

  /**
   * message.
   */
  private final String message;

  /**
   * constructor.
   *
   * @param status  status
   * @param message message
   */
  MdApiStatus(int status, String message) {
    this.status = status;
    this.message = message;
  }

  /**
   * get status.
   *
   * @return status
   */
  public int getStatus() {
    return status;
  }

  /**
   * get message.
   *
   * @return message
   */
  public String getMessage() {
    return message;
  }
}
