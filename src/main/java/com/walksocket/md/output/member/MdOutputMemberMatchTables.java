package com.walksocket.md.output.member;

import com.google.gson.annotations.Expose;
import com.walksocket.md.MdInfoDiff;
import com.walksocket.md.MdLogger;

import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * output match.
 */
public class MdOutputMemberMatchTables {

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
   * base table type.
   */
  @Expose
  public String baseTableType;

  /**
   * compare table type.
   */
  @Expose
  public String compareTableType;

  /**
   * base checksum.
   */
  @Expose
  public String baseChecksum;

  /**
   * compare checksum.
   */
  @Expose
  public String compareChecksum;

  /**
   * constructor.
   * @param baseInfo base info
   * @param compareInfo compare info
   * @throws SQLException sql error
   */
  public MdOutputMemberMatchTables(MdInfoDiff baseInfo, MdInfoDiff compareInfo) throws SQLException {
    this.tableName = baseInfo.getTableName();
    this.tableComment = baseInfo.getInfoTable().getTableComment();
    this.baseTableType = baseInfo.getInfoTable().getTableType();
    this.compareTableType = compareInfo.getInfoTable().getTableType();

    // checksum
    try {
      ExecutorService service = Executors.newFixedThreadPool(2);
      CompletableFuture<String> baseFuture = baseInfo.getChecksumFuture(service);
      CompletableFuture<String> compareFuture = compareInfo.getChecksumFuture(service);
      this.baseChecksum = baseFuture.get(600, TimeUnit.SECONDS);
      this.compareChecksum = compareFuture.get(600, TimeUnit.SECONDS);
    } catch (Exception e) {
      MdLogger.error(e);
      new SQLException(e);
    }
  }
}
