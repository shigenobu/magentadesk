package com.walksocket.md.filter;

import com.walksocket.md.MdInfoDiff;
import com.walksocket.md.db.MdDbConnection;
import com.walksocket.md.output.MdOutputDiff;
import com.walksocket.md.output.member.MdOutputMemberExistsOnlyBaseTable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * filter diff exists only base.
 */
public class MdFilterDiffExistsOnlyBaseTables extends MdFilterDiffAbstract {

  /**
   * constructor.
   * @param con db connection
   */
  public MdFilterDiffExistsOnlyBaseTables(MdDbConnection con) {
    super(con);
  }

  @Override
  public void filter(List<MdInfoDiff> baseInfoList, List<MdInfoDiff> compareInfoList, MdOutputDiff outputDiff) throws SQLException {
    List<MdInfoDiff> removedBaseInfoLIst = new ArrayList<>();

    List<String> compareTableNames = compareInfoList.stream().map(m -> m.getTableName()).collect(Collectors.toList());
    for (MdInfoDiff baseInfo : baseInfoList) {
      String baseTableName = baseInfo.getTableName();
      if (!compareTableNames.contains(baseTableName)) {
        outputDiff.existsOnlyBaseTables.add(
            new MdOutputMemberExistsOnlyBaseTable(baseInfo));

        removedBaseInfoLIst.add(baseInfo);
      }
    }
    for (MdInfoDiff info : removedBaseInfoLIst) {
      baseInfoList.remove(info);
    }

    outputDiff.existsOnlyBaseTables.sort(Comparator.comparing(t -> t.tableName));
  }
}
