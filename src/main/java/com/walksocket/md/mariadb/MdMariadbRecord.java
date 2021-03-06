package com.walksocket.md.mariadb;

import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * mariadb record.
 */
public class MdMariadbRecord {

  /**
   * params.
   */
  private Map<String, String> params = new LinkedHashMap<>();

  /**
   * constructor.
   * @param record record
   */
  public MdMariadbRecord(List<Map<String, String>> record) {
    record.forEach(pair -> {
      pair.forEach((columnName, columnValue) -> {
        params.put(columnName.toLowerCase(), columnValue);
      });
    });
  }

  /**
   * get value.
   * @param columnName columu name.
   * @return column value.
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
   * get all.
   * @return all params
   */
  public Map<String, String> getAll() {
    return params;
  }
}
