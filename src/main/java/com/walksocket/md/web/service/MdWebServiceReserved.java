package com.walksocket.md.web.service;

import com.walksocket.md.MdDate;
import com.walksocket.md.MdLogger;
import com.walksocket.md.db.MdDbRecord;
import com.walksocket.md.sqlite.MdSqliteConnection;
import com.walksocket.md.sqlite.MdSqliteUtils;
import com.walksocket.md.web.MdWebQueue;
import com.walksocket.md.web.MdWebQueueMessage;

import java.util.List;

/**
 * service reserved.
 */
public class MdWebServiceReserved implements Runnable {

  /**
   * queue.
   */
  private MdWebQueue queue;

  /**
   * constructor.
   * @param queue queue
   */
  public MdWebServiceReserved(MdWebQueue queue) {
    this.queue = queue;
  }

  /**
   * reserved to processing.
   */
  @Override
  public void run() {
    try (MdSqliteConnection con = new MdSqliteConnection()) {
      String sql = "";

      // get record
      sql = String.format(
          "SELECT executionId, mode, input FROM execution " +
              "WHERE created > %s and state = 'reserved' ORDER BY created asc LIMIT 1",
          MdDate.timestamp() - 60 * 60
      );
      List<MdDbRecord> records = con.getRecords(sql);
      if (!records.isEmpty()) {
        String executionId = records.get(0).get("executionId");
        String mode = records.get(0).get("mode");
        String input = records.get(0).get("input");

          // begin
          con.begin();

          // reserved -> processing
          sql = String.format(
              "update execution set state = 'processing' where executionId = '%s'",
              MdSqliteUtils.quote(executionId)
          );
          con.execute(sql);

          // commit
          con.commit();

          // message into queue
          MdWebQueueMessage messageInput = new MdWebQueueMessage();
          messageInput.executionId = executionId;
          messageInput.mode = mode;
          messageInput.input = input;
          queue.add(messageInput);
        }
    } catch (Exception e) {
      MdLogger.error(e);
    }
  }
}
