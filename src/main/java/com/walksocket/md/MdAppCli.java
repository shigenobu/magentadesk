package com.walksocket.md;

import com.walksocket.md.exception.MdExceptionAbstract;
import com.walksocket.md.input.MdInputAbstract;
import com.walksocket.md.input.MdInputDiff;
import com.walksocket.md.input.MdInputMaintenance;
import com.walksocket.md.input.MdInputSync;
import com.walksocket.md.output.MdOutputAbstract;

import java.util.concurrent.TimeUnit;

/**
 * app cli.
 */
public class MdAppCli {

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
}
