package com.walksocket.md;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * date.
 */
public class MdDate {

  static {
    // default locale en
    Locale.setDefault(Locale.ENGLISH);
  }

  /**
   * add seconds for now.
   */
  private static long addSeconds;

  /**
   * init.
   * @param addSeconds add seconds
   */
  public static void init(long addSeconds) {
    MdDate.addSeconds = addSeconds;
  }

  /**
   * now.
   * @return now
   */
  public static String now() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
    Date date = new Date();
    date.setTime(date.getTime() + addSeconds * 1000);
    return sdf.format(date);
  }

  /**
   * timestamp.
   * @return timestamp
   */
  public static int timestamp() {
    Calendar cal = Calendar.getInstance();
    return (int) (cal.getTimeInMillis() / 1000);
  }
}
