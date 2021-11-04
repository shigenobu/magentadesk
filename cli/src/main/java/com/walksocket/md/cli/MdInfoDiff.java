package com.walksocket.md.cli;

import com.walksocket.md.cli.db.MdDbRecord;
import com.walksocket.md.cli.info.*;
import com.walksocket.md.cli.input.member.MdInputMemberCondition;
import com.walksocket.md.cli.input.member.MdInputMemberOption;
import com.walksocket.md.cli.mariadb.MdMariadbConnection;
import com.walksocket.md.cli.mariadb.MdMariadbUtils;
import com.walksocket.md.cli.supplier.MdSupplierInfoGetChecksum;
import com.walksocket.md.cli.supplier.MdSupplierInfoGetChecksumFake;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * info for diff.
 */
public class MdInfoDiff {

  /**
   * create info list.
   * @param con mariadb connection.
   * @param database base or compare database
   * @param option input option
   * @param conditions condtion list
   * @return listed info
   * @throws SQLException sql error
   */
  public static List<MdInfoDiff> createInfoList(
      MdMariadbConnection con,
      String database,
      MdInputMemberOption option,
      List<MdInputMemberCondition> conditions) throws SQLException {
    String sql = null;
    List<MdDbRecord> records = null;

    List<MdInfoDiff> infoList = new ArrayList<>();

    List<String> likeSqlPatterns = new ArrayList<>();
    List<String> notLikeSqlPatterns = new ArrayList<>();
    for (String pattern : option.includeTableLikePatterns) {
      likeSqlPatterns.add(
          String.format("table_name like '%s'",
              MdMariadbUtils.quote(pattern)));
    }
    for (String pattern : option.excludeTableLikePatterns) {
      notLikeSqlPatterns.add(
          String.format("table_name not like '%s'",
              MdMariadbUtils.quote(pattern)));
    }
    if (likeSqlPatterns.size() == 0) {
      likeSqlPatterns.add("table_name like '%'");
    }
    if (notLikeSqlPatterns.size() == 0) {
      notLikeSqlPatterns.add("table_name not like ''");
    }

    sql = String.format(
        "SELECT `TABLE_NAME` " +
            "FROM information_schema.TABLES " +
            "WHERE table_schema = '%s' and (%s) and (%s) " +
            "ORDER BY table_name",
        database,
        MdUtils.join(likeSqlPatterns, " OR "),
        MdUtils.join(notLikeSqlPatterns, " AND "));
    records = con.getRecords(sql);
    for (MdDbRecord record : records) {
      String tableName = record.get("TABLE_NAME");

      MdInputMemberCondition condition = null;
      Optional<MdInputMemberCondition> opt = conditions.stream().filter(c -> c.tableName.equals(tableName)).findFirst();
      if (opt.isPresent()) {
        condition = opt.get();
      }

      MdInfoDiff info = new MdInfoDiff(
          con,
          database,
          tableName,
          option,
          condition);
      infoList.add(info);
    }

    return infoList;
  }

  /**
   * mariadb connection.
   */
  private MdMariadbConnection con;

  /**
   * database (base or compare).
   */
  private String database;

  /**
   * table name.
   */
  private String tableName;

  /**
   * input option.
   */
  private MdInputMemberOption option;

  /**
   * input condition.
   */
  private MdInputMemberCondition condition;

  /**
   * definition.
   * <pre>
   *   SHOW CREATE [TABLE|VIEW|SEQUENCE] ...
   * </pre>
   */
  private String definition;

  /**
   * information table.
   * <pre>
   *   FROM information_schema.TABLES
   * </pre>
   */
  private MdInfoDiffTable infoTable;

  /**
   * information column.
   * <pre>
   *   FROM information_schema.COLUMNS
   * </pre>
   */
  private List<MdInfoDiffColumn> infoColumns = null;

  /**
   * information reference.
   * <pre>
   *   FROM information_schema.REFERENTIAL_CONSTRAINTS
   * </pre>
   */
  private List<MdInfoDiffReference> infoReferences = null;

  /**
   * information constraint.
   * <pre>
   *   FROM information_schema.CHECK_CONSTRAINTS
   * </pre>
   */
  private List<MdInfoDiffConstraint> infoConstraints = null;

  /**
   * information index.
   * <pre>
   *   FROM information_schema.STATISTICS
   * </pre>
   */
  private List<MdInfoDiffIndex> infoIndexes = null;

  /**
   * information partition.
   * <pre>
   *   FROM information_schema.PARTITIONS
   * </pre>
   */
  private List<MdInfoDiffPartition> infoPartitions = null;

  /**
   * information trigger.
   * <pre>
   *   FROM information_schema.TRIGGERS
   * </pre>
   */
  private List<MdInfoDiffTrigger> infoTriggers = null;

  /**
   * information referenced.
   */
  private Set<String> referencedTableNames = null;

  /**
   * supplier for get checksum.
   */
  private MdSupplierInfoGetChecksum supplier;

  /**
   * construtor.
   * @param con mariadb connection
   * @param database base or compare database
   * @param tableName table name
   * @param option input option
   * @param condition where condition
   * @throws SQLException sql error
   */
  public MdInfoDiff(
      MdMariadbConnection con,
      String database,
      String tableName,
      MdInputMemberOption option,
      MdInputMemberCondition condition) throws SQLException {
    this.con = con;
    this.database = database;
    this.tableName = tableName;
    this.option = option;
    this.condition = condition;
  }

  /**
   * get database.
   * @return database
   */
  public String getDatabase() {
    return database;
  }

  /**
   * get table name.
   * @return table name
   */
  public String getTableName() {
    return tableName;
  }

  /**
   * get where expression.
   * @return where expression
   */
  public String getWhereExpression() {
    if (condition == null) {
      return "";
    }

    String expression = "";
    if (!MdUtils.isNullOrEmpty(condition.expression)) {
      expression = "WHERE " + condition.expression;
    }
    return expression;
  }

  /**
   * get definition.
   * @return definition
   * @throws SQLException sql error
   */
  public String getDefinition() throws SQLException {
    if (definition == null) {
      String type;
      if (getInfoTable().isView()) {
        type = "VIEW";
      } else if (getInfoTable().isSequence()) {
        type = "SEQUENCE";
      } else {
        type = "TABLE";
      }

      String sql = String.format(
          "SHOW CREATE %s `%s`.`%s`",
          type,
          database,
          tableName);
      List<MdDbRecord> records = con.getRecords(sql);
      for (MdDbRecord record : records) {
        if (type.equals("TABLE")
            || type.equals("SEQUENCE")) {
          definition = record.get("Create Table");
        } else if (type.equals("VIEW")) {
          definition = record.get("Create View");
        }
      }
    }
    return definition;
  }

  /**
   * get structure hash.
   * @return structure hash
   * @throws SQLException sql error
   */
  public String getStructureHash() throws SQLException {
    StringBuffer buffer = new StringBuffer();

    // table
    buffer.append(getInfoTable().getHash());
    MdLogger.trace(() -> ("after table hash:" + buffer.toString()));

    // column
    for (MdInfoDiffColumn i : getInfoColumns()) {
      buffer.append(i.getHash());
    }
    MdLogger.trace(() -> ("after column hash:" + buffer.toString()));

    // reference
    for (MdInfoDiffReference i : getInfoReferences()) {
      buffer.append(i.getHash());
    }
    MdLogger.trace(() -> ("after reference hash:" + buffer.toString()));

    // constraint
    for (MdInfoDiffConstraint i : getInfoConstraints()) {
      buffer.append(i.getHash());
    }
    MdLogger.trace(() -> ("after constraint hash:" + buffer.toString()));

    // index
    for (MdInfoDiffIndex i : getInfoIndexes()) {
      buffer.append(i.getHash());
    }
    MdLogger.trace(() -> ("after index hash:" + buffer.toString()));

    // partition
    if (!option.ignorePartitions) {
      for (MdInfoDiffPartition i : getInfoPartitions()) {
        buffer.append(i.getHash());
      }
    }
    MdLogger.trace(() -> ("after partition hash:" + buffer.toString()));

    // no trigger

    String hash = MdUtils.getHash(buffer.toString());
    MdLogger.trace(() -> ("hash of " + tableName + ":" + buffer.toString()));
    return hash;
  }

  /**
   * get future for checksum.
   * @param service service
   * @return future for checksum
   */
  public CompletableFuture<String> getChecksumFuture(ExecutorService service) {
    if (supplier == null) {
      if (condition != null) {
        supplier = new MdSupplierInfoGetChecksumFake(con.getConnectionString(), database, tableName);
      } else {
        supplier = new MdSupplierInfoGetChecksum(con.getConnectionString(), database, tableName);
      }
    }
    return CompletableFuture.supplyAsync(supplier, service);
  }

  /**
   * get info table for diff.
   * @return info table object
   * @throws SQLException sql error.
   */
  public MdInfoDiffTable getInfoTable() throws SQLException {
    if (infoTable == null) {
      String sql = String.format(
          "SELECT * " +
              "FROM information_schema.TABLES " +
              "WHERE table_schema = '%s' and table_name = '%s'",
          database,
          tableName);
      List<MdDbRecord> records = con.getRecords(sql);
      for (MdDbRecord record : records) {
        infoTable = new MdInfoDiffTable(record, option);
      }
    }
    return infoTable;
  }

  /**
   * get info column list for diff.
   * @return info column object list
   * @throws SQLException sql error.
   */
  private List<MdInfoDiffColumn> getInfoColumns() throws SQLException {
    if (infoColumns == null) {
      infoColumns = new ArrayList<>();
      String sql = String.format(
          "SELECT * " +
              "FROM information_schema.COLUMNS " +
              "WHERE table_schema = '%s' and table_name = '%s' " +
              "ORDER BY ordinal_position",
          database,
          tableName);
      List<MdDbRecord> records = con.getRecords(sql);
      for (MdDbRecord record : records) {
        infoColumns.add(new MdInfoDiffColumn(record, option));
      }
    }
    return infoColumns;
  }

  /**
   * get info reference list for diff.
   * @return info reference object list
   * @throws SQLException sql error.
   */
  private List<MdInfoDiffReference> getInfoReferences() throws SQLException {
    if (infoReferences == null) {
      infoReferences = new ArrayList<>();
      String sql = String.format(
          "SELECT * " +
              "FROM information_schema.REFERENTIAL_CONSTRAINTS " +
              "WHERE constraint_schema = '%s' and table_name = '%s' " +
              "ORDER BY constraint_name",
          database,
          tableName);
      List<MdDbRecord> records = con.getRecords(sql);
      for (MdDbRecord record : records) {
        infoReferences.add(new MdInfoDiffReference(record));
      }
    }
    return infoReferences;
  }

  /**
   * get info constraint list for diff.
   * @return info constraint object list
   * @throws SQLException sql error.
   */
  private List<MdInfoDiffConstraint> getInfoConstraints() throws SQLException {
    if (infoConstraints == null) {
      infoConstraints = new ArrayList<>();
      String sql = String.format(
          "SELECT * " +
              "FROM information_schema.CHECK_CONSTRAINTS " +
              "WHERE constraint_schema = '%s' and table_name = '%s' " +
              "ORDER BY constraint_name",
          database,
          tableName);
      List<MdDbRecord> records = con.getRecords(sql);
      for (MdDbRecord record : records) {
        infoConstraints.add(new MdInfoDiffConstraint(record));
      }
    }
    return infoConstraints;
  }

  /**
   * get info index list for diff.
   * @return info index object list
   * @throws SQLException sql error.
   */
  private List<MdInfoDiffIndex> getInfoIndexes() throws SQLException {
    if (infoIndexes == null) {
      infoIndexes = new ArrayList<>();
      String sql = String.format(
          "SELECT * " +
              "FROM information_schema.STATISTICS " +
              "WHERE table_schema = '%s' and table_name = '%s' " +
              "ORDER BY index_name, seq_in_index",
          database,
          tableName);
      List<MdDbRecord> records = con.getRecords(sql);
      for (MdDbRecord record : records) {
        infoIndexes.add(new MdInfoDiffIndex(record, option));
      }
    }
    return infoIndexes;
  }

  /**
   * get info partition list for diff.
   * @return info partition object list
   * @throws SQLException sql error.
   */
  private List<MdInfoDiffPartition> getInfoPartitions() throws SQLException {
    if (infoPartitions == null) {
      infoPartitions = new ArrayList<>();
      String sql = String.format(
          "SELECT * " +
              "FROM information_schema.PARTITIONS " +
              "WHERE table_schema = '%s' and table_name = '%s' " +
              "ORDER BY partition_ordinal_position",
          database,
          tableName);
      List<MdDbRecord> records = con.getRecords(sql);
      for (MdDbRecord record : records) {
        infoPartitions.add(new MdInfoDiffPartition(record, option));
      }
    }
    return infoPartitions;
  }

  /**
   * get info trigger list for diff.
   * @return info trigger object list
   * @throws SQLException sql error.
   */
  private List<MdInfoDiffTrigger> getInfoTriggers() throws SQLException {
    if (infoTriggers == null) {
      infoTriggers = new ArrayList<>();
      String sql = String.format(
          "SELECT * " +
              "FROM information_schema.TRIGGERS " +
              "WHERE event_object_schema = '%s' and event_object_table = '%s' " +
              "ORDER BY event_manipulation, action_order",
          database,
          tableName);
      List<MdDbRecord> records = con.getRecords(sql);
      for (MdDbRecord record : records) {
        infoTriggers.add(new MdInfoDiffTrigger(record));
      }
    }
    return infoTriggers;
  }

  /**
   * get referenced table name.
   * @return referenced table name
   * @throws SQLException sql error
   */
  private Set<String> getReferencedTableNames() throws SQLException {
    if (referencedTableNames == null) {
      referencedTableNames = new HashSet<>();
      String sql;
      List<MdDbRecord> records;

      sql = String.format(
          "SELECT * " +
              "FROM information_schema.REFERENTIAL_CONSTRAINTS " +
              "WHERE unique_constraint_schema = '%s' and referenced_table_name = '%s'",
          database,
          tableName);
      records = con.getRecords(sql);
      for (MdDbRecord record : records) {
        referencedTableNames.add(record.get("REFERENCED_TABLE_NAME"));
      }
    }
    return referencedTableNames;
  }

  /**
   * get primary columns.
   * @return primary columns
   * @throws SQLException sql error
   */
  public List<MdInfoDiffColumn> getPrimaryColumns() throws SQLException {
    List<MdInfoDiffColumn> columns = new ArrayList<>();
    for (MdInfoDiffColumn infoColumn : getInfoColumns()) {
      if (!infoColumn.isPrimary()) {
        continue;
      }
      columns.add(infoColumn);
    }

    return columns;
  }

  /**
   * get real columns.
   * @return real columns
   * @throws SQLException sql error
   */
  public List<MdInfoDiffColumn> getRealColumns() throws SQLException {
    List<MdInfoDiffColumn> columns = new ArrayList<>();
    for (MdInfoDiffColumn infoColumn : getInfoColumns()) {
      if (infoColumn.isGenerated()) {
        continue;
      }
      columns.add(infoColumn);
    }

    return columns;
  }

  /**
   * has foreign key.
   * @return if true, has foreign key
   * @throws SQLException sql error
   */
  public boolean hasForeignKey() throws SQLException {
    return getInfoReferences().size() > 0;
  }

  /**
   * has trigger.
   * @return if true, has trigger
   * @throws SQLException sql error
   */
  public boolean hasTrigger() throws SQLException {
    return getInfoTriggers().size() > 0;
  }

  /**
   * get trigger hash.
   * @return trigger hash
   * @throws SQLException sql error
   */
  public String getTriggerHash() throws SQLException {
    return getInfoTriggers().stream().map(MdInfoDiffTrigger::getHash).collect(Collectors.joining());
  }

  /**
   * has referenced.
   * @return if true, has referenced
   * @throws SQLException sql error
   */
  public boolean hasReferenced() throws SQLException {
    return getReferencedTableNames().size() > 0;
  }
}
