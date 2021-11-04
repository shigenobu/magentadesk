package com.walksocket.md.cli.info;

import com.walksocket.md.cli.MdLogger;
import com.walksocket.md.cli.MdUtils;
import com.walksocket.md.cli.input.member.MdInputMemberOption;
import com.walksocket.md.cli.mariadb.MdMariadbUtils;
import com.walksocket.md.cli.db.MdDbRecord;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * info diff table.
 */
public class MdInfoDiffTable implements MdInfoDiffInterface {

  /**
   * table name.
   */
  private String TABLE_NAME;

  /**
   * table type.
   */
  private String TABLE_TYPE;

  /**
   * engine.
   */
  private String ENGINE;

  /**
   * row format.
   */
  private String ROW_FORMAT;

  /**
   * auto increment.
   */
  private String AUTO_INCREMENT;

  /**
   * table collation.
   */
  private String TABLE_COLLATION;

  /**
   * table comment.
   */
  private String TABLE_COMMENT;

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
  public MdInfoDiffTable(MdDbRecord record, MdInputMemberOption option) throws SQLException {
    this.TABLE_NAME = record.get("TABLE_NAME");
    this.TABLE_TYPE = record.get("TABLE_TYPE");
    this.ENGINE = record.get("ENGINE");
    this.ROW_FORMAT = record.get("ROW_FORMAT");
    this.AUTO_INCREMENT = record.get("AUTO_INCREMENT");
    this.TABLE_COLLATION = record.get("TABLE_COLLATION");
    this.TABLE_COMMENT = record.get("TABLE_COMMENT");

    this.option = option;
  }

  /**
   * get table type.
   * @return table type.
   */
  public String getTableType() {
    return TABLE_TYPE;
  }

  /**
   * get table comment.
   * @return table comment.
   */
  public String getTableComment() {
    return TABLE_COMMENT;
  }

  /**
   * is valid collation.
   * @return if valid, true
   */
  public boolean isValidCollation() {
    return MdMariadbUtils.isValidCollation(TABLE_COLLATION);
  }

  /**
   * is innodeb.
   * @return if innodb, true
   */
  public boolean isInnodb() {
    return ENGINE.equalsIgnoreCase("INNODB");
  }

  /**
   * is view.
   * @return if view, true
   */
  public boolean isView() {
    return TABLE_TYPE.equalsIgnoreCase("VIEW") || TABLE_TYPE.equalsIgnoreCase("SYSTEM VIEW");
  }

  /**
   * is sequence.
   * @return if sequence, true
   */
  public boolean isSequence() {
    return TABLE_TYPE.equalsIgnoreCase("SEQUENCE");
  }

  @Override
  public String getHash() {
    List<String> src = new ArrayList<>();
    src.add(getClass().getName());

    src.add(TABLE_NAME);
    src.add(TABLE_TYPE);
    src.add(ENGINE);
    src.add(ROW_FORMAT);
    if (!option.ignoreAutoIncrement) {
      src.add(AUTO_INCREMENT);
    } else {
      MdLogger.trace(String.format("ignoreAutoIncrement table:%s", TABLE_NAME));
    }
    src.add(TABLE_COLLATION);
    if (!option.ignoreComment) {
      src.add(TABLE_COMMENT);
    } else {
      MdLogger.trace(String.format("ignoreComment table:%s", TABLE_NAME));
    }

    return MdUtils.getHash(MdUtils.join(src, "|"));
  }
}
