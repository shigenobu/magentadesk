package com.walksocket.md.mariadb;

import com.walksocket.md.MdLogger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * mariadb connection.
 */
public class MdMariadbConnection implements AutoCloseable {

  /**
   * connection string.
   */
  private String connectionStrinng;

  /**
   * connection.
   */
  private Connection con;

  /**
   * in transaction.
   */
  private boolean inTransaction;

  /**
   * constructor.
   * @param connectionStrinng connection string.
   */
  public MdMariadbConnection(String connectionStrinng) {
    this.connectionStrinng = connectionStrinng;
  }

  /**
   * open.
   * @throws SQLException sql error
   */
  public void open() throws SQLException {
    if (isOpen()) {
      return;
    }

    MdLogger.sql(connectionStrinng);
    try {
      Class.forName("org.mariadb.jdbc.Driver");
      con = DriverManager.getConnection(connectionStrinng);
      con.setAutoCommit(false);
      con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
      con.setReadOnly(true);
      MdLogger.sql(con);

    } catch (ClassNotFoundException e) {
      MdLogger.error(e);
    }

    return;
  }

  /**
   * is open.
   * @return if opend, true
   * @throws SQLException sql error
   */
  public boolean isOpen() throws SQLException {
    if (con != null && !con.isClosed()) {
      return true;
    }
    return false;
  }

  /**
   * begin.
   * @throws SQLException sql error
   */
  public void begin() throws SQLException {
    open();
    MdLogger.sql("BEGIN");
    inTransaction = true;
  }

  /**
   * commit.
   * @throws SQLException sql error
   */
  public void commit() throws SQLException {
    if (isOpen() && inTransaction) {
      con.commit();
      MdLogger.sql("COMMIT");
      inTransaction = false;
    }
  }

  /**
   * rollback.
   */
  public void rollback() {
    try {
      if (isOpen() && inTransaction) {
        con.rollback();
        MdLogger.sql("ROLLBACK");
        inTransaction = false;
      }
    } catch (SQLException e) {
      MdLogger.error(e);
    }
  }

  /**
   * execute.
   * @param sql sql
   * @throws SQLException sql error
   */
  public void execute(String sql) throws SQLException {
    Statement stmt = null;
    try {
      open();
      stmt = con.createStatement();
      stmt.execute(sql);
      MdLogger.sql(sql);
    } catch (Exception e) {
      MdLogger.error(sql);
      throw e;
    } finally {
      if (stmt != null) {
        try {
          stmt.close();
        } catch (Exception e) {
          MdLogger.error(e);
        }
      }
    }
  }

  /**
   * get records.
   * @param sql sql
   * @return records
   * @throws SQLException sql error
   */
  public List<MdMariadbRecord> getRecords(String sql) throws SQLException {
    List<MdMariadbRecord> records = new ArrayList<>();
    Statement stmt = null;
    ResultSet rs = null;
    try {
      open();
      stmt = con.createStatement();
      rs = stmt.executeQuery(sql);
      MdLogger.sql(sql);
      while (rs.next()) {
        ResultSetMetaData meta = rs.getMetaData();
        List<Map<String, String>> record = new ArrayList<>();
        for (int i = 1; i <= meta.getColumnCount(); i++) {
          String columnName = meta.getColumnName(i).toLowerCase();
          Map<String, String> pair = new HashMap<>();
          pair.put(columnName, rs.getString(i));
          record.add(pair);
        }
        records.add(new MdMariadbRecord(record));
      }
    } catch (Exception e) {
      MdLogger.error(sql);
      throw e;
    } finally {
      if (rs != null) {
        try {
          rs.close();
        } catch (Exception e) {
          MdLogger.error(e);
        }
      }
      if (stmt != null) {
        try {
          stmt.close();
        } catch (Exception e) {
          MdLogger.error(e);
        }
      }
    }

    return records;
  }

  @Override
  public void close() throws Exception {
    if (con == null || con.isClosed()) {
      return;
    }
    con.close();
  }
}
