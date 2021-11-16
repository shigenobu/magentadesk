package com.walksocket.md.service;

import com.walksocket.md.MdExecute;
import com.walksocket.md.MdJson;
import com.walksocket.md.MdLogger;
import com.walksocket.md.MdMode;
import com.walksocket.md.input.MdInputAbstract;
import com.walksocket.md.input.MdInputDiff;
import com.walksocket.md.input.MdInputMaintenance;
import com.walksocket.md.input.MdInputSync;
import com.walksocket.md.output.MdOutputAbstract;
import com.walksocket.md.sqlite.MdSqliteConnection;
import com.walksocket.md.sqlite.MdSqliteUtils;
import com.walksocket.md.api.MdApiQueue;
import com.walksocket.md.api.MdApiQueueMessage;
import com.walksocket.md.MdState;

/**
 * service processing.
 */
public class MdServiceProcessing implements Runnable {

  /**
   * queue.
   */
  private MdApiQueue queue;

  /**
   * constructor.
   * @param queue queue
   */
  public MdServiceProcessing(MdApiQueue queue) {
    this.queue = queue;
  }

  /**
   * processing to complete or error.
   */
  @Override
  public void run() {
    MdLogger.trace("start " + getClass().getSimpleName());
    MdApiQueueMessage message = null;
    if ((message = queue.poll()) == null) {
      return;
    }

    MdInputAbstract input = null;
    if (message.mode.equals(MdMode.DIFF.getMode())) {
      input = MdJson.toObject(message.input, MdInputDiff.class);
    } else if (message.mode.equals(MdMode.SYNC.getMode())) {
      input = MdJson.toObject(message.input, MdInputSync.class);
    } else if (message.mode.equals(MdMode.MAINTENANCE.getMode())) {
      input = MdJson.toObject(message.input, MdInputMaintenance.class);
    }

    MdOutputAbstract output = null;
    try {
      output = MdExecute.execute(input);
    } catch (Exception e) {
      MdLogger.error(e);
    }

    try (MdSqliteConnection con = new MdSqliteConnection()) {
      String sql = "";

      // begin
      con.begin();

      if (output == null) {
        // processing -> error
        sql = String.format(
            "UPDATE execution " +
                "SET state = '%s' " +
                "WHERE executionId = '%s'",
            MdSqliteUtils.quote(MdState.ERROR.getState()),
            MdSqliteUtils.quote(message.executionId)
        );
        con.execute(sql);
      } else {
        // processing -> complete
        sql = String.format(
            "UPDATE execution " +
                "SET state = '%s', output = '%s' " +
                "WHERE executionId = '%s'",
            MdSqliteUtils.quote(MdState.COMPLETE.getState()),
            MdSqliteUtils.quote(MdJson.toJsonString(output)),
            MdSqliteUtils.quote(message.executionId)
        );
        con.execute(sql);
      }

      // commit
      con.commit();

    } catch (Exception e) {
      MdLogger.error(e);
    }
  }
}
