package com.walksocket.md;

import com.walksocket.md.exception.MdExceptionAbstract;
import com.walksocket.md.input.MdInputAbstract;
import com.walksocket.md.input.MdInputDiff;
import com.walksocket.md.input.MdInputMaintenance;
import com.walksocket.md.input.MdInputSync;
import com.walksocket.md.output.MdOutputAbstract;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * app.
 */
public class MdApp implements AutoCloseable {

  /**
   * arg mode.
   */
  private static final String ARG_MODE = "--mode=";

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
   * @param args args
   * @throws Exception error
   */
  public static void main(String args[]) throws Exception {
    // if no args, show help
    if (args.length == 0) {
      help();
      System.exit(MdExceptionAbstract.ExitCode.SUCCESS.getExitCode());
    }

    // check args
    String mode = null;
    String logPath = null;
    long addSeconds  = 60 * 60 * 9;
    String webHost = "0.0.0.0";
    int webPort = 8710;
    for (String arg : args) {
      if (arg.startsWith(ARG_MODE)) {
        mode = arg.substring(ARG_MODE.length());
      }
      if (arg.startsWith(ARG_LOG_PATH)) {
        logPath = arg.substring(ARG_LOG_PATH.length());
      }
      if (arg.startsWith(ARG_ADD_SECONDS)) {
        addSeconds = Long.parseLong(arg.substring(ARG_ADD_SECONDS.length()));
      }
      if (arg.startsWith(ARG_WEB_HOST)) {
        webHost = arg.substring(ARG_WEB_HOST.length());
      }
      if (arg.startsWith(ARG_WEB_PORT)) {
        webPort = Integer.parseInt(arg.substring(ARG_WEB_PORT.length()));
      }
    }
    if (mode == null) {
      System.err.println("Arg 'mode' is required.");
      System.exit(MdExceptionAbstract.ExitCode.INVALID_ARGS.getExitCode());
    }
    String finalMode = mode;
    Optional<MdMode> opt = Arrays.asList(MdMode.values())
        .stream()
        .filter(elem -> finalMode.compareTo(elem.getMode()) == 0)
        .findFirst();
    if (!opt.isPresent()) {
      System.err.println("Arg 'mode' is not defined.");
      System.exit(MdExceptionAbstract.ExitCode.INVALID_ARGS.getExitCode());
    }

    // init date
    MdDate.init(addSeconds);

    // execute
    MdExceptionAbstract.ExitCode exitCode = MdExceptionAbstract.ExitCode.ERROR;
    try (MdApp app = new MdApp()) {
      // set log
      MdLogger.open(logPath);
      MdLogger.trace(String.format(
          "(ENV) isDebug:%s, isPretty:%s, mdHome:%s",
          MdEnv.isDebug(),
          MdEnv.isPretty(),
          MdEnv.getMdHome()));
      MdLogger.trace(String.format("(ARGS) mode:%s", mode));
      MdLogger.trace(String.format("(ARGS) logPath:%s", logPath));
      MdLogger.trace(String.format("(ARGS) addSeconds:%s", addSeconds));

      if (mode.equals(MdMode.WEB.getMode())) {
        // -----
        // web
        // -----
        MdLogger.trace(String.format("(ARGS) webHost:%s", webHost));
        MdLogger.trace(String.format("(ARGS) webPort:%s", webPort));

        // execute
        exitCode = MdAppWeb.execute(webHost, webPort);
      } else {
        // -----
        // cli(diff,sync,maintenance)
        // -----
        // read stdin
        String json = MdFile.readString(System.in);
        if (MdUtils.isNullOrEmpty(json)) {
          System.err.println("Stdin json is required.");
          System.exit(MdExceptionAbstract.ExitCode.INVALID_STDIN.getExitCode());
        }
        MdLogger.trace(String.format("(STDIN) json:%s", json));

        // execute
        exitCode = MdAppCli.execute(mode, json);
      }
    } catch (MdExceptionAbstract me) {
      exitCode = me.getExitCode();
      MdLogger.error(me);
    } catch (Exception e) {
      MdLogger.error(e);
    }

    System.exit(exitCode.getExitCode());
  }

  /**
   * help.
   * @throws IOException read error
   */
  private static void help() throws IOException {
    InputStream in = MdApp.class.getClassLoader().getResourceAsStream("help.txt");
    String help = MdFile.readString(in);
    System.out.print(help);
  }

  @Override
  public void close() throws Exception {
    MdLogger.close();
  }
}
