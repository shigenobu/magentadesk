package com.walksocket.md;

import com.walksocket.md.input.MdInputSync;
import com.walksocket.md.mariadb.MdMariadbConnection;
import com.walksocket.md.db.MdDbRecord;
import com.walksocket.md.mariadb.MdMariadbUtils;
import com.walksocket.md.output.parts.MdOutputPartsColumn;

import java.sql.SQLException;
import java.util.*;

/**
 * info for sync.
 */
public class MdInfoSync {

  /**
   * create info list.
   * @param con mariadb connection
   * @param baseDatabase base database
   * @param compareDatabase compare database
   * @param inputSync input object
   * @return listed info
   * @throws SQLException sql error
   */
  public static List<MdInfoSync> createInfoList(
      MdMariadbConnection con,
      String baseDatabase,
      String compareDatabase,
      MdInputSync inputSync) throws SQLException {
    String sql;
    List<MdDbRecord> records;

    List<MdInfoSync> infoList = new ArrayList<>();

    sql = String.format(
        "SELECT `tableName`, `tableComment`, `columns` " +
            "FROM `magentadesk`.`diffTable` " +
            "WHERE `summaryId` = '%s'",
        MdMariadbUtils.quote(inputSync.summaryId));
    records = con.getRecords(sql);
    for (MdDbRecord record : records) {
      String tableName = record.get("tableName");
      String tableComment = record.get("tableComment");
      MdOutputPartsColumn[] columns = MdJson.toObject(record.get("columns"), MdOutputPartsColumn[].class);

      MdInfoSync info = new MdInfoSync(
          baseDatabase,
          compareDatabase,
          tableName,
          tableComment,
          Arrays.asList(columns));
      infoList.add(info);
    }

    sql = String.format(
        "SELECT `diffSeq`, `tableName` " +
            "FROM `magentadesk`.`diffRecord` " +
            "WHERE `diffSeq` in (%s)",
        MdUtils.join(inputSync.diffSeqs, ", "));
    records = con.getRecords(sql);
    for (MdDbRecord record : records) {
      long diffSeq = Long.parseLong(record.get("diffSeq"));
      String tableName = record.get("tableName");

      Optional<MdInfoSync> opt = infoList
          .stream()
          .filter(i -> i.getTableName().equals(tableName))
          .findFirst();
      if (opt.isPresent()) {
        opt.get().addDiffSeq(diffSeq);
      }
    }

    return infoList;
  }

  /**
   * base database.
   */
  private String baseDatabase;

  /**
   * compare database.
   */
  private String compareDatabase;

  /**
   * table name.
   */
  private String tableName;

  /**
   * table comment.
   */
  private String tableComment;

  /**
   * columns.
   */
  private List<MdOutputPartsColumn> columns;

  /**
   * diff seqs.
   */
  private List<Long> diffSeqs = new ArrayList<>();

  /**
   * constructor.
   * @param baseDatabase base database
   * @param compareDatabase compare database
   * @param tableName table name
   * @param tableComment table comment
   * @param columns columns
   */
  protected MdInfoSync(String baseDatabase, String compareDatabase, String tableName, String tableComment, List<MdOutputPartsColumn> columns) {
    this.baseDatabase = baseDatabase;
    this.compareDatabase = compareDatabase;
    this.tableName = tableName;
    this.tableComment = tableComment;
    this.columns = columns;
  }

  /**
   * get base database.
   * @return base database
   */
  public String getBaseDatabase() {
    return baseDatabase;
  }

  /**
   * get compare database.
   * @return compare database
   */
  public String getCompareDatabase() {
    return compareDatabase;
  }

  /**
   * get table name.
   * @return table name.
   */
  public String getTableName() {
    return tableName;
  }

  /**
   * get table comment.
   * @return table comment
   */
  public String getTableComment() {
    return tableComment;
  }

  /**
   * get columns.
   * @return columns
   */
  public List<MdOutputPartsColumn> getColumns() {
    return columns;
  }

  /**
   * add diff seq.
   * @param diffSeq diff seq
   */
  protected void addDiffSeq(long diffSeq) {
    this.diffSeqs.add(diffSeq);
  }

  /**
   * get diff seqs.
   * @return diff seqs.
   */
  public List<Long> getDiffSeqs() {
    return diffSeqs;
  }
}
