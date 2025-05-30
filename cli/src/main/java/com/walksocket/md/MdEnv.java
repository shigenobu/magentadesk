package com.walksocket.md;

import java.io.File;

/**
 * env.
 */
public class MdEnv {

  /**
   * is debug.
   */
  private static boolean isDebug = false;

  /**
   * is pretty.
   */
  private static boolean isPretty = false;

  /**
   * limit length.
   */
  private static int limitLength = 10000;

  /**
   * limit mismatch count.
   */
  private static int limitMismatchCount = 10000;

  /**
   * magentadesk home directory.
   */
  private static String mdHome = System.getProperty("user.home") + File.separator + ".magentadesk";

  /**
   * is wait.
   */
  private static boolean isWait = false;

  static {
    String mdEnv = System.getenv("MD_ENV");
    if (mdEnv != null && mdEnv.equals("DEBUG")) {
      isDebug = true;
    }

    String mdOutput = System.getenv("MD_OUTPUT");
    if (mdOutput != null && mdOutput.equals("PRETTY")) {
      isPretty = true;
    }

    String mdLimitLength = System.getenv("MD_LIMIT_LENGTH");
    if (mdLimitLength != null && MdUtils.isNumber(mdLimitLength)) {
      limitLength = Integer.parseInt(mdLimitLength);
    }

    String mdLimitMismatchCount = System.getenv("MD_LIMIT_MISMATCH_COUNT");
    if (mdLimitMismatchCount != null && MdUtils.isNumber(mdLimitMismatchCount)) {
      limitMismatchCount = Integer.parseInt(mdLimitMismatchCount);
    }

    String mdHome = System.getenv("MD_HOME");
    if (!MdUtils.isNullOrEmpty(mdHome) && new File(mdHome).canWrite()) {
      MdEnv.mdHome = mdHome;
    }
    File f = new File(MdEnv.mdHome);
    if (!f.exists()) {
      f.mkdir();
    }

    String mdWait = System.getenv("MD_WAIT");
    if (mdWait != null && mdWait.equals("WAIT")) {
      isWait = true;
    }
  }

  /**
   * is debug.
   *
   * @return if debug, true
   */
  public static boolean isDebug() {
    return isDebug;
  }

  /**
   * set debug.
   */
  public static void setDebug() {
    isDebug = true;
  }

  /**
   * is pretty.
   *
   * @return if pretty, true
   */
  public static boolean isPretty() {
    return isPretty;
  }

  /**
   * set pretty.
   */
  public static void setPretty() {
    isPretty = true;
  }

  /**
   * get limit length.
   *
   * @return limit length.
   */
  public static int getLimitLength() {
    return limitLength;
  }

  /**
   * get limit mismatch count.
   *
   * @return limit mismatch count.
   */
  public static int getLimitMismatchCount() {
    return limitMismatchCount;
  }

  /**
   * set limit length.
   *
   * @param limitLength limit length
   */
  public static void setLimitLength(int limitLength) {
    MdEnv.limitLength = limitLength;
  }

  /**
   * set limit mismatch count.
   *
   * @param limitMismatchCount limit mismatch count
   */
  public static void setLimitMismatchCount(int limitMismatchCount) {
    MdEnv.limitMismatchCount = limitMismatchCount;
  }

  /**
   * get magentadesk home directory.
   *
   * @return magentadesk home directory
   */
  public static String getMdHome() {
    return mdHome;
  }

  /**
   * set is wait.
   *
   * @param isWait is wait
   */
  public static void setIsWait(boolean isWait) {
    MdEnv.isWait = isWait;
  }

  /**
   * is wait.
   *
   * @return lock wait is true, default is false.
   */
  public static boolean isWait() {
    return isWait;
  }
}
