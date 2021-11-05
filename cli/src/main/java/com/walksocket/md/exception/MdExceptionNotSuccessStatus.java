package com.walksocket.md.exception;

/**
 * exception not success status.
 */
public class MdExceptionNotSuccessStatus extends MdExceptionAbstract {

  @Override
  public ExitCode getExitCode() {
    return ExitCode.NOT_SUCCESS_STATUS;
  }
}
