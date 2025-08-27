package com.walksocket.md;

import java.util.Arrays;
import org.apache.commons.lang3.Strings;

/**
 * db utils.
 */
public class MdDbUtils {

  /**
   * charset.
   */
  public enum MdDbCharset {
    UTF8,
    UTF8MB3,
    UTF8MB4,
  }

  /**
   * quote.
   *
   * @param src string
   * @return quoted string
   */
  public static String quote(String src) {
    return Strings.CS.replace(src, "'", "''");
  }

  /**
   * is valid charset.
   *
   * @param charset charset
   * @return if valid, true
   */
  public static boolean isValidCharset(String charset) {
    return Arrays.stream(MdDbCharset.values())
        .anyMatch(c -> c.toString().equalsIgnoreCase(charset));
  }

  /**
   * is valid collation.
   *
   * @param collation collation.
   * @return if valid, true
   */
  public static boolean isValidCollation(String collation) {
    String charset = collation.split("_")[0];
    return isValidCharset(charset);
  }
}
