package com.walksocket.md.cli;

import com.walksocket.md.cli.exception.MdExceptionAbstract;
import com.walksocket.md.cli.input.MdInputDiff;
import com.walksocket.md.cli.input.MdInputMaintenance;
import com.walksocket.md.cli.output.MdOutputDiff;
import com.walksocket.md.cli.output.MdOutputMaintenance;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.io.IOException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestMaintenance {

  private MdInputMaintenance inputMaintenance;

  @BeforeClass
  public static void beforeClass() throws IOException {
    MdEnv.setDebug();
    MdEnv.setPretty();
    MdDate.init(60 * 60 * 9);
    MdLogger.open("stderr");
  }

  @Before
  public void testBefore() {
    inputMaintenance = new MdInputMaintenance();
    inputMaintenance.host = "127.0.0.1";
    inputMaintenance.port = 13306;
    inputMaintenance.user = "root";
    inputMaintenance.pass = "pass";
    inputMaintenance.charset = "utf8mb4";
    inputMaintenance.baseDatabase = "base";
    inputMaintenance.compareDatabase = "compare";
  }

  @After
  public void testAfter() throws Exception {
    // maintenance off
    inputMaintenance.maintenance = "off";
    MdExecute.execute(inputMaintenance);
  }

  @Test
  public void test01SetMaintenanceOn() throws Exception {
    // in maintenance
    inputMaintenance.maintenance = "on";

    MdOutputMaintenance outputMaintenance = (MdOutputMaintenance) MdExecute.execute(inputMaintenance);
    System.out.println(MdJson.toJsonStringFriendly(outputMaintenance));

    Assert.assertEquals("on", outputMaintenance.maintenance);

    // check fail
    MdInputDiff inputDiff = new MdInputDiff();
    inputDiff.host = "127.0.0.1";
    inputDiff.port = 13306;
    inputDiff.user = "root";
    inputDiff.pass = "pass";
    inputDiff.charset = "utf8mb4";
    inputDiff.baseDatabase = "base";
    inputDiff.compareDatabase = "compare";

    try {
      MdExecute.execute(inputDiff);
      Assert.fail("not hear.");
    } catch (MdExceptionAbstract e) {
      Assert.assertEquals(MdExceptionAbstract.ExitCode.IN_MAINTENANCE, e.getExitCode());
    }
  }

  @Test
  public void test02SetMaintenanceOff() throws Exception {
    // in maintenance
    inputMaintenance.maintenance = "off";

    MdOutputMaintenance outputMaintenance = (MdOutputMaintenance) MdExecute.execute(inputMaintenance);
    System.out.println(MdJson.toJsonStringFriendly(outputMaintenance));

    Assert.assertEquals("off", outputMaintenance.maintenance);

    // check success
    MdInputDiff inputDiff = new MdInputDiff();
    inputDiff.host = "127.0.0.1";
    inputDiff.port = 13306;
    inputDiff.user = "root";
    inputDiff.pass = "pass";
    inputDiff.charset = "utf8mb4";
    inputDiff.baseDatabase = "base";
    inputDiff.compareDatabase = "compare";

    try {
      MdOutputDiff outputDiff = (MdOutputDiff) MdExecute.execute(inputDiff);
      Assert.assertNotNull(outputDiff);
    } catch (MdExceptionAbstract e) {
      Assert.fail("not hear.");
    }
  }
}
