package com.walksocket.md;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

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
  private static int addSeconds;

  /**
   * init.
   *
   * @param addSeconds add seconds
   */
  public static void init(int addSeconds) {
    MdDate.addSeconds = addSeconds;
  }

  /**
   * now.
   *
   * @return now
   */
  public static String now() {
    var dt = OffsetDateTime.now(ZoneOffset.ofTotalSeconds(addSeconds));
    return dt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));
  }

  /**
   * timestamp.
   *
   * @return timestamp
   */
  public static int timestamp() {
    Calendar cal = Calendar.getInstance();
    return (int) (cal.getTimeInMillis() / 1000);
  }
}
