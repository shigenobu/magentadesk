package com.walksocket.md.cli.sqlite;

import org.apache.commons.lang3.StringUtils;

/**
 * sqlite utils.
 */
public class MdSqliteUtils {

  /**
   * quote.
   * @param src string
   * @return quoted string
   */
  public static String quote(String src) {
    return StringUtils.replace(src, "'", "''");
  }
}
