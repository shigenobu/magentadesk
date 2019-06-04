package com.walksocket.md.info;

import com.walksocket.md.MdUtils;
import com.walksocket.md.input.member.MdInputMemberOption;
import com.walksocket.md.mariadb.MdMariadbRecord;
import com.walksocket.md.mariadb.MdMariadbUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * input diff column.
 */
public class MdInfoDiffColumn implements MdInfoDiffInterface {

  /**
   * column name.
   */
  private String COLUMN_NAME;

  /**
   * ordinal position.
   */
  private String ORDINAL_POSITION;

  /**
   * column default.
   */
  private String COLUMN_DEFAULT;

  /**
   * is nullable.
   */
  private String IS_NULLABLE;

  /**
   * data type.
   */
  private String DATA_TYPE;

  /**
   * charset.
   */
  private String CHARACTER_SET_NAME;

  /**
   * collation.
   */
  private String COLLATION_NAME;

  /**
   * column type.
   */
  private String COLUMN_TYPE;

  /**
   * column key.
   */
  private String COLUMN_KEY;

  /**
   * extra.
   */
  private String EXTRA;

  /**
   * column comment.
   */
  private String COLUMN_COMMENT;

  /**
   * is generated.
   */
  private String IS_GENERATED;

  /**
   * generate expression.
   */
  private String GENERATION_EXPRESSION;

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
  public MdInfoDiffColumn(MdMariadbRecord record, MdInputMemberOption option) throws SQLException {
    this.COLUMN_NAME = record.get("COLUMN_NAME");
    this.ORDINAL_POSITION = record.get("ORDINAL_POSITION");
    this.COLUMN_DEFAULT = record.get("COLUMN_DEFAULT");
    this.IS_NULLABLE = record.get("IS_NULLABLE");
    this.DATA_TYPE = record.get("DATA_TYPE");
    this.CHARACTER_SET_NAME = record.get("CHARACTER_SET_NAME");
    this.COLLATION_NAME = record.get("COLLATION_NAME");
    this.COLUMN_TYPE = record.get("COLUMN_TYPE");
    this.COLUMN_KEY = record.get("COLUMN_KEY");
    this.EXTRA = record.get("EXTRA");
    this.COLUMN_COMMENT = record.get("COLUMN_COMMENT");
    this.IS_GENERATED = record.get("IS_GENERATED");
    this.GENERATION_EXPRESSION = record.get("GENERATION_EXPRESSION");

    this.option = option;
  }

  /**
   * get column name.
   * @return column name
   */
  public String getColumnName() {
    return COLUMN_NAME;
  }

  /**
   * get column comment.
   * @return column comment.
   */
  public String getColumnComment() {
    return COLUMN_COMMENT;
  }

  /**
   * get column type.
   * @return column type
   */
  public String getColumnType() {
    return COLUMN_TYPE;
  }

  /**
   * is primary.
   * @return if primary, true
   */
  public boolean isPrimary() {
    return !MdUtils.isNullOrEmpty(COLUMN_KEY) && COLUMN_KEY.toUpperCase().equals("PRI");
  }

  /**
   * is generated.
   * @return if generated, true
   */
  public boolean isGenerated() {
    return !MdUtils.isNullOrEmpty(IS_GENERATED) && !IS_GENERATED.toUpperCase().equals("NEVER");
  }

  /**
   * is valid collation.
   * @return if valid, true
   */
  public boolean isValidCollation() {
    if (!hasCollation()) {
      // number and so on, is valid.
      return true;
    }
    return MdMariadbUtils.isValidCollation(COLLATION_NAME);
  }

  /**
   * has collation.
   * @return if collation name defined, true
   */
  public boolean hasCollation() {
    return !MdUtils.isNullOrEmpty(COLLATION_NAME);
  }

  /**
   * get binary collation name.
   * @return binary collation name
   */
  public String getBinaryCollationName() {
    if (!hasCollation()) {
      return "";
    }

    return CHARACTER_SET_NAME.toLowerCase() + "_bin";
  }

  @Override
  public String getHash() {
    List<String> src = new ArrayList<>();
    src.add(getClass().getName());

    if (!(isGenerated() &&
        (
            GENERATION_EXPRESSION.toUpperCase().equals("ROW START")
            || GENERATION_EXPRESSION.toUpperCase().equals("ROW END")
        ) && option.ignoreSystemVersioned)) {
      src.add(COLUMN_NAME);
      src.add(ORDINAL_POSITION);
      src.add(COLUMN_DEFAULT);
      src.add(IS_NULLABLE);
      src.add(DATA_TYPE);
      src.add(CHARACTER_SET_NAME);
      src.add(COLLATION_NAME);
      src.add(COLUMN_TYPE);
      src.add(COLUMN_KEY);
      src.add(EXTRA);
      if (!option.ignoreComment) {
        src.add(COLUMN_COMMENT);
      }
      src.add(IS_GENERATED);
      src.add(GENERATION_EXPRESSION);
    }

    return MdUtils.getHash(MdUtils.join(src, "|"));
  }
}
