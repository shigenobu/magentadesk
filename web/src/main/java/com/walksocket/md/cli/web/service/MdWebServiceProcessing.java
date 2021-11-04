package com.walksocket.md.cli.web.service;

import com.walksocket.md.cli.MdExecute;
import com.walksocket.md.cli.MdJson;
import com.walksocket.md.cli.MdLogger;
import com.walksocket.md.cli.MdMode;
import com.walksocket.md.cli.input.MdInputAbstract;
import com.walksocket.md.cli.input.MdInputDiff;
import com.walksocket.md.cli.input.MdInputMaintenance;
import com.walksocket.md.cli.input.MdInputSync;
import com.walksocket.md.cli.output.MdOutputAbstract;
import com.walksocket.md.cli.sqlite.MdSqliteConnection;
import com.walksocket.md.cli.sqlite.MdSqliteUtils;
import com.walksocket.md.cli.web.MdWebQueue;
import com.walksocket.md.cli.web.MdWebQueueMessage;
import com.walksocket.md.cli.web.MdWebState;

/**
 * service processing.
 */
public class MdWebServiceProcessing implements Runnable {

  /**
   * queue.
   */
  private MdWebQueue queue;

  /**
   * constructor.
   * @param queue queue
   */
  public MdWebServiceProcessing(MdWebQueue queue) {
    this.queue = queue;
  }

  /**
   * processing to complete or error.
   */
  @Override
  public void run() {
    MdWebQueueMessage message = null;
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
            MdSqliteUtils.quote(MdWebState.ERROR.getState()),
            MdSqliteUtils.quote(message.executionId)
        );
        con.execute(sql);
      } else {
        // processing -> complete
        sql = String.format(
            "UPDATE execution " +
                "SET state = '%s', output = '%s' " +
                "WHERE executionId = '%s'",
            MdSqliteUtils.quote(MdWebState.COMPLETE.getState()),
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
