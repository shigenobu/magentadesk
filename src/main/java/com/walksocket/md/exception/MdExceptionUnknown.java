package com.walksocket.md.exception;

/**
 * exception unknown.
 */
public class MdExceptionUnknown extends MdExceptionAbstract {

  @Override
  public ExitCode getExitCode() {
    return ExitCode.UNKNOWN;
  }
}
