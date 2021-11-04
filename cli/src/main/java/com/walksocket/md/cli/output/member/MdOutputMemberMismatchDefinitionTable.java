package com.walksocket.md.cli.output.member;

import com.google.gson.annotations.Expose;
import com.walksocket.md.cli.MdInfoDiff;

import java.sql.SQLException;

/**
 * output mismatch definition.
 */
public class MdOutputMemberMismatchDefinitionTable {

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
   * base definition.
   */
  @Expose
  public String baseDefinition;

  /**
   * compare definition.
   */
  @Expose
  public String compareDefinition;

  /**
   * constructor.
   * @param baseInfo base info
   * @param compareInfo compare info
   * @throws SQLException sql error
   */
  public MdOutputMemberMismatchDefinitionTable(MdInfoDiff baseInfo, MdInfoDiff compareInfo) throws SQLException {
    this.tableName = baseInfo.getTableName();
    this.tableComment = baseInfo.getInfoTable().getTableComment();
    this.baseDefinition = baseInfo.getDefinition();
    this.compareDefinition = compareInfo.getDefinition();
  }
}
