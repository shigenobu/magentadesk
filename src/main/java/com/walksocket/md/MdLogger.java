package com.walksocket.md;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * logger.
 */
public class MdLogger {

  static {
    // default locale en
    Locale.setDefault(Locale.ENGLISH);
  }

  /**
   * add seconds for now.
   */
  private static long addSeconds;

  /**
   * stdout pattern.
   */
  private static final String LOG_STDOUT = "STDOUT";

  /**
   * stderr pattern.
   */
  private static final String LOG_STDERR = "STDERR";

  /**
   * log writer.
   */
  private static OutputStreamWriter writer;

  /**
   * set add seconds.
   * @param addSeconds add seconds
   */
  public static void setAddSeconds(long addSeconds) {
    MdLogger.addSeconds = addSeconds;
  }

  /**
   * open logger.
   * @param logPath log path
   * @throws IOException no file error
   */
  public static void open(String logPath) throws IOException {
    if (MdUtils.isNullOrEmpty(logPath)) {
      return;
    }
    if (writer == null) {
      if (logPath.equalsIgnoreCase(LOG_STDOUT)) {
        // stdout
        writer = new OutputStreamWriter(System.out);
      } else if (logPath.equalsIgnoreCase(LOG_STDERR)) {
        // stderr
        writer = new OutputStreamWriter(System.err);
      } else {
        // file
        writer = new FileWriter(logPath, true);
      }
    }
  }

  /**
   * close.
   * @throws IOException close error
   */
  public static void close() throws IOException {
    if (writer != null) {
      writer.close();
    }
  }

  /**
   * sql logger.
   * @param message message
   */
  public static void sql(Object message) {
    if (MdEnv.isDebug()) {
      out("SQL", message);
    }
  }

  /**
   * trace logger.
   * @param message message
   */
  public static void trace(Object message) {
    if (MdEnv.isDebug()) {
      out("TRACE", message);
    }
  }

  /**
   * error logger.
   * @param message message
   */
  public static void error(Object message) {
    out("ERROR", message);
  }

  /**
   * out log.
   * @param level level
   * @param message message
   */
  private static void out(String level, Object message) {
    if (writer == null) {
      return;
    }

    try {
      String msg = "[" + now() + "][" + level + "]" + message.toString();
      writer.write(msg + "\n");
      writer.flush();

      if (message instanceof Throwable) {
        StackTraceElement[] stacks = ((Throwable) message).getStackTrace();
        StringBuffer buffer = new StringBuffer();
        for (StackTraceElement stack : stacks) {
          buffer.append("\n");
          buffer.append("<" + stack.getClassName() + ">");
          buffer.append("<" + stack.getFileName() + ">");
          buffer.append("<" + stack.getLineNumber() + ">");
          buffer.append("<" + stack.getMethodName() + ">");
        }
        writer.write(buffer.toString() + "\n");
        writer.flush();
      }
    } catch (Exception e) {
    }
  }

  /**
   * now.
   * @return now
   */
  private static String now() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
    Date date = new Date();
    date.setTime(date.getTime() + addSeconds * 1000);
    return sdf.format(date);
  }
}
