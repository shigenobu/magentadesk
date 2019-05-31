package com.walksocket.md.info;

import com.walksocket.md.MdUtils;
import com.walksocket.md.input.member.MdInputMemberOption;
import com.walksocket.md.mariadb.MdMariadbRecord;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * input diff partition.
 */
public class MdInfoDiffPartition implements MdInfoDiffInterface {

  /**
   * partition name.
   */
  private String PARTITION_NAME;

  /**
   * subpartition name.
   */
  private String SUBPARTITION_NAME;

  /**
   * partition ordinal postion.
   */
  private String PARTITION_ORDINAL_POSITION;

  /**
   * subpartition ordinal postion.
   */
  private String SUBPARTITION_ORDINAL_POSITION;

  /**
   * partition method.
   */
  private String PARTITION_METHOD;

  /**
   * subpartition method.
   */
  private String SUBPARTITION_METHOD;

  /**
   * partition expression.
   */
  private String PARTITION_EXPRESSION;

  /**
   * subpartition expression.
   */
  private String SUBPARTITION_EXPRESSION;

  /**
   * partition comment.
   */
  private String PARTITION_COMMENT;

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
  public MdInfoDiffPartition(MdMariadbRecord record, MdInputMemberOption option) throws SQLException {
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
    }

    return MdUtils.getHash(MdUtils.join(src, "|"));
  }
}
