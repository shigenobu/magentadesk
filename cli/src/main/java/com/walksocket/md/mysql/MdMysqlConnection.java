package com.walksocket.md.mysql;

import com.walksocket.md.MdLogger;
import com.walksocket.md.db.MdDbConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * mysql connection.
 */
public class MdMysqlConnection extends MdDbConnection {

  /**
   * constructor.
   *
   * @param connectionString connection string.
   */
  public MdMysqlConnection(String connectionString) {
    this.connectionString = connectionString;
  }

  /**
   * open.
   *
   * @throws SQLException sql error
   */
  @Override
  public void open() throws SQLException {
    if (isOpen()) {
      return;
    }

    MdLogger.sql(connectionString);
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      con = DriverManager.getConnection(connectionString);
      con.setAutoCommit(false);
      con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
      MdLogger.sql("CONNECT:" + con);

    } catch (ClassNotFoundException e) {
      MdLogger.error(e);
    }

  }
}
