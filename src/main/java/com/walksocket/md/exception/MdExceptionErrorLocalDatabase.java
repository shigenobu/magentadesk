package com.walksocket.md.exception;

/**
 * exception error local database.
 */
public class MdExceptionErrorLocalDatabase extends MdExceptionAbstract {

  @Override
  public ExitCode getExitCode() {
    return ExitCode.ERROR_LOCAL_DATABASE;
  }
}
