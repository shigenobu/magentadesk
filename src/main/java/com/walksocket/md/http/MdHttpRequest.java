package com.walksocket.md.http;

import com.google.gson.annotations.Expose;
import com.walksocket.md.output.member.MdOutputMemberReflectedRecordTable;

import java.util.ArrayList;
import java.util.List;

/**
 * http request.
 */
public class MdHttpRequest {

  /**
   * run.
   */
  @Expose
  public boolean run;

  /**
   * reflected records.
   */
  @Expose
  public List<MdOutputMemberReflectedRecordTable> reflectedRecordTables = new ArrayList<>();

  /**
   * constructor.
   * @param run run
   * @param reflectedRecordTables reflected record tables
   */
  public MdHttpRequest(boolean run, List<MdOutputMemberReflectedRecordTable> reflectedRecordTables) {
    this.run = run;
    this.reflectedRecordTables = reflectedRecordTables;
  }
}
