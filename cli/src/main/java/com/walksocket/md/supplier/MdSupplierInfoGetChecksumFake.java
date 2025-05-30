package com.walksocket.md.supplier;

import com.walksocket.md.MdLogger;

/**
 * supplier for checksum fake.
 */
public class MdSupplierInfoGetChecksumFake extends MdSupplierInfoGetChecksum {

  /**
   * constructor.
   *
   * @param connectionString connection string
   * @param database         database
   * @param tableName        table name
   */
  public MdSupplierInfoGetChecksumFake(String connectionString, String database, String tableName) {
    super(connectionString, database, tableName);

    checksum = String.format("fake_%s_%s", database, tableName);
    MdLogger.trace("FAKE CHECKSUM " + checksum);
  }
}
