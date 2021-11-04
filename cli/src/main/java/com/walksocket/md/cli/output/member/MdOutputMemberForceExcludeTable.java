package com.walksocket.md.cli.output.member;

import com.google.gson.annotations.Expose;
import com.walksocket.md.cli.MdInfoDiff;

import java.sql.SQLException;

/**
 * output force exclude.
 */
public class MdOutputMemberForceExcludeTable {

  /**
   * force exclude reason.
   */
  public enum MdOutputMemberForceExcldeReason {

    /**
     * referenced foreign key.
     */
    REFERENCED_FOREIGH_KEY("referencedForeignKey"),

    /**
     * is view.
     */
    IS_VIEW("isView"),

    /**
     * is sequence.
     */
    IS_SEQUENCE("isSequence"),

    /**
     * not innodb.
     */
    NOT_INNODB("notInnoDB"),

    /**
     * mismatch trigger.
     */
    MISMATCH_TRIGGER("mismatchTrigger"),
    ;

    /**
     * reason.
     */
    private String reason;

    /**
     * constructor.
     * @param reason reason.
     */
    MdOutputMemberForceExcldeReason(String reason) {
      this.reason = reason;
    }

    /**
     * get reason.
     * @return reason.
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
  public MdOutputMemberForceExcludeTable(MdInfoDiff info, MdOutputMemberForceExcldeReason reason) throws SQLException {
    this.tableName = info.getTableName();
    this.tableComment = info.getInfoTable().getTableComment();
    this.definition = info.getDefinition();
    this.reason = reason.getReason();
  }
}
