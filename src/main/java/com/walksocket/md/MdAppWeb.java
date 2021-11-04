package com.walksocket.md;

import com.walksocket.md.exception.MdExceptionAbstract;
import com.walksocket.md.sqlite.MdSqliteConnection;

/**
 * app web.
 */
public class MdAppWeb {

  /**
   * execute.
   * @param host host
   * @param port port
   * @return exit code
   * @throws Exception several error
   */
  public static MdExceptionAbstract.ExitCode execute(String host, int port) throws Exception {
    // start server
    MdExceptionAbstract.ExitCode exitCode = MdExceptionAbstract.ExitCode.SUCCESS;
    try (MdWeb web = new MdWeb(host, port)) {
      web.start();
      web.waitFor();
    }
    return exitCode;
  }
}
