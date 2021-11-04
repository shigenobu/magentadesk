package com.walksocket.md.cli.filter;

import com.walksocket.md.cli.MdInfoDiff;
import com.walksocket.md.cli.mariadb.MdMariadbConnection;
import com.walksocket.md.cli.output.MdOutputDiff;
import com.walksocket.md.cli.output.member.MdOutputMemberIncorrectDefinitionTables;
import com.walksocket.md.cli.info.MdInfoDiffColumn;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * filter diff incorrect definition.
 */
public class MdFilterDiffIncorrectDefinitionTables extends MdFilterDiffAbstract {

  /**
   * constructor.
   * @param con mariadb connection
   */
  public MdFilterDiffIncorrectDefinitionTables(MdMariadbConnection con) {
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

      // primary check
      boolean hasPrimary = false;
      for (MdInfoDiffColumn infoColumn : baseInfo.getRealColumns()) {
        if (infoColumn.isPrimary()) {
          hasPrimary = true;
          break;
        }
      }
      if (!hasPrimary) {
        outputDiff.incorrectDefinitionTables.add(
            new MdOutputMemberIncorrectDefinitionTables(
                baseInfo,
                MdOutputMemberIncorrectDefinitionTables.MdOutputMemberIncorrectReason.NO_PRIMARY_KEY));

        removedBaseInfoList.add(baseInfo);
        removedCompareInfoLIst.add(compareInfo);
        continue;
      }

      // foreign check
      if (baseInfo.hasForeignKey()) {
        outputDiff.incorrectDefinitionTables.add(
            new MdOutputMemberIncorrectDefinitionTables(
                baseInfo,
                MdOutputMemberIncorrectDefinitionTables.MdOutputMemberIncorrectReason.HAS_FOREIGN_KEY));

        removedBaseInfoList.add(baseInfo);
        removedCompareInfoLIst.add(compareInfo);
        continue;
      }

      // charset check
      if (!baseInfo.getInfoTable().isValidCollation()
          || !compareInfo.getInfoTable().isValidCollation()
          || baseInfo.getRealColumns().stream().filter(c -> !c.isValidCollation()).findFirst().isPresent()
          || compareInfo.getRealColumns().stream().filter(c -> !c.isValidCollation()).findFirst().isPresent()) {
        outputDiff.incorrectDefinitionTables.add(
            new MdOutputMemberIncorrectDefinitionTables(
                baseInfo,
                MdOutputMemberIncorrectDefinitionTables.MdOutputMemberIncorrectReason.INVALID_CHARSET));

        removedBaseInfoList.add(baseInfo);
        removedCompareInfoLIst.add(compareInfo);
        continue;
      }
    }
    for (MdInfoDiff info : removedBaseInfoList) {
      baseInfoList.remove(info);
    }
    for (MdInfoDiff info : removedCompareInfoLIst) {
      compareInfoList.remove(info);
    }

    outputDiff.incorrectDefinitionTables.sort(Comparator.comparing(t -> t.tableName));
  }
}
