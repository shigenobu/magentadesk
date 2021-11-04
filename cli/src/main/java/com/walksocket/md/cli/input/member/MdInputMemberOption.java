package com.walksocket.md.cli.input.member;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * input option.
 */
public class MdInputMemberOption {

  /**
   * include like patterns.
   * <pre>
   *   or condition
   * </pre>
   */
  @Expose
  public List<String> includeTableLikePatterns = new ArrayList<>();

  /**
   * exclude like patterns.
   * <pre>
   *   and condition
   * </pre>
   */
  @Expose
  public List<String> excludeTableLikePatterns = new ArrayList<>();

  /**
   * ignore auto commit value on structure.
   */
  @Expose
  public boolean ignoreAutoIncrement;

  /**
   * ignore table, column, index and partition comment on structure.
   */
  @Expose
  public boolean ignoreComment;

  /**
   * ignore partition value on structure.
   */
  @Expose
  public boolean ignorePartitions;

  /**
   * ignore column default sequence definition on structure.
   */
  @Expose
  public boolean ignoreDefaultForSequence;
}
