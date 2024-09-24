package com.walksocket.md.mariadb;

import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * mariadb utils.
 */
public class MdMariadbUtils {

  /**
   * column type.
   */
  public enum MdMariadbColumnType {
    NUBMER,
    STRING,
    BINARY
    ;
  }

  /**
   * dynamic type.
   */
  public enum MdMariadbDynamicType {
    BINARY,
    CHAR,
    DATE,
    DATETIME,
    DECIMAL,
    DOUBLE,
    INTEGER,
    TIME,
    UNSIGNED
    ;
  }

  /**
   * column type mapping.
   */
  private static Map<String, MdMariadbColumnType> columnTypes = new HashMap<>();

  /**
   * dynamic type mapping.
   */
  private static Map<String, MdMariadbDynamicType> dynamicTypes = new HashMap<>();

  static {
    // columnTypes
    columnTypes.put("TINYINT", MdMariadbColumnType.NUBMER);
    columnTypes.put("SMALLINT", MdMariadbColumnType.NUBMER);
    columnTypes.put("MEDIUMINT", MdMariadbColumnType.NUBMER);
    columnTypes.put("INT", MdMariadbColumnType.NUBMER);
    columnTypes.put("BIGINT", MdMariadbColumnType.NUBMER);
    columnTypes.put("DECIMAL", MdMariadbColumnType.NUBMER);
    columnTypes.put("FLOAT", MdMariadbColumnType.NUBMER);
    columnTypes.put("DOUBLE", MdMariadbColumnType.NUBMER);
    columnTypes.put("BIT", MdMariadbColumnType.NUBMER);

    columnTypes.put("CHAR", MdMariadbColumnType.STRING);
    columnTypes.put("VARCHAR", MdMariadbColumnType.STRING);
    columnTypes.put("TINYTEXT", MdMariadbColumnType.STRING);
    columnTypes.put("TEXT", MdMariadbColumnType.STRING);
    columnTypes.put("MEDIUMTEXT", MdMariadbColumnType.STRING);
    columnTypes.put("LONGTEXT", MdMariadbColumnType.STRING);
    columnTypes.put("ENUM", MdMariadbColumnType.STRING);
    columnTypes.put("SET", MdMariadbColumnType.STRING);
    columnTypes.put("JSON", MdMariadbColumnType.STRING);
    columnTypes.put("INET4", MdMariadbColumnType.STRING);
    columnTypes.put("INET6", MdMariadbColumnType.STRING);
    columnTypes.put("UUID", MdMariadbColumnType.STRING);

    columnTypes.put("DATE", MdMariadbColumnType.STRING);
    columnTypes.put("TIME", MdMariadbColumnType.STRING);
    columnTypes.put("DATETIME", MdMariadbColumnType.STRING);
    columnTypes.put("TIMESTAMP", MdMariadbColumnType.STRING);
    columnTypes.put("YEAR", MdMariadbColumnType.STRING);

    columnTypes.put("BINARY", MdMariadbColumnType.BINARY);
    columnTypes.put("VARBINARY", MdMariadbColumnType.BINARY);
    columnTypes.put("TINYBLOB", MdMariadbColumnType.BINARY);
    columnTypes.put("BLOB", MdMariadbColumnType.BINARY);
    columnTypes.put("MEDIUMBLOB", MdMariadbColumnType.BINARY);
    columnTypes.put("LONGBLOB", MdMariadbColumnType.BINARY);

    // dynamicTypes
    dynamicTypes.put("TINYINT", MdMariadbDynamicType.INTEGER);
    dynamicTypes.put("SMALLINT", MdMariadbDynamicType.INTEGER);
    dynamicTypes.put("MEDIUMINT", MdMariadbDynamicType.INTEGER);
    dynamicTypes.put("INT", MdMariadbDynamicType.INTEGER);
    dynamicTypes.put("BIGINT", MdMariadbDynamicType.INTEGER);
    dynamicTypes.put("DECIMAL", MdMariadbDynamicType.DECIMAL);
    dynamicTypes.put("FLOAT", MdMariadbDynamicType.DOUBLE);
    dynamicTypes.put("DOUBLE", MdMariadbDynamicType.DOUBLE);
    dynamicTypes.put("BIT", MdMariadbDynamicType.INTEGER);

    dynamicTypes.put("CHAR", MdMariadbDynamicType.CHAR);
    dynamicTypes.put("VARCHAR", MdMariadbDynamicType.CHAR);
    dynamicTypes.put("TINYTEXT", MdMariadbDynamicType.CHAR);
    dynamicTypes.put("TEXT", MdMariadbDynamicType.CHAR);
    dynamicTypes.put("MEDIUMTEXT", MdMariadbDynamicType.CHAR);
    dynamicTypes.put("LONGTEXT", MdMariadbDynamicType.CHAR);
    dynamicTypes.put("ENUM", MdMariadbDynamicType.CHAR);
    dynamicTypes.put("SET", MdMariadbDynamicType.CHAR);
    dynamicTypes.put("JSON", MdMariadbDynamicType.CHAR);
    dynamicTypes.put("INET4", MdMariadbDynamicType.CHAR);
    dynamicTypes.put("INET6", MdMariadbDynamicType.CHAR);
    dynamicTypes.put("UUID", MdMariadbDynamicType.CHAR);

    dynamicTypes.put("DATE", MdMariadbDynamicType.DATE);
    dynamicTypes.put("TIME", MdMariadbDynamicType.TIME);
    dynamicTypes.put("DATETIME", MdMariadbDynamicType.DATETIME);
    dynamicTypes.put("TIMESTAMP", MdMariadbDynamicType.DATETIME);
    dynamicTypes.put("YEAR", MdMariadbDynamicType.UNSIGNED);

    dynamicTypes.put("BINARY", MdMariadbDynamicType.BINARY);
    dynamicTypes.put("VARBINARY", MdMariadbDynamicType.BINARY);
    dynamicTypes.put("TINYBLOB", MdMariadbDynamicType.BINARY);
    dynamicTypes.put("BLOB", MdMariadbDynamicType.BINARY);
    dynamicTypes.put("MEDIUMBLOB", MdMariadbDynamicType.BINARY);
    dynamicTypes.put("LONGBLOB", MdMariadbDynamicType.BINARY);
  }

  /**
   * get column type.
   * @param columnType column type string
   * @return column type enum
   * @exception SQLException sql error
   */
  public static MdMariadbColumnType getColumnType(String columnType) throws SQLException {
    columnType = columnType.toUpperCase();
    for (Map.Entry<String, MdMariadbColumnType> entry : columnTypes.entrySet()) {
      if (columnType.equalsIgnoreCase(entry.getKey())) {
        return entry.getValue();
      }
    }
    for (Map.Entry<String, MdMariadbColumnType> entry : columnTypes.entrySet()) {
      if (columnType.startsWith(entry.getKey())) {
        return entry.getValue();
      }
    }

    throw new SQLException(columnType + " is wrong.");
  }

  /**
   * get dynamic type.
   * @param columnType column type string
   * @return dynamic type enum
   * @exception SQLException sql error
   */
  public static MdMariadbDynamicType getDynamicType(String columnType) throws SQLException {
    columnType = columnType.toUpperCase();
    if ((
        columnType.startsWith("TINYINT")
          || columnType.startsWith("SMALLINT")
          || columnType.startsWith("MEDIUMINT")
          || columnType.startsWith("INT")
          || columnType.startsWith("BIGINT"))
        && columnType.contains("UNSIGNED")) {
      return MdMariadbDynamicType.UNSIGNED;
    }
    for (Map.Entry<String, MdMariadbDynamicType> entry : dynamicTypes.entrySet()) {
      if (columnType.equalsIgnoreCase(entry.getKey())) {
        return entry.getValue();
      }
    }
    for (Map.Entry<String, MdMariadbDynamicType> entry : dynamicTypes.entrySet()) {
      if (columnType.startsWith(entry.getKey())) {
        return entry.getValue();
      }
    }

    throw new SQLException(columnType + " is wrong.");
  }
}
