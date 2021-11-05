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
 * app cli.
 */
public class MdAppCli implements AutoCloseable {

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
    try (MdAppCli app = new MdAppCli()) {
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
      exitCode = execute(mode, json);

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
   * @param mode mode
   * @param json json
   * @return exit code
   * @throws Exception several error
   */
  public static MdExceptionAbstract.ExitCode execute(String mode, String json) throws Exception {
    MdExceptionAbstract.ExitCode exitCode;
    MdOutputAbstract output = null;

    // start
    long start = System.currentTimeMillis();

    // execute
    MdInputAbstract input = null;
    if (mode.equals(MdMode.DIFF.getMode())) {
      input = MdJson.toObject(json, MdInputDiff.class);
    } else if (mode.equals(MdMode.SYNC.getMode())) {
      input = MdJson.toObject(json, MdInputSync.class);
    } else if (mode.equals(MdMode.MAINTENANCE.getMode())) {
      input = MdJson.toObject(json, MdInputMaintenance.class);
    }
    input.validate();
    MdLogger.trace(String.format("input:%s", MdJson.toJsonStringFriendly(input)));
    exitCode = MdExceptionAbstract.ExitCode.SUCCESS;

    // end
    long end = System.currentTimeMillis();
    MdLogger.trace(String.format(
        "execution time: %s (sec)",
        TimeUnit.MILLISECONDS.toSeconds(end - start)));
    if (output != null) {
      if (MdEnv.isPretty()) {
        System.out.print(MdJson.toJsonStringFriendly(output));
      } else {
        System.out.print(MdJson.toJsonString(output));
      }
    }
    return exitCode;
  }

  /**
   * help.
   * @throws IOException read error
   */
  private static void help() throws IOException {
    InputStream in = MdAppCli.class.getClassLoader().getResourceAsStream("help.txt");
    String help = MdFile.readString(in);
    System.out.print(help);
  }

  @Override
  public void close() throws Exception {
    MdLogger.close();
  }
}
