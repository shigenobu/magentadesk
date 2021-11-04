package com.walksocket.md.cli.exception;

/**
 * exception no exists base or compare.
 */
public class MdExceptionNoExistsBaseOrCompare extends MdExceptionAbstract {

  @Override
  public ExitCode getExitCode() {
    return ExitCode.NO_EXISTS_BASE_OR_COMPARE;
  }
}
