package com.walksocket.md.cli.input;

import com.google.gson.annotations.Expose;
import com.walksocket.md.cli.MdMode;
import com.walksocket.md.cli.MdUtils;
import com.walksocket.md.cli.exception.MdExceptionInvalidInput;

import java.util.Arrays;

/**
 * input maintenance.
 */
public class MdInputMaintenance extends MdInputAbstract {

  /**
   * maintenance.
   */
  public enum Maintenance {

    /**
     * ON.
     */
    ON("on"),

    /**
     * OFF.
     */
    OFF("off"),
    ;

    /**
     * maintenance.
     */
    private String maintenance;

    /**
     * constructor.
     * @param maintenance maintenance
     */
    Maintenance(String maintenance) {
      this.maintenance = maintenance;
    }

    /**
     * get maintenance.
     * @return maintenance
     */
    public String getMaintenance() {
      return maintenance;
    }
  }

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
   * maintenance.
   */
  @Expose
  public String maintenance;

  @Override
  public MdMode getMode() {
    return MdMode.MAINTENANCE;
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
    if (MdUtils.isNullOrEmpty(maintenance)
      || !Arrays.asList(Maintenance.values()).stream().filter(m -> m.getMaintenance().equals(maintenance)).findFirst().isPresent()) {
      throw new MdExceptionInvalidInput("Invalid maintenance.");
    }
  }
}
