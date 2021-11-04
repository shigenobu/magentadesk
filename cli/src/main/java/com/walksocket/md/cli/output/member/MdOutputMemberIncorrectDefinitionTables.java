package com.walksocket.md.cli.output.member;

import com.google.gson.annotations.Expose;
import com.walksocket.md.cli.MdInfoDiff;

import java.sql.SQLException;

/**
 * output incorrect.
 */
public class MdOutputMemberIncorrectDefinitionTables {

  /**
   * incorrect reason.
   */
  public enum MdOutputMemberIncorrectReason {

    /**
     * no primary key.
     */
    NO_PRIMARY_KEY("noPrimaryKey"),

    /**
     * has foreign key.
     */
    HAS_FOREIGN_KEY("hasForeignKey"),

    /**
     * invalid charset.
     */
    INVALID_CHARSET("invalidCharset"),
    ;

    /**
     * reason.
     */
    private String reason;

    /**
     * constructor.
     * @param reason reason
     */
    MdOutputMemberIncorrectReason(String reason) {
      this.reason = reason;
    }

    /**
     * get reason.
     * @return reason
     */
    public String getReason() {
      return reason;
    }
  }

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
   * definition.
   */
  @Expose
  public String definition;

  /**
   * reason.
   */
  @Expose
  public String reason;

  /**
   * constructor.
   * @param info info
   * @param reason reason
   * @throws SQLException sql error
   */
  public MdOutputMemberIncorrectDefinitionTables(MdInfoDiff info, MdOutputMemberIncorrectReason reason) throws SQLException {
    this.tableName = info.getTableName();
    this.tableComment = info.getInfoTable().getTableComment();
    this.definition = info.getDefinition();
    this.reason = reason.getReason();
  }
}
