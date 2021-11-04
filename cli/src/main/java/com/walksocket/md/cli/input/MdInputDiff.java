package com.walksocket.md.cli.input;

import com.google.gson.annotations.Expose;
import com.walksocket.md.cli.MdMode;
import com.walksocket.md.cli.MdUtils;
import com.walksocket.md.cli.exception.MdExceptionInvalidInput;
import com.walksocket.md.cli.input.member.MdInputMemberCondition;
import com.walksocket.md.cli.input.member.MdInputMemberOption;

import java.util.ArrayList;
import java.util.List;

/**
 * input diff.
 */
public class MdInputDiff extends MdInputAbstract {

  /**
   * base database.
   */
  @Expose
  public String baseDatabase;

  /**
   * compare database.
   */
  @Expose
  public String compareDatabase;

  /**
   * option.
   */
  @Expose
  public MdInputMemberOption option = new MdInputMemberOption();

  /**
   * condition list.
   */
  @Expose
  public List<MdInputMemberCondition> conditions = new ArrayList<>();

  @Override
  public MdMode getMode() {
    return MdMode.DIFF;
  }

  @Override
  public void validate() throws MdExceptionInvalidInput {
    super.validate();

    if (MdUtils.isNullOrEmpty(baseDatabase)) {
      throw new MdExceptionInvalidInput("Invalid baseDatabase.");
    }
    if (MdUtils.isNullOrEmpty(compareDatabase)) {
      throw new MdExceptionInvalidInput("Invalid compareDatabase.");
    }

    if (option == null) {
      option = new MdInputMemberOption();
    }
    if (option.includeTableLikePatterns == null) {
      option.includeTableLikePatterns = new ArrayList<>();
    }
    if (option.excludeTableLikePatterns == null) {
      option.excludeTableLikePatterns = new ArrayList<>();
    }

    if (conditions == null) {
      conditions = new ArrayList<>();
    }
  }
}
