package com.walksocket.md.exception;

/**
 * exception abstract.
 */
public abstract class MdExceptionAbstract extends Exception {

  /**
   * exit code enum.
   */
  public enum ExitCode {

    /**
     * success.
     */
    SUCCESS(0),

    /**
     * error.
     */
    ERROR(1),

    /**
     * invalid args.
     */
    INVALID_ARGS(11),

    /**
     * invalid stdin.
     */
    INVALID_STDIN(12),

    /**
     * invalid input.
     */
    INVALID_INPUT(13),

    /**
     * invalid version.
     */
    INVALID_VERSION(14),

    /**
     * disallow simultaneous execution.
     */
    DISALLOW_SIMULTANEOUS_EXECUTION(21),

    /**
     * in maintenance.
     */
    IN_MAINTENANCE(22),

    /**
     * no exists base or compare.
     */
    NO_EXISTS_BASE_OR_COMPARE(23),

    /**
     * no exists diff seqs.
     */
    NO_EXISTS_DIFF_SEQS(24),

    /**
     * not success code.
     */
    NOT_SUCCESS_CODE(31),

    /**
     * not success status.
     */
    NOT_SUCCESS_STATUS(32),

    /**
     * error local database.
     */
    ERROR_LOCAL_DATABASE(41),

    /**
     * unknown.
     */
    UNKNOWN(99)
    ;

    /**
     * exit code.
     */
    private int exitCode;

    /**
     * constructor.
     * @param exitCode exit code
     */
    ExitCode(int exitCode) {
      this.exitCode = exitCode;
    }

    /**
     * get exit code.
     * @return exit code.
     */
    public int getExitCode() {
      return exitCode;
    }
  }

  /**
   * constructor default.
   */
  public MdExceptionAbstract() {
    super();
  }

  /**
   * constructor.
   * @param message message
   */
  public MdExceptionAbstract(String message) {
    super(message);
  }

  /**
   * get exit code enum.
   * @return exit code enum
   */
  public abstract ExitCode getExitCode();
}
