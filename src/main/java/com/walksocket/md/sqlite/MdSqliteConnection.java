package com.walksocket.md.sqlite;

import com.walksocket.md.MdEnv;
import com.walksocket.md.MdLogger;
import com.walksocket.md.db.MdDbConnection;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * sqlite connection.
 */
public class MdSqliteConnection extends MdDbConnection {

  /**
   * constructor.
   */
  public MdSqliteConnection() {
    this.connectionString = "jdbc:sqlite:" + new File(MdEnv.getMdHome(), "magentadesk.db").getAbsolutePath();
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
      Class.forName("org.sqlite.JDBC");
      con = DriverManager.getConnection(connectionString);
      con.setAutoCommit(false);
      con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
      MdLogger.sql("CONNECT:" + con);

    } catch (ClassNotFoundException e) {
      MdLogger.error(e);
    }

    return;
  }
}
