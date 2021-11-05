package com.walksocket.md.output.member;

import com.google.gson.annotations.Expose;
import com.walksocket.md.MdInfoSync;
import com.walksocket.md.output.parts.MdOutputPartsColumn;
import com.walksocket.md.output.parts.MdOutputPartsReflectedRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * output reflect.
 */
public class MdOutputMemberReflectedRecordTable {

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
   * columns.
   */
  @Expose
  public List<MdOutputPartsColumn> columns;

  /**
   * records.
   */
  @Expose
  public List<MdOutputPartsReflectedRecord> records = new ArrayList<>();

  /**
   * constructor.
   * @param info info
   */
  public MdOutputMemberReflectedRecordTable(MdInfoSync info) {
    this.tableName = info.getTableName();
    this.tableComment = info.getTableComment();
    this.columns = info.getColumns();
  }
}
