package com.walksocket.md.exception;

/**
 * exception disallow simultaneous execution.
 */
public class MdExceptionDisallowSimultaneousExecution extends MdExceptionAbstract {

  /**
   * constructor.
   *
   * @param message message
   */
  public MdExceptionDisallowSimultaneousExecution(String message) {
    super(message);
  }

  @Override
  public ExitCode getExitCode() {
    return ExitCode.DISALLOW_SIMULTANEOUS_EXECUTION;
  }
}
