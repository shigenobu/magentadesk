package com.walksocket.md.filter;

import com.walksocket.md.MdInfoDiff;
import com.walksocket.md.db.MdDbConnection;
import com.walksocket.md.output.MdOutputDiff;
import com.walksocket.md.output.member.MdOutputMemberMismatchDefinitionTable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * filter diff mismatch definition.
 */
public class MdFilterDiffMismatchDefinitionTables extends MdFilterDiffAbstract {

  /**
   * constructor.
   * @param con db connection
   */
  public MdFilterDiffMismatchDefinitionTables(MdDbConnection con) {
    super(con);
  }

  @Override
  public void filter(List<MdInfoDiff> baseInfoList, List<MdInfoDiff> compareInfoList, MdOutputDiff outputDiff) throws SQLException {
    List<MdInfoDiff> removedBaseInfoList = new ArrayList<>();
    List<MdInfoDiff> removedCompareInfoLIst = new ArrayList<>();

    for (MdInfoDiff baseInfo : baseInfoList) {
      MdInfoDiff compareInfo = compareInfoList
          .stream()
          .filter(i -> i.getTableName().equals(baseInfo.getTableName()))
          .findFirst()
          .get();

      // definition
      if (baseInfo.getDefinition().equals(compareInfo.getDefinition())) {
        continue;
      }

      // structure hash
      if (!baseInfo.getStructureHash().equals(compareInfo.getStructureHash())) {
        outputDiff.mismatchDefinitionTables.add(
            new MdOutputMemberMismatchDefinitionTable(baseInfo, compareInfo));

        removedBaseInfoList.add(baseInfo);
        removedCompareInfoLIst.add(compareInfo);
      }
    }
    for (MdInfoDiff info : removedBaseInfoList) {
      baseInfoList.remove(info);
    }
    for (MdInfoDiff info : removedCompareInfoLIst) {
      compareInfoList.remove(info);
    }

    outputDiff.mismatchDefinitionTables.sort(Comparator.comparing(t -> t.tableName));
  }
}
