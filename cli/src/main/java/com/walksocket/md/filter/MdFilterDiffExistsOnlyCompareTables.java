package com.walksocket.md.filter;

import com.walksocket.md.MdInfoDiff;
import com.walksocket.md.mariadb.MdMariadbConnection;
import com.walksocket.md.output.MdOutputDiff;
import com.walksocket.md.output.member.MdOutputMemberExistsOnlyCompareTable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * filter diff exists only compare.
 */
public class MdFilterDiffExistsOnlyCompareTables extends MdFilterDiffAbstract {

  /**
   * constructor.
   * @param con mariadb connection
   */
  public MdFilterDiffExistsOnlyCompareTables(MdMariadbConnection con) {
    super(con);
  }

  @Override
  public void filter(List<MdInfoDiff> baseInfoList, List<MdInfoDiff> compareInfoList, MdOutputDiff outputDiff) throws SQLException {
    List<MdInfoDiff> removedCompareInfoLIst = new ArrayList<>();

    List<String> baseTableNames = baseInfoList.stream().map(m -> m.getTableName()).collect(Collectors.toList());
    for (MdInfoDiff compareInfo : compareInfoList) {
      String compareTableName = compareInfo.getTableName();
      if (!baseTableNames.contains(compareTableName)) {
        outputDiff.existsOnlyCompareTables.add(
            new MdOutputMemberExistsOnlyCompareTable(compareInfo));

        removedCompareInfoLIst.add(compareInfo);
      }
    }
    for (MdInfoDiff info : removedCompareInfoLIst) {
      compareInfoList.remove(info);
    }

    outputDiff.existsOnlyCompareTables.sort(Comparator.comparing(t -> t.tableName));
  }
}
