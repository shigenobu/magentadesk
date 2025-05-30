package com.walksocket.md.info;

import com.walksocket.md.MdLogger;
import com.walksocket.md.MdUtils;
import com.walksocket.md.db.MdDbRecord;
import com.walksocket.md.input.member.MdInputMemberOption;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * input diff partition.
 */
public class MdInfoDiffPartition implements MdInfoDiffInterface {

  /**
   * table name.
   */
  private final String TABLE_NAME;

  /**
   * partition name.
   */
  private final String PARTITION_NAME;

  /**
   * subpartition name.
   */
  private final String SUBPARTITION_NAME;

  /**
   * partition ordinal postion.
   */
  private final String PARTITION_ORDINAL_POSITION;

  /**
   * subpartition ordinal postion.
   */
  private final String SUBPARTITION_ORDINAL_POSITION;

  /**
   * partition method.
   */
  private final String PARTITION_METHOD;

  /**
   * subpartition method.
   */
  private final String SUBPARTITION_METHOD;

  /**
   * partition expression.
   */
  private final String PARTITION_EXPRESSION;

  /**
   * subpartition expression.
   */
  private final String SUBPARTITION_EXPRESSION;

  /**
   * partition comment.
   */
  private final String PARTITION_COMMENT;

  /**
   * input option.
   */
  private final MdInputMemberOption option;

  /**
   * constructor.
   *
   * @param record record
   * @param option input option
   * @throws SQLException sql error
   */
  public MdInfoDiffPartition(MdDbRecord record, MdInputMemberOption option) throws SQLException {
    this.TABLE_NAME = record.get("TABLE_NAME");
    this.PARTITION_NAME = record.get("PARTITION_NAME");
    this.SUBPARTITION_NAME = record.get("SUBPARTITION_NAME");
    this.PARTITION_ORDINAL_POSITION = record.get("PARTITION_ORDINAL_POSITION");
    this.SUBPARTITION_ORDINAL_POSITION = record.get("SUBPARTITION_ORDINAL_POSITION");
    this.PARTITION_METHOD = record.get("PARTITION_METHOD");
    this.SUBPARTITION_METHOD = record.get("SUBPARTITION_METHOD");
    this.PARTITION_EXPRESSION = record.get("PARTITION_EXPRESSION");
    this.SUBPARTITION_EXPRESSION = record.get("SUBPARTITION_EXPRESSION");
    this.PARTITION_COMMENT = record.get("PARTITION_COMMENT");

    this.option = option;
  }

  @Override
  public String getHash() {
    List<String> src = new ArrayList<>();
    src.add(getClass().getName());

    src.add(PARTITION_NAME);
    src.add(SUBPARTITION_NAME);
    src.add(PARTITION_ORDINAL_POSITION);
    src.add(SUBPARTITION_ORDINAL_POSITION);
    src.add(PARTITION_METHOD);
    src.add(SUBPARTITION_METHOD);
    src.add(PARTITION_EXPRESSION);
    src.add(SUBPARTITION_EXPRESSION);
    if (!option.ignoreComment) {
      src.add(PARTITION_COMMENT);
    } else {
      MdLogger.trace(String.format("ignoreComment partition:%s.%s", TABLE_NAME, PARTITION_NAME));
    }

    return MdUtils.getHash(MdUtils.join(src, "|"));
  }
}
