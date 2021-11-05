package com.walksocket.md;

import com.walksocket.md.bash.MdBashCommand;
import com.walksocket.md.bash.MdBashResult;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * bash.
 */
public class MdBash {

  /**
   * default timeout.
   */
  public static final int DEFAULT_TIMEOUT = 30;

  /**
   * init command env.
   */
  private static final String INIT_COMMAND_ENV = "env";

  /**
   * init command lang.
   */
  private static final String INIT_COMMAND_LANG = "LANG=en_US.UTF-8";

  /**
   * init command shell.
   */
  private static final String INIT_COMMAND_SHELL = "/bin/bash";

  /**
   * init command shell option.
   */
  private static final String INIT_COMMAND_SHELL_OPTION = "-c";

  /**
   * constructor.
   */
  private MdBash() {
  }

  /**
   * exec.
   * @param cmd command object
   * @return result
   */
  public static MdBashResult exec(MdBashCommand cmd) {
    MdLogger.trace(cmd);

    // check bash
    if (!new File(INIT_COMMAND_SHELL).exists()) {
      return null;
    }

    // get
    String command = cmd.getCommand();
    int timeout = cmd.getTimeout();
    String stdin = cmd.getStdin();
    String modifiedCommand = command;
    if (!MdUtils.isNullOrEmpty(stdin)) {
      modifiedCommand = String.format(" echo '%s' | %s", stdin, command);
    }

    // init
    int code = -1;
    List<String> commands = new ArrayList<>();
    commands.add(INIT_COMMAND_ENV);
    commands.add(INIT_COMMAND_LANG);
    commands.add(INIT_COMMAND_SHELL);
    commands.add(INIT_COMMAND_SHELL_OPTION);
    commands.add("(IFS=$'\n';" + modifiedCommand + ")");
    MdLogger.trace(MdUtils.join(commands, " "));
    ProcessBuilder builder
        = new ProcessBuilder((String[]) commands.toArray(new String[commands.size()]));
    builder.redirectErrorStream(true);

    // exec
    int len = 1024;
    byte[] tmp = new byte[len];
    Process process = null;
    InputStream in = null;

    StringBuffer buffer = new StringBuffer();
    try {
      process = builder.start();
      in = process.getInputStream();
      int readLen = 0;
      while ((readLen = in.read(tmp, 0, len)) >= 0) {
        buffer.append(new String(tmp, 0, readLen, StandardCharsets.UTF_8));
      }

      long begin = System.currentTimeMillis();
      while (true) {
        if (!process.isAlive()) {
          code = process.exitValue();
          break;
        }

        long now = System.currentTimeMillis();
        if (TimeUnit.MILLISECONDS.toSeconds(now - begin) >= timeout) {
          break;
        }
      }
    } catch (Exception e) {
      MdLogger.error(e);
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException e) {
          MdLogger.error(e);
        }
      }
      if (process != null) {
        process.destroy();
      }
    }
    MdLogger.trace(String.format("command -> %s, code -> %s, output -> %s",
        command,
        code,
        buffer.toString()));

    MdBashResult result = new MdBashResult();
    result.command = command;
    result.code = code;
    result.output = buffer.toString();
    return result;
  }
}
