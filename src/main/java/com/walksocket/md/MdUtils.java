package com.walksocket.md;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;

/**
 * utils.
 */
public class MdUtils {

  /**
   * is null or empty for string.
   * @param src string
   * @return if null or empty, true
   */
  public static boolean isNullOrEmpty(String src) {
    return src == null || src.equals("");
  }

  /**
   * is null or empty for collection.
   * @param src collection
   * @return if null or empty, true
   */
  public static boolean isNullOrEmpty(Collection src) {
    return src == null || src.size() == 0;
  }

  /**
   * get hash.
   * @param src base string
   * @return hashed string
   */
  public static String getHash(String src) {
    StringBuilder builder = new StringBuilder();
    byte[] data = new byte[0];
    try {
      data = MessageDigest.getInstance("MD5").digest(src.getBytes(StandardCharsets.UTF_8));
      for (byte d : data) {
        builder.append(String.format("%02x", d));
      }
    } catch (NoSuchAlgorithmException e) {
      MdLogger.error(e);
    }
    return builder.toString();
  }

  /**
   * is number.
   * @param src string
   * @return if number, true
   */
  public static boolean isNumber(String src) {
    return src.chars().allMatch(Character::isDigit);
  }

  /**
   * join list, to create string.
   * @param src list
   * @param delimiter devided string
   * @return joined string
   */
  public static String join(List<?> src, String delimiter) {
    return StringUtils.join(src, delimiter);
  }

  /**
   * get random string.
   * @return random string
   */
  public static String randomString() {
    return RandomStringUtils.randomAlphanumeric(32).toLowerCase();
  }

  /**
   * to hex string.
   * @param value string
   * @return hex string
   */
  public static String toHexString(String value) {
    byte[] data = value.getBytes(StandardCharsets.UTF_8);
    StringBuilder sb = new StringBuilder();
    for (byte d : data) {
      sb.append(String.format("%02X", d));
    }
    return sb.toString();
  }
}
