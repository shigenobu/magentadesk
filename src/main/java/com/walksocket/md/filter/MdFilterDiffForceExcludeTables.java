package com.walksocket.md.filter;

import com.walksocket.md.MdInfoDiff;
import com.walksocket.md.info.MdInfoDiffTrigger;
import com.walksocket.md.mariadb.MdMariadbConnection;
import com.walksocket.md.output.MdOutputDiff;
import com.walksocket.md.output.member.MdOutputMemberForceExcludeTable;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * filter diff force exclude.
 */
public class MdFilterDiffForceExcludeTables extends MdFilterDiffAbstract {

  /**
   * constructor.
   * @param con mariadb connection
   */
  public MdFilterDiffForceExcludeTables(MdMariadbConnection con) {
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

      // view
      if (baseInfo.getInfoTable().isView()) {
        outputDiff.forceExcludeTables.add(
            new MdOutputMemberForceExcludeTable(
                baseInfo,
                MdOutputMemberForceExcludeTable.MdOutputMemberForceExcldeReason.IS_VIEW));

        removedBaseInfoList.add(baseInfo);
        removedCompareInfoLIst.add(compareInfo);
        continue;
      }

      // sequence
      if (baseInfo.getInfoTable().isSequence()) {
        outputDiff.forceExcludeTables.add(
            new MdOutputMemberForceExcludeTable(
                baseInfo,
                MdOutputMemberForceExcludeTable.MdOutputMemberForceExcldeReason.IS_SEQUENCE));

        removedBaseInfoList.add(baseInfo);
        removedCompareInfoLIst.add(compareInfo);
        continue;
      }

      // innodb
      if (!baseInfo.getInfoTable().isInnodb()) {
        outputDiff.forceExcludeTables.add(
            new MdOutputMemberForceExcludeTable(
                baseInfo,
                MdOutputMemberForceExcludeTable.MdOutputMemberForceExcldeReason.NOT_INNODB));

        removedBaseInfoList.add(baseInfo);
        removedCompareInfoLIst.add(compareInfo);
        continue;
      }

      // foreign had
      boolean hasBaseReferenced = baseInfo.getReferencedTableNames().size() > 0;
      boolean hasCompareReferenced = compareInfo.getReferencedTableNames().size() > 0;
      if (hasBaseReferenced || hasCompareReferenced) {
        outputDiff.forceExcludeTables.add(
            new MdOutputMemberForceExcludeTable(
                baseInfo,
                MdOutputMemberForceExcludeTable.MdOutputMemberForceExcldeReason.REFERENCED_FOREIGH_KEY));

        removedBaseInfoList.add(baseInfo);
        removedCompareInfoLIst.add(compareInfo);
        continue;
      }

      // trigger
      String baseTriggerHash = null;
      String compareTriggerHash = null;
      if (baseInfo.getInfoTriggers().size() > 0) {
        baseTriggerHash = baseInfo.getInfoTriggers().stream().map(MdInfoDiffTrigger::getHash).collect(Collectors.joining());
      }
      if (compareInfo.getInfoTriggers().size() > 0) {
        compareTriggerHash = compareInfo.getInfoTriggers().stream().map(MdInfoDiffTrigger::getHash).collect(Collectors.joining());
      }
      if (baseTriggerHash == null && compareTriggerHash == null) {
        continue;
      }
      if ((baseTriggerHash != null && compareTriggerHash == null)
          || (baseTriggerHash == null && compareTriggerHash != null)
          || !baseTriggerHash.equals(compareTriggerHash)) {
        outputDiff.forceExcludeTables.add(
            new MdOutputMemberForceExcludeTable(
                baseInfo,
                MdOutputMemberForceExcludeTable.MdOutputMemberForceExcldeReason.MISMATCH_TRIGGER));

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

    outputDiff.forceExcludeTables.sort(Comparator.comparing(t -> t.tableName));
  }
}
