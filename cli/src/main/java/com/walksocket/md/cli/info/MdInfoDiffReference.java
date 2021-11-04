package com.walksocket.md.cli.info;

import com.walksocket.md.cli.MdUtils;
import com.walksocket.md.cli.db.MdDbRecord;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * info diff reference.
 */
public class MdInfoDiffReference implements MdInfoDiffInterface {

  /**
   * constraint name.
   */
  private String CONSTRAINT_NAME;

  /**
   * unique constraint name.
   */
  private String UNIQUE_CONSTRAINT_NAME;

  /**
   * match option.
   */
  private String MATCH_OPTION;

  /**
   * update rule.
   */
  private String UPDATE_RULE;

  /**
   * delete rule.
   */
  private String DELETE_RULE;

  /**
   * table name.
   */
  private String TABLE_NAME;

  /**
   * referenced table name.
   */
  private String REFERENCED_TABLE_NAME;

  /**
   * constructor.
   * @param record record
   * @throws SQLException sql error
   */
  public MdInfoDiffReference(MdDbRecord record) throws SQLException {
    this.CONSTRAINT_NAME = record.get("CONSTRAINT_NAME");
    this.UNIQUE_CONSTRAINT_NAME = record.get("UNIQUE_CONSTRAINT_NAME");
    this.MATCH_OPTION = record.get("MATCH_OPTION");
    this.UPDATE_RULE = record.get("UPDATE_RULE");
    this.DELETE_RULE = record.get("DELETE_RULE");
    this.TABLE_NAME = record.get("TABLE_NAME");
    this.REFERENCED_TABLE_NAME = record.get("REFERENCED_TABLE_NAME");
  }

  @Override
  public String getHash() {
    List<String> src = new ArrayList<>();
    src.add(getClass().getName());

    src.add(CONSTRAINT_NAME);
    src.add(UNIQUE_CONSTRAINT_NAME);
    src.add(MATCH_OPTION);
    src.add(UPDATE_RULE);
    src.add(DELETE_RULE);
    src.add(TABLE_NAME);
    src.add(REFERENCED_TABLE_NAME);

    return MdUtils.getHash(MdUtils.join(src, "|"));
  }
}
