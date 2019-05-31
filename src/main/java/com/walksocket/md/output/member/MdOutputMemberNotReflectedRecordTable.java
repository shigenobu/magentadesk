package com.walksocket.md.output.member;

import com.google.gson.annotations.Expose;
import com.walksocket.md.MdInfoSync;
import com.walksocket.md.output.parts.MdOutputPartsColumn;
import com.walksocket.md.output.parts.MdOutputPartsNotReflectedRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * output not reflect.
 */
public class MdOutputMemberNotReflectedRecordTable {

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
  public List<MdOutputPartsNotReflectedRecord> records = new ArrayList<>();

  /**
   * constructor.
   * @param info info
   */
  public MdOutputMemberNotReflectedRecordTable(MdInfoSync info) {
    this.tableName = info.getTableName();
    this.tableComment = info.getTableComment();
    this.columns = info.getColumns();
  }
}
