package com.walksocket.md.filter;

import com.walksocket.md.MdInfoSync;
import com.walksocket.md.mariadb.MdMariadbConnection;
import com.walksocket.md.output.MdOutputSync;

import java.sql.SQLException;
import java.util.List;

/**
 * filter sync abstract.
 */
public abstract class MdFilterSyncAbstract {

  /**
   * mariadb connection.
   */
  protected MdMariadbConnection con;

  /**
   * constructor.
   * @param con mariadb connection
   */
  public MdFilterSyncAbstract(MdMariadbConnection con) {
    this.con = con;
  }

  /**
   * filter.
   * @param infoList info list
   * @param outputSync output sync
   * @throws SQLException sql error
   */
  public abstract void filter(List<MdInfoSync> infoList, MdOutputSync outputSync) throws SQLException;
}
