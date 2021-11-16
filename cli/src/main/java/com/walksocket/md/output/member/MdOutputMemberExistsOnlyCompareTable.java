package com.walksocket.md.output.member;

import com.google.gson.annotations.Expose;
import com.walksocket.md.MdInfoDiff;
import com.walksocket.md.MdValue;

import java.sql.SQLException;

/**
 * output only compare.
 */
public class MdOutputMemberExistsOnlyCompareTable extends MdValue {

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
   * compare definition.
   */
  @Expose
  public String compareDefinition;

  /**
   * constructor.
   * @param compareInfo compare info
   * @throws SQLException sql error
   */
  public MdOutputMemberExistsOnlyCompareTable(MdInfoDiff compareInfo) throws SQLException {
    this.tableName = compareInfo.getTableName();
    this.tableComment = compareInfo.getInfoTable().getTableComment();
    this.compareDefinition = compareInfo.getDefinition();
  }
}
