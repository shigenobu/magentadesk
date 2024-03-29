package com.walksocket.md.output.member;

import com.google.gson.annotations.Expose;
import com.walksocket.md.MdInfoDiff;
import com.walksocket.md.MdValue;

import java.sql.SQLException;

/**
 * output only base.
 */
public class MdOutputMemberExistsOnlyBaseTable extends MdValue {

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
   * constructor.
   * @param baseInfo base info
   * @throws SQLException sql error
   */
  public MdOutputMemberExistsOnlyBaseTable(MdInfoDiff baseInfo) throws SQLException {
    this.tableName = baseInfo.getTableName();
    this.tableComment = baseInfo.getInfoTable().getTableComment();
    this.baseDefinition = baseInfo.getDefinition();
  }
}
