package com.walksocket.md.cli.exception;

/**
 * exception not success code.
 */
public class MdExceptionNotSuccessCode extends MdExceptionAbstract {

  @Override
  public ExitCode getExitCode() {
    return ExitCode.NOT_SUCCESS_CODE;
  }
}
