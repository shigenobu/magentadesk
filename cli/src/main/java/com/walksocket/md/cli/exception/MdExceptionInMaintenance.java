package com.walksocket.md.cli.exception;

/**
 * exception in maintenance.
 */
public class MdExceptionInMaintenance extends MdExceptionAbstract {

  /**
   * constructor.
   * @param message message
   */
  public MdExceptionInMaintenance(String message) {
    super(message);
  }

  @Override
  public ExitCode getExitCode() {
    return ExitCode.IN_MAINTENANCE;
  }
}
