package com.walksocket.md.output.member;

import com.google.gson.annotations.Expose;
import com.walksocket.md.MdInfoDiff;
import com.walksocket.md.MdValue;
import com.walksocket.md.output.parts.MdOutputPartsColumn;
import com.walksocket.md.info.MdInfoDiffColumn;
import com.walksocket.md.output.parts.MdOutputPartsRecord;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * output mismatch.
 */
public class MdOutputMemberMismatchRecordTable extends MdValue {

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
  public List<MdOutputPartsColumn> columns = new ArrayList<>();

  /**
   * records.
   */
  @Expose
  public List<MdOutputPartsRecord> records = new ArrayList<>();

  /**
   * constructor.
   * @param info info
   * @param partsRecord records
   * @throws SQLException sql error
   */
  public MdOutputMemberMismatchRecordTable(MdInfoDiff info, List<MdOutputPartsRecord> partsRecord) throws SQLException {
    this.tableName = info.getTableName();
    this.tableComment = info.getInfoTable().getTableComment();
    for (MdInfoDiffColumn infoColumn : info.getRealColumns()) {
      this.columns.add(new MdOutputPartsColumn(infoColumn));
    }
    this.records.addAll(partsRecord);
  }
}
