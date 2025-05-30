package com.walksocket.md.output;

import com.google.gson.annotations.Expose;
import com.walksocket.md.output.member.MdOutputMemberExistsOnlyBaseTable;
import com.walksocket.md.output.member.MdOutputMemberExistsOnlyCompareTable;
import com.walksocket.md.output.member.MdOutputMemberForceExcludeTable;
import com.walksocket.md.output.member.MdOutputMemberIncorrectDefinitionTables;
import com.walksocket.md.output.member.MdOutputMemberMatchTables;
import com.walksocket.md.output.member.MdOutputMemberMismatchDefinitionTable;
import com.walksocket.md.output.member.MdOutputMemberMismatchRecordTable;
import java.util.ArrayList;
import java.util.List;

/**
 * output diff.
 */
public class MdOutputDiff extends MdOutputAbstract {

  /**
   * exist only base tables.
   */
  @Expose
  public List<MdOutputMemberExistsOnlyBaseTable> existsOnlyBaseTables = new ArrayList<>();

  /**
   * exist only compare tables.
   */
  @Expose
  public List<MdOutputMemberExistsOnlyCompareTable> existsOnlyCompareTables = new ArrayList<>();

  /**
   * force exclude tables.
   */
  @Expose
  public List<MdOutputMemberForceExcludeTable> forceExcludeTables = new ArrayList<>();

  /**
   * incorrect tables.
   */
  @Expose
  public List<MdOutputMemberIncorrectDefinitionTables> incorrectDefinitionTables = new ArrayList<>();

  /**
   * mismatch definition tables.
   */
  @Expose
  public List<MdOutputMemberMismatchDefinitionTable> mismatchDefinitionTables = new ArrayList<>();

  /**
   * mismatch record tables.
   */
  @Expose
  public List<MdOutputMemberMismatchRecordTable> mismatchRecordTables = new ArrayList<>();

  /**
   * match tables.
   */
  @Expose
  public List<MdOutputMemberMatchTables> matchTables = new ArrayList<>();

  /**
   * summary id.
   */
  @Expose
  public String summaryId;
}
