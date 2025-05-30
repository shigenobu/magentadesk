package com.walksocket.md.input;

import com.google.gson.annotations.Expose;
import com.walksocket.md.MdDbUtils;
import com.walksocket.md.MdMode;
import com.walksocket.md.MdUtils;
import com.walksocket.md.MdValue;
import com.walksocket.md.db.MdDbFactory;
import com.walksocket.md.db.MdDbFactory.DbType;
import com.walksocket.md.exception.MdExceptionInvalidInput;

/**
 * input abstract.
 */
public abstract class MdInputAbstract extends MdValue {

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
   * mariadb or mysql
   */
  @Expose
  public String dbType = DbType.MARIADB.getDbType();

  /**
   * get connection string.
   *
   * @return connection string
   */
  public String getConnectionString() {
    if (dbType.equalsIgnoreCase(MdDbFactory.DbType.MYSQL.getDbType())) {
      return String.format(
          "%s%s:%s/information_schema" +
              "?user=%s" +
              "&password=%s" +
              "&useUnicode=true" +
              "&characterEncoding=utf8" +
              "&sessionVariables=character_set_client=%s,character_set_results=%s,character_set_connection=%s",
          MdDbFactory.JDBC_PREFIX_MYSQL,
          host,
          port,
          user,
          pass,
          charset,
          charset,
          charset);
    }
    return String.format(
        "%s%s:%s/information_schema" +
            "?user=%s" +
            "&password=%s" +
            "&useUnicode=true" +
            "&characterEncoding=utf8" +
            "&sessionVariables=character_set_client=%s,character_set_results=%s,character_set_connection=%s",
        MdDbFactory.JDBC_PREFIX_MARIADB,
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
   *
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
        || !MdDbUtils.isValidCharset(charset)) {
      throw new MdExceptionInvalidInput("Invalid charset.");
    }
    if (!dbType.equalsIgnoreCase(MdDbFactory.DbType.MARIADB.getDbType())
        && !dbType.equalsIgnoreCase(MdDbFactory.DbType.MYSQL.getDbType())) {
      throw new MdExceptionInvalidInput("Invalid dbType.");
    }
  }

  /**
   * get mode.
   *
   * @return mode enum.
   */
  public abstract MdMode getMode();
}
