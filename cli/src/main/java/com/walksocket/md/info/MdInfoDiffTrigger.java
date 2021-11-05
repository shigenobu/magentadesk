package com.walksocket.md.info;

import com.walksocket.md.MdUtils;
import com.walksocket.md.db.MdDbRecord;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * info diff trigger.
 */
public class MdInfoDiffTrigger implements MdInfoDiffInterface {

  /**
   * trigger name.
   */
  private String TRIGGER_NAME;

  /**
   * event manipulation.
   */
  private String EVENT_MANIPULATION;

  /**
   * action order.
   */
  private String ACTION_ORDER;

  /**
   * action statement.
   */
  private String ACTION_STATEMENT;

  /**
   * action timing.
   */
  private String ACTION_TIMING;

  /**
   * charset client.
   */
  private String CHARACTER_SET_CLIENT;

  /**
   * collation connection.
   */
  private String COLLATION_CONNECTION;

  /**
   * database collation.
   */
  private String DATABASE_COLLATION;

  /**
   * constructor.
   * @param record record
   * @throws SQLException sql error
   */
  public MdInfoDiffTrigger(MdDbRecord record) throws SQLException {
    this.TRIGGER_NAME = record.get("TRIGGER_NAME");
    this.EVENT_MANIPULATION = record.get("EVENT_MANIPULATION");
    this.ACTION_ORDER = record.get("ACTION_ORDER");
    this.ACTION_STATEMENT = record.get("ACTION_STATEMENT");
    this.ACTION_TIMING = record.get("ACTION_TIMING");
    this.CHARACTER_SET_CLIENT = record.get("CHARACTER_SET_CLIENT");
    this.COLLATION_CONNECTION = record.get("COLLATION_CONNECTION");
    this.DATABASE_COLLATION = record.get("DATABASE_COLLATION");
  }

  @Override
  public String getHash() {
    List<String> src = new ArrayList<>();
    src.add(getClass().getName());

    src.add(TRIGGER_NAME);
    src.add(EVENT_MANIPULATION);
    src.add(ACTION_ORDER);
    src.add(ACTION_STATEMENT);
    src.add(ACTION_TIMING);
    src.add(CHARACTER_SET_CLIENT);
    src.add(COLLATION_CONNECTION);
    src.add(DATABASE_COLLATION);

    return MdUtils.getHash(MdUtils.join(src, "|"));
  }
}
