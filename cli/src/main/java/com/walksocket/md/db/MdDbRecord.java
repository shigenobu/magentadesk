package com.walksocket.md.db;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * db record.
 */
public class MdDbRecord {

  /**
   * create empty record.
   *
   * @return db record
   */
  public static MdDbRecord createEmptyRecord() {
    return new MdDbRecord();
  }

  /**
   * params.
   */
  private final Map<String, String> params = new LinkedHashMap<>();

  /**
   * constructor.
   */
  private MdDbRecord() {
  }

  /**
   * constructor for record.
   *
   * @param record record
   */
  public MdDbRecord(List<Map<String, String>> record) {
    record.forEach(pair -> {
      pair.forEach((columnName, columnValue) -> {
        params.put(columnName.toLowerCase(), columnValue);
      });
    });
  }

  /**
   * get value.
   *
   * @param columnName column name
   * @return column value
   * @throws SQLException no column error
   */
  public String get(String columnName) throws SQLException {
    String colName = columnName.toLowerCase();
    if (params.containsKey(colName)) {
      return params.get(colName);
    }
    throw new SQLException(columnName + " value is not exist !");
  }

  /**
   * get value when not exists, return empty.
   *
   * @param columnName column name
   * @return column value or empty
   */
  public String getOrEmpty(String columnName) {
    return getOrDefault(columnName, "");
  }

  /**
   * get value when not exists, return alternative.
   *
   * @param columnName  column name
   * @param alternative alternative
   * @return column value or alternative
   */
  public String getOrDefault(String columnName, String alternative) {
    String colName = columnName.toLowerCase();
    if (params.containsKey(colName)) {
      return params.get(colName);
    }
    return alternative;
  }

  /**
   * get all.
   *
   * @return all params
   */
  public Map<String, String> getAll() {
    return params;
  }
}
