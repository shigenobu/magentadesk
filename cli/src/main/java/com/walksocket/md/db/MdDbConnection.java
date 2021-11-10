package com.walksocket.md.db;

import com.walksocket.md.MdLogger;
import com.walksocket.md.MdUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * db connection.
 */
abstract public class MdDbConnection implements AutoCloseable {

  /**
   * connection string.
   */
  protected String connectionString;

  /**
   * connection.
   */
  protected Connection con;

  /**
   * in transaction.
   */
  protected boolean inTransaction;

  /**
   * get connection string.
   * @return connection string
   */
  public String getConnectionString() {
    return connectionString;
  }

  /**
   * open.
   * @throws SQLException sql error
   */
  abstract public void open() throws SQLException;

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
  public List<MdDbRecord> getRecords(String sql) throws SQLException {
    List<MdDbRecord> records = new ArrayList<>();
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
        records.add(new MdDbRecord(record));
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

  /**
   * get record.
   * @param sql sql
   * @return record
   * @throws SQLException sql error
   */
  public MdDbRecord getRecord(String sql) throws SQLException {
    List<MdDbRecord> records = getRecords(sql);
    if (!MdUtils.isNullOrEmpty(records)) {
      return records.get(0);
    }
    return new MdDbRecord();
  }

  @Override
  public void close() throws Exception {
    if (con == null || con.isClosed()) {
      return;
    }
    rollback();
    con.close();
    MdLogger.sql("DISCONNECT:" + con);
  }
}
