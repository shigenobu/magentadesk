package com.walksocket.md;

import com.walksocket.md.db.MdDbConnection;
import com.walksocket.md.db.MdDbFactory;
import com.walksocket.md.db.MdDbFactory.DbType;
import com.walksocket.md.db.MdDbRecord;
import com.walksocket.md.exception.MdExceptionAbstract;
import com.walksocket.md.exception.MdExceptionInvalidVersion;
import com.walksocket.md.exception.MdExceptionUnknown;
import com.walksocket.md.execute.MdExecuteAbstract;
import com.walksocket.md.execute.MdExecuteDiff;
import com.walksocket.md.execute.MdExecuteMaintenance;
import com.walksocket.md.execute.MdExecuteSync;
import com.walksocket.md.input.MdInputAbstract;
import com.walksocket.md.output.MdOutputAbstract;

import java.util.List;

public class MdExecute {

  /**
   * execute.
   * @param input input object
   * @return output object
   * @throws Exception several error
   */
  public static MdOutputAbstract execute(MdInputAbstract input) throws Exception {
    String sql;
    List<MdDbRecord> records;

    Exception ex = new MdExceptionUnknown();
    try (MdDbConnection con = MdDbFactory.newCreate(input.getConnectionString())) {
      if (con.getDbType() == DbType.MYSQL) {
        // check version
        String version = null;
        sql = "SELECT @@version as version";
        records = con.getRecords(sql);
        for (MdDbRecord record : records) {
          version = record.get("version").toLowerCase();
          break;
        }
        if (MdUtils.isNullOrEmpty(version)
            || !(version.contains("8.0.31")
            || version.contains("8.0.32")
            || version.contains("8.0.33")
            || version.contains("8.0.34")
            || version.contains("8.0.35")
            || version.contains("8.0.36")
            || version.contains("8.0.37")
            || version.contains("8.0.38")
            || version.contains("8.0.39")
            || version.contains("8.0.40"))) {
          throw new MdExceptionInvalidVersion("MySQL 8.0.31, 8.0.32, 8.0.33, 8.0.34, 8.0.35, 8.0.36, 8.0.37, 8.0.38, 8.0.39, 8.0.40 is required.");
        }
      } else {
        // check version
        String version = null;
        sql = "SELECT @@version as version";
        records = con.getRecords(sql);
        for (MdDbRecord record : records) {
          version = record.get("version").toLowerCase();
          break;
        }
        if (MdUtils.isNullOrEmpty(version)
            || !version.contains("mariadb")
            || !(version.contains("10.3.")
            || version.contains("10.4.")
            || version.contains("10.5.")
            || version.contains("10.6.")
            || version.contains("10.7.")
            || version.contains("10.8.")
            || version.contains("10.9.")
            || version.contains("10.10.")
            || version.contains("10.11.")
            || version.contains("11.0.")
            || version.contains("11.1.")
            || version.contains("11.2.")
            || version.contains("11.3.")
            || version.contains("11.4.")
            || version.contains("11.5.")
            || version.contains("11.6."))) {
          throw new MdExceptionInvalidVersion("MariaDB 10.3, 10.4, 10.5, 10.6, 10.7, 10.8, 10.9, 10.10, 10.11, 11.0, 11.1, 11.2, 11.3, 11.4, 11.5, 11.6 is required.");
        }
      }

      // create database `magentadesk`.
      sql = "CREATE DATABASE IF NOT EXISTS `magentadesk`";
      con.execute(sql);

      // create table `magentadesk`.`diffSummary`.
      sql = "CREATE TABLE IF NOT EXISTS `magentadesk`.`diffSummary` (" +
          "  `summaryId` varchar(32) not null," +
          "  `baseDatabase` varchar(64) not null," +
          "  `compareDatabase` varchar(64) not null," +
          "  `created` datetime not null default current_timestamp," +
          "  primary key (`summaryId`)," +
          "  key (`baseDatabase`, `compareDatabase`)," +
          "  key (`created`)" +
          ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
      con.execute(sql);

      // create table `magentadesk`.`diffTable`.
      sql = "CREATE TABLE IF NOT EXISTS `magentadesk`.`diffTable` (" +
          "  `summaryId` varchar(32) not null," +
          "  `tableName` varchar(64) not null," +
          "  `tableComment` varchar(512)," +
          "  `columns` json," +
          "  primary key (`summaryId`, `tableName`)," +
          "  foreign key (`summaryId`) references `magentadesk`.`diffSummary` (`summaryId`) on delete cascade" +
          ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
      con.execute(sql);

      if (con.getDbType() == DbType.MYSQL) {
        // create table `magentadesk`.`diffSequence`.
        sql = "CREATE TABLE IF NOT EXISTS `magentadesk`.`diffSequence` (" +
          "  `id` bigint not null auto_increment," +
          "  primary key (`id`)" +
          ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
        con.execute(sql);

        // create function `magentadesk`.`nextDiffSeq`.
        sql = "CREATE FUNCTION IF NOT EXISTS `magentadesk`.`nextDiffSeq` () RETURNS INTEGER DETERMINISTIC" +
          "  BEGIN" +
          "    DECLARE lastId bigint;" +
          "    SELECT id INTO lastId FROM `magentadesk`.`diffSequence`;" +
          "    IF lastId IS NULL THEN INSERT IGNORE INTO `magentadesk`.`diffSequence` (`id`) VALUES (0);" +
          "    ELSEIF lastId > 100000000 THEN DELETE FROM `magentadesk`.`diffSequence`; INSERT IGNORE INTO `magentadesk`.`diffSequence` (`id`) VALUES (0);" +
          "    END IF;" +
          "    UPDATE `magentadesk`.`diffSequence` SET `id` = LAST_INSERT_ID(`id` + 1);" +
          "    SELECT LAST_INSERT_ID() INTO lastId;" +
          "    RETURN lastId;" +
          "  END";
        con.execute(sql);

        // create table `magentadesk`.`diffRecord`.
        sql = "CREATE TABLE IF NOT EXISTS `magentadesk`.`diffRecord` (" +
            "  `summaryId` varchar(32) not null," +
            "  `tableName` varchar(64) not null," +
            "  `diffSeq` bigint not null," +
            "  `baseValues` json," +
            "  `compareValues` json," +
            "  primary key (`summaryId`, `tableName`, `diffSeq`)," +
            "  unique key (`diffSeq`)," +
            "  foreign key (`summaryId`) references `magentadesk`.`diffSummary` (`summaryId`) on delete cascade" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
        con.execute(sql);

      } else {
        // create sequence `magentadesk`.`diffSequence`.
        sql = "CREATE SEQUENCE IF NOT EXISTS `magentadesk`.`diffSequence` increment by 0 cycle;";
        con.execute(sql);

        // create table `magentadesk`.`diffRecord`.
        sql = "CREATE TABLE IF NOT EXISTS `magentadesk`.`diffRecord` (" +
            "  `summaryId` varchar(32) not null," +
            "  `tableName` varchar(64) not null," +
            "  `diffSeq` bigint not null," +
            "  `baseValues` longblob," +
            "  `compareValues` longblob," +
            "  primary key (`summaryId`, `tableName`, `diffSeq`)," +
            "  unique key (`diffSeq`)," +
            "  foreign key (`summaryId`) references `magentadesk`.`diffSummary` (`summaryId`) on delete cascade" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
        con.execute(sql);
      }

      // delete from `magentadesk`.`diffSummary` where expired.
      sql = "DELETE FROM `magentadesk`.`diffSummary` WHERE `created` < (NOW() - INTERVAL 10800 SECOND)";
      con.execute(sql);

      // create table `magentadesk`.`diffLock`.
      sql = "CREATE TABLE IF NOT EXISTS `magentadesk`.`diffLock` (" +
          "  `baseDatabase` varchar(64) not null," +
          "  `compareDatabase` varchar(64) not null," +
          "  primary key (`baseDatabase`, `compareDatabase`)" +
          ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
      con.execute(sql);

      // create table `magentadesk`.`diffMaintenance`.
      sql = "CREATE TABLE IF NOT EXISTS `magentadesk`.`diffMaintenance` (" +
          "  `baseDatabase` varchar(64) not null," +
          "  `compareDatabase` varchar(64) not null," +
          "  `maintenance` enum('on', 'off') not null default 'off'," +
          "  primary key (`baseDatabase`, `compareDatabase`)" +
          ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
      con.execute(sql);

      // begin
      con.begin();

      // execute
      MdExecuteAbstract execute = null;
      MdMode mode = input.getMode();
      if (mode == MdMode.DIFF) {
        execute = new MdExecuteDiff(con);
      } else if (mode == MdMode.SYNC) {
        execute = new MdExecuteSync(con);
      } else if (mode == MdMode.MAINTENANCE) {
        execute = new MdExecuteMaintenance(con);
      }
      MdOutputAbstract output = execute.execute(input);

      return output;

    } catch (MdExceptionAbstract e) {
      // rollback
      MdLogger.error(e);
      ex = e;
    }

    // throw
    throw ex;
  }
}
