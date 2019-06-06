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
   * magentadesk home diretory.
   */
  private static String mdHome = System.getProperty("user.home") + File.separator + ".magentadesk";

  static {
    String mdEnv = System.getenv("MD_ENV");
    if (mdEnv != null && mdEnv.equals("DEBUG")) {
      isDebug = true;
    }

    String mdOutput = System.getenv("MD_OUTPUT");
    if (mdOutput != null && mdOutput.equals("PRETTY")) {
      isPretty = true;
    }

    String mdHome = System.getenv("MD_HOME");
    if (!MdUtils.isNullOrEmpty(mdHome) && new File(mdHome).canWrite()) {
      MdEnv.mdHome = mdHome;
    }
    File f = new File(MdEnv.mdHome);
    if (!f.exists()) {
      f.mkdir();
    }
  }

  /**
   * is debug.
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
   * get magentadesk home diretory.
   * @return magentadesk home diretory
   */
  public static String getMdHome() {
    return mdHome;
  }
}
