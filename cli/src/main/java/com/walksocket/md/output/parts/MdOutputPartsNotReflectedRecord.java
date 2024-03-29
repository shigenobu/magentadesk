package com.walksocket.md.output.parts;

import com.google.gson.annotations.Expose;
import com.walksocket.md.MdValue;

import java.util.List;

/**
 * output not reflected record.
 */
public class MdOutputPartsNotReflectedRecord extends MdValue {

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
   * @param partsRecord parts record
   */
  public MdOutputPartsNotReflectedRecord(MdOutputPartsRecord partsRecord) {
    this.diffSeq = partsRecord.diffSeq;
    this.baseValues = partsRecord.baseValues;
    this.compareValues = partsRecord.compareValues;
  }
}
