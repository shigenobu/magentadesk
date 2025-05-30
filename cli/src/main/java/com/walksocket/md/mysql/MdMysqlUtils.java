package com.walksocket.md.mysql;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * mysql utils.
 */
public class MdMysqlUtils {

  /**
   * column type.
   */
  public enum MdMysqlColumnType {
    NUBMER,
    STRING,
    BINARY
  }

  /**
   * returning type.
   */
  public enum MdMysqlReturningType {
    FLOAT,
    DOUBLE,
    DECIMAL,
    SIGNED,
    UNSIGNED,
    DATE,
    TIME,
    DATETIME,
    YEAR,
    CHAR,
    JSON
  }

  /**
   * column type mapping.
   */
  private static final Map<String, MdMysqlColumnType> columnTypes = new HashMap<>();

  /**
   * returning type mapping.
   */
  private static final Map<String, MdMysqlReturningType> returningTypes = new HashMap<>();

  static {
    // columnTypes
    columnTypes.put("TINYINT", MdMysqlColumnType.NUBMER);
    columnTypes.put("SMALLINT", MdMysqlColumnType.NUBMER);
    columnTypes.put("MEDIUMINT", MdMysqlColumnType.NUBMER);
    columnTypes.put("INT", MdMysqlColumnType.NUBMER);
    columnTypes.put("BIGINT", MdMysqlColumnType.NUBMER);
    columnTypes.put("DECIMAL", MdMysqlColumnType.NUBMER);
    columnTypes.put("FLOAT", MdMysqlColumnType.NUBMER);
    columnTypes.put("DOUBLE", MdMysqlColumnType.NUBMER);
    columnTypes.put("BIT", MdMysqlColumnType.BINARY);

    columnTypes.put("CHAR", MdMysqlColumnType.STRING);
    columnTypes.put("VARCHAR", MdMysqlColumnType.STRING);
    columnTypes.put("TINYTEXT", MdMysqlColumnType.STRING);
    columnTypes.put("TEXT", MdMysqlColumnType.STRING);
    columnTypes.put("MEDIUMTEXT", MdMysqlColumnType.STRING);
    columnTypes.put("LONGTEXT", MdMysqlColumnType.STRING);
    columnTypes.put("ENUM", MdMysqlColumnType.STRING);
    columnTypes.put("SET", MdMysqlColumnType.STRING);
    columnTypes.put("JSON", MdMysqlColumnType.STRING);

    columnTypes.put("DATE", MdMysqlColumnType.STRING);
    columnTypes.put("TIME", MdMysqlColumnType.STRING);
    columnTypes.put("DATETIME", MdMysqlColumnType.STRING);
    columnTypes.put("TIMESTAMP", MdMysqlColumnType.STRING);
    columnTypes.put("YEAR", MdMysqlColumnType.STRING);

    columnTypes.put("BINARY", MdMysqlColumnType.BINARY);
    columnTypes.put("VARBINARY", MdMysqlColumnType.BINARY);
    columnTypes.put("TINYBLOB", MdMysqlColumnType.BINARY);
    columnTypes.put("BLOB", MdMysqlColumnType.BINARY);
    columnTypes.put("MEDIUMBLOB", MdMysqlColumnType.BINARY);
    columnTypes.put("LONGBLOB", MdMysqlColumnType.BINARY);

    // dynamicTypes
    returningTypes.put("TINYINT", MdMysqlReturningType.SIGNED);
    returningTypes.put("SMALLINT", MdMysqlReturningType.SIGNED);
    returningTypes.put("MEDIUMINT", MdMysqlReturningType.SIGNED);
    returningTypes.put("INT", MdMysqlReturningType.SIGNED);
    returningTypes.put("BIGINT", MdMysqlReturningType.SIGNED);
    returningTypes.put("DECIMAL", MdMysqlReturningType.DECIMAL);
    returningTypes.put("FLOAT", MdMysqlReturningType.FLOAT);
    returningTypes.put("DOUBLE", MdMysqlReturningType.DOUBLE);
    returningTypes.put("BIT", MdMysqlReturningType.CHAR);

    returningTypes.put("CHAR", MdMysqlReturningType.CHAR);
    returningTypes.put("VARCHAR", MdMysqlReturningType.CHAR);
    returningTypes.put("TINYTEXT", MdMysqlReturningType.CHAR);
    returningTypes.put("TEXT", MdMysqlReturningType.CHAR);
    returningTypes.put("MEDIUMTEXT", MdMysqlReturningType.CHAR);
    returningTypes.put("LONGTEXT", MdMysqlReturningType.CHAR);
    returningTypes.put("ENUM", MdMysqlReturningType.CHAR);
    returningTypes.put("SET", MdMysqlReturningType.CHAR);
    returningTypes.put("JSON", MdMysqlReturningType.CHAR);

    returningTypes.put("DATE", MdMysqlReturningType.DATE);
    returningTypes.put("TIME", MdMysqlReturningType.TIME);
    returningTypes.put("DATETIME", MdMysqlReturningType.DATETIME);
    returningTypes.put("TIMESTAMP", MdMysqlReturningType.DATETIME);
    returningTypes.put("YEAR", MdMysqlReturningType.YEAR);

    returningTypes.put("BINARY", MdMysqlReturningType.CHAR);
    returningTypes.put("VARBINARY", MdMysqlReturningType.CHAR);
    returningTypes.put("TINYBLOB", MdMysqlReturningType.CHAR);
    returningTypes.put("BLOB", MdMysqlReturningType.CHAR);
    returningTypes.put("MEDIUMBLOB", MdMysqlReturningType.CHAR);
    returningTypes.put("LONGBLOB", MdMysqlReturningType.CHAR);
  }

  /**
   * is binary.
   *
   * @param columnType column type
   * @return if binary, true
   * @throws SQLException sql error
   */
  public static boolean isBinary(String columnType) throws SQLException {
    columnType = columnType.toUpperCase();
    for (Map.Entry<String, MdMysqlColumnType> entry : columnTypes.entrySet()) {
      if (columnType.equalsIgnoreCase(entry.getKey())) {
        return entry.getValue() == MdMysqlColumnType.BINARY;
      }
    }
    for (Map.Entry<String, MdMysqlColumnType> entry : columnTypes.entrySet()) {
      if (columnType.startsWith(entry.getKey())) {
        return entry.getValue() == MdMysqlColumnType.BINARY;
      }
    }

    throw new SQLException(columnType + " is wrong.");
  }

  /**
   * get column type.
   *
   * @param columnType column type string
   * @return column type enum
   * @throws SQLException sql error
   */
  public static MdMysqlColumnType getColumnType(String columnType) throws SQLException {
    columnType = columnType.toUpperCase();
    for (Map.Entry<String, MdMysqlColumnType> entry : columnTypes.entrySet()) {
      if (columnType.equalsIgnoreCase(entry.getKey())) {
        return entry.getValue();
      }
    }
    for (Map.Entry<String, MdMysqlColumnType> entry : columnTypes.entrySet()) {
      if (columnType.startsWith(entry.getKey())) {
        return entry.getValue();
      }
    }

    throw new SQLException(columnType + " is wrong.");
  }

  /**
   * get returning type.
   *
   * @param columnType column type string
   * @return dynamic type enum
   * @throws SQLException sql error
   */
  public static MdMysqlReturningType getReturningType(String columnType) throws SQLException {
    columnType = columnType.toUpperCase();
    if ((
        columnType.startsWith("TINYINT")
            || columnType.startsWith("SMALLINT")
            || columnType.startsWith("MEDIUMINT")
            || columnType.startsWith("INT")
            || columnType.startsWith("BIGINT"))
        && columnType.contains("UNSIGNED")) {
      return MdMysqlReturningType.UNSIGNED;
    }
    for (Map.Entry<String, MdMysqlReturningType> entry : returningTypes.entrySet()) {
      if (columnType.equalsIgnoreCase(entry.getKey())) {
        return entry.getValue();
      }
    }
    for (Map.Entry<String, MdMysqlReturningType> entry : returningTypes.entrySet()) {
      if (columnType.startsWith(entry.getKey())) {
        return entry.getValue();
      }
    }

    throw new SQLException(columnType + " is wrong.");
  }
}
