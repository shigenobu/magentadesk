package com.walksocket.md.web.service;

import com.walksocket.md.MdDate;
import com.walksocket.md.MdLogger;
import com.walksocket.md.db.MdDbRecord;
import com.walksocket.md.sqlite.MdSqliteConnection;
import com.walksocket.md.sqlite.MdSqliteUtils;
import com.walksocket.md.web.MdWebQueue;
import com.walksocket.md.web.MdWebQueueMessage;
import com.walksocket.md.web.MdWebState;

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

      // begin
      con.begin();

      // delete old records
      sql = String.format(
          "DELETE FROM execution " +
              "WHERE created < %s",
          MdDate.timestamp() - 60 * 60 * 3
      );
      con.execute(sql);

      // get record
      sql = String.format(
          "SELECT executionId, mode, input " +
              "FROM execution " +
              "WHERE created >= %s and state = '%s' ORDER BY created asc LIMIT 1",
          MdDate.timestamp() - 60 * 60,
          MdSqliteUtils.quote(MdWebState.RESERVED.getState())
      );
      List<MdDbRecord> records = con.getRecords(sql);
      if (records.isEmpty()) {
        // commit
        con.commit();
        return;
      }
      String executionId = records.get(0).get("executionId");
      String mode = records.get(0).get("mode");
      String input = records.get(0).get("input");

      // reserved -> processing
      sql = String.format(
          "UPDATE execution " +
              "SET state = '%s' " +
              "WHERE executionId = '%s'",
          MdSqliteUtils.quote(MdWebState.PROCESSING.getState()),
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

    } catch (Exception e) {
      MdLogger.error(e);
    }
  }
}
