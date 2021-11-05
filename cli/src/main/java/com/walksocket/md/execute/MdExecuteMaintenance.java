package com.walksocket.md.execute;

import com.walksocket.md.input.MdInputAbstract;
import com.walksocket.md.input.MdInputMaintenance;
import com.walksocket.md.mariadb.MdMariadbConnection;
import com.walksocket.md.mariadb.MdMariadbUtils;
import com.walksocket.md.output.MdOutputAbstract;
import com.walksocket.md.output.MdOutputMaintenance;

/**
 * execute maintenance.
 */
public class MdExecuteMaintenance extends MdExecuteAbstract {

  /**
   * constructor.
   * @param con mariadb connection
   */
  public MdExecuteMaintenance(MdMariadbConnection con) {
    super(con);
  }

  @Override
  public MdOutputAbstract execute(MdInputAbstract input) throws Exception {
    MdInputMaintenance inputMaintenance = (MdInputMaintenance) input;
    MdOutputMaintenance outputMaintenance = new MdOutputMaintenance();
    String sql = "";

    // -----
    // lock
    lock(inputMaintenance.baseDatabase, inputMaintenance.compareDatabase);

    // -----
    // insert `magentadesk`.`diffMaintenance`.
    sql = String.format(
        "INSERT IGNORE INTO `magentadesk`.`diffMaintenance`" +
            " (`baseDatabase`, `compareDatabase`, `maintenance`) " +
            "VALUES " +
            "('%s', '%s', '%s') " +
            "ON DUPLICATE KEY UPDATE `maintenance` = VALUES (`maintenance`)",
        MdMariadbUtils.quote(inputMaintenance.baseDatabase),
        MdMariadbUtils.quote(inputMaintenance.compareDatabase),
        MdMariadbUtils.quote(inputMaintenance.maintenance));
    con.execute(sql);

    // -----
    // maintenance
    outputMaintenance.maintenance = inputMaintenance.maintenance;

    // -----
    // commit
    con.commit();

    return outputMaintenance;
  }
}
