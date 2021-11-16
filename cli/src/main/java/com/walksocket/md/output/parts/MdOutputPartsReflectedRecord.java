package com.walksocket.md.output.parts;

import com.google.gson.annotations.Expose;
import com.walksocket.md.MdValue;

import java.util.ArrayList;
import java.util.List;

/**
 * output reflected record.
 */
public class MdOutputPartsReflectedRecord extends MdValue {

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
   * changes.
   */
  @Expose
  public List<Boolean> changes;

  /**
   * constructor.
   * @param partsRecord parts record
   */
  public MdOutputPartsReflectedRecord(MdOutputPartsRecord partsRecord) {
    this.diffSeq = partsRecord.diffSeq;
    this.values = partsRecord.baseValues;
    this.changes = new ArrayList<>();

    for (int i = 0; i < partsRecord.baseValues.size(); i++) {
      String bValue = partsRecord.baseValues.get(i);
      String cValue = partsRecord.compareValues.get(i);

      if (bValue == null && cValue == null) {
        changes.add(false);
      } else if (bValue != null && cValue == null){
        changes.add(true);
      } else if (bValue == null && cValue != null){
        changes.add(true);
      } else if (bValue.equals(cValue)) {
        changes.add(false);
      } else {
        changes.add(true);
      }
    }
  }
}
