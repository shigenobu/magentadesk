package com.walksocket.md.exception;

/**
 * exception invalid input.
 */
public class MdExceptionInvalidInput extends MdExceptionAbstract {

  /**
   * constructor.
   *
   * @param message message
   */
  public MdExceptionInvalidInput(String message) {
    super(message);
  }

  @Override
  public ExitCode getExitCode() {
    return ExitCode.INVALID_INPUT;
  }
}
