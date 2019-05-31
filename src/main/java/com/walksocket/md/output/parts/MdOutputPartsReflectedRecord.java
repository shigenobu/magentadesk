package com.walksocket.md.output.parts;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * output reflected record.
 */
public class MdOutputPartsReflectedRecord {

  /**
   * diff seq.
   */
  @Expose
  public long diffSeq;

  /**
   * values.
   */
  @Expose
  public List<String> values;

  /**
   * constructor.
   * @param partsRecord parts record
   */
  public MdOutputPartsReflectedRecord(MdOutputPartsRecord partsRecord) {
    this.diffSeq = partsRecord.diffSeq;
    this.values = partsRecord.baseValues;
  }
}
