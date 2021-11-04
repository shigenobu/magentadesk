package com.walksocket.md.cli.bash;

import com.google.gson.annotations.Expose;

/**
 * bash stdin.
 */
public class MdBashStdin {

  /**
   * run.
   */
  @Expose
  public boolean run;

  /**
   * reflected json path.
   */
  @Expose
  public String reflectedJsonPath;

  /**
   * constructor.
   * @param run run
   * @param reflectedJsonPath reflected json path
   */
  public MdBashStdin(boolean run, String reflectedJsonPath) {
    this.run = run;
    this.reflectedJsonPath = reflectedJsonPath;
  }
}
