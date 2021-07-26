package com.walksocket.md.input;

import com.google.gson.annotations.Expose;
import com.walksocket.md.MdUtils;
import com.walksocket.md.exception.MdExceptionInvalidInput;
import com.walksocket.md.mariadb.MdMariadbUtils;

/**
 * input abstract.
 */
public abstract class MdInputAbstract {

  /**
   * mode enum.
   */
  public enum Mode {

    /**
     * diff.
     */
    DIFF("diff"),

    /**
     * sync.
     */
    SYNC("sync"),

    /**
     * maintenance.
     */
    MAINTENANCE("maintenance"),
    ;

    /**
     * mode.
     */
    private String mode;

    /**
     * constructor.
     * @param mode mode
     */
    Mode(String mode) {
      this.mode = mode;
    }

    /**
     * get mode.
     * @return mode
     */
    public String getMode() {
      return mode;
    }
  }

  /**
   * host.
   */
  @Expose
  public String host;

  /**
   * port.
   */
  @Expose
  public int port;

  /**
   * user.
   */
  @Expose
  public String user;

  /**
   * pass.
   */
  @Expose
  public String pass;

  /**
   * charset.
   * <pre>
   *   utf8, utf8mb3 or utf8mb4
   * </pre>
   */
  @Expose
  public String charset;

  /**
   * get connection string.
   * @return connection string
   */
  public String getConnectionString() {
    return String.format(
        "jdbc:mariadb://%s:%s/information_schema" +
            "?user=%s" +
            "&password=%s" +
            "&useUnicode=true" +
            "&characterEncoding=utf8" +
            "&sessionVariables=character_set_client=%s,character_set_results=%s,character_set_connection=%s",
        host,
        port,
        user,
        pass,
        charset,
        charset,
        charset);
  }

  /**
   * validate.
   * @throws MdExceptionInvalidInput input error
   */
  public void validate() throws MdExceptionInvalidInput {
    if (MdUtils.isNullOrEmpty(host)) {
      throw new MdExceptionInvalidInput("Invalid host.");
    }
    if (port < 1024 || port > 65535) {
      throw new MdExceptionInvalidInput("Invalid port.");
    }
    if (MdUtils.isNullOrEmpty(user)) {
      throw new MdExceptionInvalidInput("Invalid user.");
    }
    if (MdUtils.isNullOrEmpty(pass)) {
      throw new MdExceptionInvalidInput("Invalid pass.");
    }
    if (MdUtils.isNullOrEmpty(charset)
      || !MdMariadbUtils.isValidCharset(charset)) {
      throw new MdExceptionInvalidInput("Invalid charset.");
    }
  }

  /**
   * get mode.
   * @return mode enum.
   */
  public abstract Mode getMode();
}
