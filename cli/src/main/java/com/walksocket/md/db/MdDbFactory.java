package com.walksocket.md.db;

import com.walksocket.md.mariadb.MdMariadbConnection;
import com.walksocket.md.mysql.MdMysqlConnection;

/**
 * db factory.
 */
public class MdDbFactory {

  /**
   * jdbc prefix mariadb.
   */
  public static final String JDBC_PREFIX_MARIADB = "jdbc:mariadb://";

  /**
   * jdbc prefix mysql.
   */
  public static final String JDBC_PREFIX_MYSQL = "jdbc:mysql://";

  /**
   * new create.
   * @param connectionString connection string
   * @return db connection
   */
  public static MdDbConnection newCreate(String connectionString) {
    if (getDbType(connectionString) == DbType.MYSQL) {
      return new MdMysqlConnection(connectionString);
    }
    return new MdMariadbConnection(connectionString);
  }

  /**
   * get db type.
   * @param connectionString connection string
   * @return db type
   */
  public static DbType getDbType(String connectionString) {
    if (connectionString.startsWith(JDBC_PREFIX_MYSQL)) {
      return DbType.MYSQL;
    }
    return DbType.MARIADB;
  }

  /**
   * db type.
   */
  public enum DbType {

    /**
     * mariadb.
     */
    MARIADB("mariadb"),

    /**
     * mysql.
     */
    MYSQL("mysql"),
    ;

    /**
     * db type.
     */
    private String dbType;

    /**
     * constructor.
     * @param dbType db type
     */
    DbType(String dbType) {
      this.dbType = dbType;
    }

    /**
     * get db type.
     * @return db type
     */
    public String getDbType() {
      return dbType;
    }
  }
}
