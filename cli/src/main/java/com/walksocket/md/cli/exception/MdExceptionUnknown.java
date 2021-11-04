package com.walksocket.md.cli.exception;

/**
 * exception unknown.
 */
public class MdExceptionUnknown extends MdExceptionAbstract {

  @Override
  public ExitCode getExitCode() {
    return ExitCode.UNKNOWN;
  }
}
