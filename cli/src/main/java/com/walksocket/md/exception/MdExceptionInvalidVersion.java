package com.walksocket.md.exception;

/**
 * exception invalid version.
 */
public class MdExceptionInvalidVersion extends MdExceptionAbstract {

  /**
   * constructor.
   * @param message message
   */
  public MdExceptionInvalidVersion(String message) {
    super(message);
  }

  @Override
  public ExitCode getExitCode() {
    return ExitCode.INVALID_VERSION;
  }
}
