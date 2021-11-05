package com.walksocket.md.supplier;

import com.walksocket.md.MdLogger;
import com.walksocket.md.db.MdDbRecord;
import com.walksocket.md.mariadb.MdMariadbConnection;

import java.util.List;
import java.util.function.Supplier;

/**
 * supplier for checksum.
 */
public class MdSupplierInfoGetChecksum implements Supplier<String> {

  /**
   * connection string.
   */
  private String connectionString;

  /**
   * database.
   */
  private String database;

  /**
   * table name.
   */
  private String tableName;

  /**
   * checksum.
   * <pre>
   *   CHECKSUM TABLE ...
   * </pre>
   */
  protected String checksum;

  /**
   * constructor.
   * @param connectionString connection string
   * @param database database
   * @param tableName table name
   */
  public MdSupplierInfoGetChecksum(String connectionString, String database, String tableName) {
    this.connectionString = connectionString;
    this.database = database;
    this.tableName = tableName;
  }

  @Override
  public String get() {
    if (checksum == null) {
      try (MdMariadbConnection con = new MdMariadbConnection(connectionString)) {
        String sql = String.format(
            "CHECKSUM TABLE `%s`.`%s`",
            database,
            tableName);
        List<MdDbRecord> records = con.getRecords(sql);
        for (MdDbRecord record : records) {
          checksum = record.get("Checksum");
        }
      } catch (Exception e) {
        MdLogger.error(e);
      }
    }
    return checksum;
  }
}
