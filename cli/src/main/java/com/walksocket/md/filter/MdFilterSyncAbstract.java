package com.walksocket.md.filter;

import com.walksocket.md.MdInfoSync;
import com.walksocket.md.db.MdDbConnection;
import com.walksocket.md.output.MdOutputSync;
import java.sql.SQLException;
import java.util.List;

/**
 * filter sync abstract.
 */
public abstract class MdFilterSyncAbstract {

  /**
   * db connection.
   */
  protected MdDbConnection con;

  /**
   * constructor.
   *
   * @param con db connection
   */
  public MdFilterSyncAbstract(MdDbConnection con) {
    this.con = con;
  }

  /**
   * filter.
   *
   * @param infoList   info list
   * @param outputSync output sync
   * @throws SQLException sql error
   */
  public abstract void filter(List<MdInfoSync> infoList, MdOutputSync outputSync)
      throws SQLException;
}
