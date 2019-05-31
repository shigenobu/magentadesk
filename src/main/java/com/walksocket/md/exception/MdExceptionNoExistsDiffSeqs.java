package com.walksocket.md.exception;

/**
 * exception no exists diff seqs.
 */
public class MdExceptionNoExistsDiffSeqs extends MdExceptionAbstract {

  @Override
  public ExitCode getExitCode() {
    return ExitCode.NO_EXISTS_DIFF_SEQS;
  }
}
