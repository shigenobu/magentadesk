package com.walksocket.md.output;

import com.google.gson.annotations.Expose;
import com.walksocket.md.output.member.MdOutputMemberCommandResult;
import com.walksocket.md.output.member.MdOutputMemberHttpResult;
import com.walksocket.md.output.member.MdOutputMemberNotReflectedRecordTable;
import com.walksocket.md.output.member.MdOutputMemberReflectedRecordTable;
import java.util.ArrayList;
import java.util.List;

/**
 * output sync.
 */
public class MdOutputSync extends MdOutputAbstract {

  /**
   * reflected records.
   */
  @Expose
  public List<MdOutputMemberReflectedRecordTable> reflectedRecordTables = new ArrayList<>();

  /**
   * not reflected records.
   */
  @Expose
  public List<MdOutputMemberNotReflectedRecordTable> notReflectedRecordTables = new ArrayList<>();

  /**
   * command results before commit.
   */
  @Expose
  public List<MdOutputMemberCommandResult> commandResultsBeforeCommit = new ArrayList<>();

  /**
   * command results after commit.
   */
  @Expose
  public List<MdOutputMemberCommandResult> commandResultsAfterCommit = new ArrayList<>();

  /**
   * http results before commit.
   */
  @Expose
  public List<MdOutputMemberHttpResult> httpResultsBeforeCommit = new ArrayList<>();

  /**
   * http results after commit.
   */
  @Expose
  public List<MdOutputMemberHttpResult> httpResultsAfterCommit = new ArrayList<>();

  /**
   * summary id.
   */
  @Expose
  public String summaryId;
}
