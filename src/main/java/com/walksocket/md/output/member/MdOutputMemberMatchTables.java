package com.walksocket.md.output.member;

import com.google.gson.annotations.Expose;
import com.walksocket.md.MdInfoDiff;
import com.walksocket.md.MdUtils;

import java.sql.SQLException;

/**
 * output match.
 */
public class MdOutputMemberMatchTables {

  /**
   * table name.
   */
  @Expose
  public String tableName;

  /**
   * table comment.
   */
  @Expose
  public String tableComment;

  /**
   * base table type.
   */
  @Expose
  public String baseTableType;

  /**
   * compare table type.
   */
  @Expose
  public String compareTableType;

  /**
   * base checksum.
   */
  @Expose
  public String baseChecksum;

  /**
   * compare checksum.
   */
  @Expose
  public String compareChecksum;

  /**
   * constructor.
   * @param baseInfo base info
   * @param compareInfo compare info
   * @throws SQLException sql error
   */
  public MdOutputMemberMatchTables(MdInfoDiff baseInfo, MdInfoDiff compareInfo) throws SQLException {
    this.tableName = baseInfo.getTableName();
    this.tableComment = baseInfo.getInfoTable().getTableComment();
    this.baseTableType = baseInfo.getInfoTable().getTableType();
    this.compareTableType = compareInfo.getInfoTable().getTableType();
    this.baseChecksum = baseInfo.getChecksum();
    this.compareChecksum = compareInfo.getChecksum();
  }
}
