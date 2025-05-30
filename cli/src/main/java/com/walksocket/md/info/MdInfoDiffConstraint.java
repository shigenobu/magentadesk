package com.walksocket.md.info;

import com.walksocket.md.MdUtils;
import com.walksocket.md.db.MdDbRecord;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * input diff constraint.
 */
public class MdInfoDiffConstraint implements MdInfoDiffInterface {

  /**
   * constraint name.
   */
  private final String CONSTRAINT_NAME;

  /**
   * check clause.
   */
  private final String CHECK_CLAUSE;

  /**
   * constructor.
   *
   * @param record record
   * @throws SQLException sql error
   */
  public MdInfoDiffConstraint(MdDbRecord record) throws SQLException {
    this.CONSTRAINT_NAME = record.get("CONSTRAINT_NAME");
    this.CHECK_CLAUSE = record.get("CHECK_CLAUSE");
  }

  @Override
  public String getHash() {
    List<String> src = new ArrayList<>();
    src.add(getClass().getName());

    src.add(CONSTRAINT_NAME);
    src.add(CHECK_CLAUSE);

    return MdUtils.getHash(MdUtils.join(src, "|"));
  }
}
