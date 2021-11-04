package com.walksocket.md.web;

import com.google.gson.annotations.Expose;
import com.walksocket.md.MdJson;

public enum MdWebStatus {

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
  MdWebStatus(int status, String message) {
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
}
