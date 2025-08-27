package com.walksocket.md;

import com.walksocket.md.exception.MdExceptionAbstract;

/**
 * app web.
 */
public class MdAppWeb implements AutoCloseable {

  /**
   * arg log path.
   */
  private static final String ARG_LOG_PATH = "--logPath=";

  /**
   * arg add seconds.
   */
  private static final String ARG_ADD_SECONDS = "--addSeconds=";

  /**
   * arg web host.
   */
  private static final String ARG_WEB_HOST = "--webHost=";

  /**
   * arg web port.
   */
  private static final String ARG_WEB_PORT = "--webPort=";

  /**
   * main.
   *
   * @param args args
   * @throws Exception error
   */
  public static void main(String[] args) throws Exception {
    // check args
    String logPath = null;
    int addSeconds = 60 * 60 * 9;
    String webHost = "0.0.0.0";
    int webPort = 8710;
    for (String arg : args) {
      if (arg.startsWith(ARG_LOG_PATH)) {
        logPath = arg.substring(ARG_LOG_PATH.length());
      }
      if (arg.startsWith(ARG_ADD_SECONDS)) {
        addSeconds = Integer.parseInt(arg.substring(ARG_ADD_SECONDS.length()));
      }
      if (arg.startsWith(ARG_WEB_HOST)) {
        webHost = arg.substring(ARG_WEB_HOST.length());
      }
      if (arg.startsWith(ARG_WEB_PORT)) {
        webPort = Integer.parseInt(arg.substring(ARG_WEB_PORT.length()));
      }
    }

    // init date
    MdDate.init(addSeconds);

    // execute
    MdExceptionAbstract.ExitCode exitCode = MdExceptionAbstract.ExitCode.ERROR;
    try (MdAppWeb ignored = new MdAppWeb()) {
      // set log
      MdLogger.open(logPath);
      MdLogger.trace(String.format(
          "(ENV) isDebug:%s, isPretty:%s, mdHome:%s",
          MdEnv.isDebug(),
          MdEnv.isPretty(),
          MdEnv.getMdHome()));
      MdLogger.trace(String.format("(ARGS) logPath:%s", logPath));
      MdLogger.trace(String.format("(ARGS) addSeconds:%s", addSeconds));

      // -----
      // web
      // -----
      MdLogger.trace(String.format("(ARGS) webHost:%s", webHost));
      MdLogger.trace(String.format("(ARGS) webPort:%s", webPort));

      // execute
      exitCode = execute(webHost, webPort);
    } catch (MdExceptionAbstract me) {
      exitCode = me.getExitCode();
      MdLogger.error(me);
    } catch (Exception e) {
      MdLogger.error(e);
    }

    System.exit(exitCode.getExitCode());
  }

  /**
   * execute.
   *
   * @param host host
   * @param port port
   * @return exit code
   * @throws Exception several error
   */
  public static MdExceptionAbstract.ExitCode execute(String host, int port) throws Exception {
    // start server
    MdExceptionAbstract.ExitCode exitCode = MdExceptionAbstract.ExitCode.SUCCESS;
    try (MdServer server = new MdServer(host, port)) {
      server.start();
      server.waitFor();
    }
    return exitCode;
  }

  @Override
  public void close() throws Exception {
    MdLogger.close();
  }
}
