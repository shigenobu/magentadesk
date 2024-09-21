package com.walksocket.md.filter;

import com.walksocket.md.MdInfoDiff;
import com.walksocket.md.db.MdDbConnection;
import com.walksocket.md.output.MdOutputDiff;

import java.sql.SQLException;
import java.util.List;

/**
 * diff filter abstract.
 */
public abstract class MdFilterDiffAbstract {

  /**
   * db connection.
   */
  protected MdDbConnection con;

  /**
   * constructor.
   * @param con db connection
   */
  public MdFilterDiffAbstract(MdDbConnection con) {
    this.con = con;
  }

  /**
   * filter.
   * @param baseInfoList base info list
   * @param compareInfoList compare info list
   * @param outputDiff output diff
   * @throws SQLException sql error
   */
  public abstract void filter(List<MdInfoDiff> baseInfoList, List<MdInfoDiff> compareInfoList, MdOutputDiff outputDiff) throws SQLException;
}
