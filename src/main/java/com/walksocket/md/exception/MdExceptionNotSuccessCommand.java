package com.walksocket.md.exception;

/**
 * exception not success command.
 */
public class MdExceptionNotSuccessCommand extends MdExceptionAbstract {

  @Override
  public ExitCode getExitCode() {
    return ExitCode.NOT_SUCCESS_COMMAND;
  }
}
