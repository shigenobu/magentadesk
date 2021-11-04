package com.walksocket.md.cli.info;

import com.walksocket.md.cli.MdLogger;
import com.walksocket.md.cli.MdUtils;
import com.walksocket.md.cli.db.MdDbRecord;
import com.walksocket.md.cli.input.member.MdInputMemberOption;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * input diff index.
 */
public class MdInfoDiffIndex implements MdInfoDiffInterface {

  /**
   * table name.
   */
  private String TABLE_NAME;

  /**
   * non unique.
   */
  private String NON_UNIQUE;

  /**
   * index name.
   */
  private String INDEX_NAME;

  /**
   * seq in index.
   */
  private String SEQ_IN_INDEX;

  /**
   * column name.
   */
  private String COLUMN_NAME;

  /**
   * collation.
   */
  private String COLLATION;

  /**
   * nullable.
   */
  private String NULLABLE;

  /**
   * index type.
   */
  private String INDEX_TYPE;

  /**
   * index comment.
   */
  private String INDEX_COMMENT;

  /**
   * input option.
   */
  private MdInputMemberOption option;

  /**
   * constructor.
   * @param record record
   * @param option input option
   * @throws SQLException sql error
   */
  public MdInfoDiffIndex(MdDbRecord record, MdInputMemberOption option) throws SQLException {
    this.TABLE_NAME = record.get("TABLE_NAME");
    this.NON_UNIQUE = record.get("NON_UNIQUE");
    this.INDEX_NAME = record.get("INDEX_NAME");
    this.SEQ_IN_INDEX = record.get("SEQ_IN_INDEX");
    this.COLUMN_NAME = record.get("COLUMN_NAME");
    this.COLLATION = record.get("COLLATION");
    this.NULLABLE = record.get("NULLABLE");
    this.INDEX_TYPE = record.get("INDEX_TYPE");
    this.INDEX_COMMENT = record.get("INDEX_COMMENT");

    this.option = option;
  }

  @Override
  public String getHash() {
    List<String> src = new ArrayList<>();
    src.add(getClass().getName());

    src.add(NON_UNIQUE);
    src.add(INDEX_NAME);
    src.add(SEQ_IN_INDEX);
    src.add(COLUMN_NAME);
    src.add(COLLATION);
    src.add(NULLABLE);
    src.add(INDEX_TYPE);
    if (!option.ignoreComment) {
      src.add(INDEX_COMMENT);
    } else {
      MdLogger.trace(String.format("ignoreComment index:%s.%s", TABLE_NAME, COLUMN_NAME));
    }

    return MdUtils.getHash(MdUtils.join(src, "|"));
  }
}
