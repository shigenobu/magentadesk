package com.walksocket.md.sqlite;

import org.apache.commons.lang3.Strings;

/**
 * sqlite utils.
 */
public class MdSqliteUtils {

  /**
   * quote.
   *
   * @param src string
   * @return quoted string
   */
  public static String quote(String src) {
    return Strings.CS.replace(src, "'", "''");
  }
}
