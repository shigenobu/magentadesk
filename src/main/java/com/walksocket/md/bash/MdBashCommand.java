package com.walksocket.md.bash;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * command.
 */
public class MdBashCommand {

  /**
   * command.
   */
  private String command;

  /**
   * timeout.
   */
  private int timeout;

  /**
   * stdin.
   */
  private String stdin;

  /**
   * constructor.
   * @param command command
   * @param timeout timeout
   */
  public MdBashCommand(String command, int timeout) {
    this.command = command;
    this.timeout = timeout;
  }

  /**
   * get command.
   * @return command.
   */
  public String getCommand() {
    return command;
  }

  /**
   * get timeout.
   * @return timeout.
   */
  public int getTimeout() {
    return timeout;
  }

  /**
   * get stdin.
   * @return stdin.
   */
  public String getStdin() {
    return stdin;
  }

  /**
   * set stdin.
   * @param stdin stdin.
   */
  public void setStdin(String stdin) {
    this.stdin = stdin;
  }

  @Override
  public String toString() {
    return String.format(
        "command:%s, timeout:%s, stdin:%s", command, timeout, stdin);
  }
}
