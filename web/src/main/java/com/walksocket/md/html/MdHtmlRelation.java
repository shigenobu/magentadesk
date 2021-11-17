package com.walksocket.md.html;

import java.util.List;
import java.util.stream.Collectors;

/**
 * html relation.
 */
public class MdHtmlRelation {

  /**
   * table.
   */
  public String table;

  /**
   * foreignKeys.
   */
  public List<String> foreignKeys;

  /**
   * referenceTable.
   */
  public String referenceTable;

  /**
   * referenceKeys.
   */
  public List<String> referenceKeys;

  /**
   * constructor.
   * @param table table
   * @param foreignKeys foreignKeys
   * @param referenceTable referenceTable
   * @param referenceKeys referenceKeys
   */
  public MdHtmlRelation(String table, List<String> foreignKeys, String referenceTable, List<String> referenceKeys) {
    this.table = table;
    this.foreignKeys = foreignKeys;
    this.referenceTable = referenceTable;
    this.referenceKeys = referenceKeys;
  }

  /**
   * get parent to child relation expression.
   * @return relation expression
   */
  public String getP2CRelation() {
    return String.format(
        "%s.%s => %s.%s",
        referenceTable,
        referenceKeys.stream().collect(Collectors.joining(",")),
        table,
        foreignKeys.stream().collect(Collectors.joining(",")));
  }

  @Override
  public String toString() {
    return "MdHtmlRelation{" +
        "table='" + table + '\'' +
        ", foreignKeys=" + foreignKeys +
        ", referenceTable='" + referenceTable + '\'' +
        ", referenceKeys=" + referenceKeys +
        '}';
  }
}
