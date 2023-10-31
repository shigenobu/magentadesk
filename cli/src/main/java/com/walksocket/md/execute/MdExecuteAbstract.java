package com.walksocket.md.execute;

import com.walksocket.md.MdEnv;
import com.walksocket.md.MdLogger;
import com.walksocket.md.db.MdDbRecord;
import com.walksocket.md.exception.MdExceptionDisallowSimultaneousExecution;
import com.walksocket.md.exception.MdExceptionInMaintenance;
import com.walksocket.md.input.MdInputAbstract;
import com.walksocket.md.input.MdInputMaintenance;
import com.walksocket.md.mariadb.MdMariadbConnection;
import com.walksocket.md.mariadb.MdMariadbUtils;
import com.walksocket.md.output.MdOutputAbstract;

import java.sql.SQLException;
import java.util.List;

/**
 * execute abstract.
 */
public abstract class MdExecuteAbstract {

  /**
   * mariadb connection.
   */
  protected MdMariadbConnection con;

  /**
   * constructor.
   * @param con mariadb connection
   */
  public MdExecuteAbstract(MdMariadbConnection con) {
    this.con = con;
  }

  /**
   * execute.
   * @param input input
   * @return output
   * @throws Exception sql error
   */
  public abstract MdOutputAbstract execute(MdInputAbstract input) throws Exception;

  /**
   * lock.
   * @param baseDatabase base database
   * @param compareDatabase compare database
   * @throws Exception several error
   */
  protected void lock(String baseDatabase, String compareDatabase) throws Exception {
    String sql;

    // insert `magentadesk`.`diffLock`.
    sql = String.format(
        "SELECT * FROM `magentadesk`.`diffLock` WHERE `baseDatabase` = '%s' AND `compareDatabase` = '%s'",
        MdMariadbUtils.quote(baseDatabase),
        MdMariadbUtils.quote(compareDatabase));
    MdDbRecord record = con.getRecord(sql);
    if (record == null) {
      sql = String.format(
          "INSERT IGNORE INTO `magentadesk`.`diffLock` (`baseDatabase`, `compareDatabase`) VALUES ('%s', '%s')",
          MdMariadbUtils.quote(baseDatabase),
          MdMariadbUtils.quote(compareDatabase));
      con.execute(sql);
    }

    // lock `magentadesk`.`diffLock`.
    try {
      String noWait = "NOWAIT";
      if (MdEnv.isWait()) {
        noWait = "";
      }

      sql = String.format(
          "SELECT `baseDatabase`, `compareDatabase` " +
              "FROM `magentadesk`.`diffLock` " +
              "WHERE `baseDatabase` = '%s' and `compareDatabase` = '%s' " +
              "FOR UPDATE %s",
          MdMariadbUtils.quote(baseDatabase),
          MdMariadbUtils.quote(compareDatabase),
          noWait);
      con.getRecord(sql);
    } catch (SQLException e) {
      MdLogger.error(e);
      throw new MdExceptionDisallowSimultaneousExecution(
          "Pair of baseDatabase and compareDatabase do not execute simultaneously.");
    }
  }

  /**
   * check maintenance.
   * @param baseDatabase base database
   * @param compareDatabase compare database
   * @throws Exception several error
   */
  protected void checkMaintenance(String baseDatabase, String compareDatabase) throws Exception {
    String sql;

    // insert `magentadesk`.`diffMaintenance`.
    sql = String.format(
        "INSERT IGNORE INTO `magentadesk`.`diffMaintenance` (`baseDatabase`, `compareDatabase`) VALUES ('%s', '%s')",
        MdMariadbUtils.quote(baseDatabase),
        MdMariadbUtils.quote(compareDatabase));
    con.execute(sql);

    // check `magentadesk`.`diffMaintenance`.
    sql = String.format(
        "SELECT `maintenance` " +
            "FROM `magentadesk`.`diffMaintenance` " +
            "WHERE `baseDatabase` = '%s' and `compareDatabase` = '%s'",
        MdMariadbUtils.quote(baseDatabase),
        MdMariadbUtils.quote(compareDatabase));
    List<MdDbRecord> records = con.getRecords(sql);
    for (MdDbRecord record : records) {
      String maintenance = record.get("maintenance");
      if (maintenance.equals(MdInputMaintenance.Maintenance.ON.getMaintenance())) {
        throw new MdExceptionInMaintenance(
            "In maintenance, baseDatabase and compareDatabase.");
      }
    }
  }
}
