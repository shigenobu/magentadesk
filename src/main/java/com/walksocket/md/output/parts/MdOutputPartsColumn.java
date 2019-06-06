package com.walksocket.md.output.parts;

import com.google.gson.annotations.Expose;
import com.walksocket.md.info.MdInfoDiffColumn;

/**
 * output column.
 */
public class MdOutputPartsColumn {

  /**
   * column name.
   */
  @Expose
  public String columnName;

  /**
   * column type.
   */
  @Expose
  public String columnType;

  /**
   * column collation.
   */
  @Expose
  public String columnCollation;

  /**
   * column comment.
   */
  @Expose
  public String columnComment;

  /**
   * is primary.
   */
  @Expose
  public boolean isPrimary;

  /**
   * constructor.
   * @param infoColumn info column
   */
  public MdOutputPartsColumn(MdInfoDiffColumn infoColumn) {
    this.columnName = infoColumn.getColumnName();
    this.columnType = infoColumn.getColumnType();
    this.columnCollation = infoColumn.getColumnCollation();
    this.columnComment = infoColumn.getColumnComment();
    this.isPrimary = infoColumn.isPrimary();
  }
}
