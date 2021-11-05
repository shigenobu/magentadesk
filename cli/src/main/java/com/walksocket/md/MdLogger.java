package com.walksocket.md;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.function.Supplier;

/**
 * logger.
 */
public class MdLogger {

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
   * trace logger.
   * @param message message
   */
  public static void trace(Supplier<Object> message) {
    if (MdEnv.isDebug()) {
      out("TRACE", message.get());
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
      String msg = "[" + MdDate.now() + "][" + String.format("%010d", Thread.currentThread().getId()) + "][" + level + "]" + message.toString();
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
}
