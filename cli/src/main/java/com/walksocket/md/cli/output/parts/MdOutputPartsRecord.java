package com.walksocket.md.cli.output.parts;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * output record.
 */
public class MdOutputPartsRecord {

  /**
   * diff seq.
   */
  @Expose
  public long diffSeq;

  /**
   * base database values.
   */
  @Expose
  public List<String> baseValues;

  /**
   * compare database values.
   */
  @Expose
  public List<String> compareValues;

  /**
   * constructor.
   * @param diffSeq diff seq
   * @param baseValues base values
   * @param compareValues compare values
   */
  public MdOutputPartsRecord(long diffSeq, List<String> baseValues, List<String> compareValues) {
    this.diffSeq = diffSeq;
    this.baseValues = baseValues;
    this.compareValues = compareValues;
  }
}
