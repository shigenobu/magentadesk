package com.walksocket.md.cli.mariadb;

import com.walksocket.md.cli.MdLogger;
import com.walksocket.md.cli.db.MdDbConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * mariadb connection.
 */
public class MdMariadbConnection extends MdDbConnection {

  /**
   * constructor.
   * @param connectionString connection string.
   */
  public MdMariadbConnection(String connectionString) {
    this.connectionString = connectionString;
  }

  /**
   * open.
   * @throws SQLException sql error
   */
  public void open() throws SQLException {
    if (isOpen()) {
      return;
    }

    MdLogger.sql(connectionString);
    try {
      Class.forName("org.mariadb.jdbc.Driver");
      con = DriverManager.getConnection(connectionString);
      con.setAutoCommit(false);
      con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
      MdLogger.sql("CONNECT:" + con);

    } catch (ClassNotFoundException e) {
      MdLogger.error(e);
    }

    return;
  }
}
