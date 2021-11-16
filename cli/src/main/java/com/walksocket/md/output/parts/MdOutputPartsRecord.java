package com.walksocket.md.output.parts;

import com.google.gson.annotations.Expose;
import com.walksocket.md.MdValue;

import java.util.List;

/**
 * output record.
 */
public class MdOutputPartsRecord extends MdValue {

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
