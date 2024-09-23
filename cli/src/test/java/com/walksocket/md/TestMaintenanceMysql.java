package com.walksocket.md;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import com.walksocket.md.db.MdDbFactory.DbType;
import com.walksocket.md.exception.MdExceptionAbstract;
import com.walksocket.md.input.MdInputDiff;
import com.walksocket.md.input.MdInputMaintenance;
import com.walksocket.md.output.MdOutputDiff;
import com.walksocket.md.output.MdOutputMaintenance;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodName.class)
public class TestMaintenanceMysql {

  private MdInputMaintenance inputMaintenance;

  @BeforeAll
  public static void beforeClass() throws IOException {
    MdEnv.setDebug();
    MdEnv.setPretty();
    MdDate.init(60 * 60 * 9);
    MdLogger.open("stderr");
  }

  @BeforeEach
  public void testBefore() {
    inputMaintenance = new MdInputMaintenance();
    inputMaintenance.host = "127.0.0.1";
    inputMaintenance.port = 23306;
    inputMaintenance.user = "root";
    inputMaintenance.pass = "pass";
    inputMaintenance.charset = "utf8mb4";
    inputMaintenance.baseDatabase = "base";
    inputMaintenance.compareDatabase = "compare";
    inputMaintenance.dbType = DbType.MYSQL.getDbType();
  }

  @AfterEach
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

    assertEquals("on", outputMaintenance.maintenance);

    // check fail
    MdInputDiff inputDiff = new MdInputDiff();
    inputDiff.host = "127.0.0.1";
    inputDiff.port = 23306;
    inputDiff.user = "root";
    inputDiff.pass = "pass";
    inputDiff.charset = "utf8mb4";
    inputDiff.baseDatabase = "base";
    inputDiff.compareDatabase = "compare";
    inputDiff.dbType = DbType.MYSQL.getDbType();

    try {
      MdExecute.execute(inputDiff);
      fail("not hear.");
    } catch (MdExceptionAbstract e) {
      assertEquals(MdExceptionAbstract.ExitCode.IN_MAINTENANCE, e.getExitCode());
    }
  }

  @Test
  public void test02SetMaintenanceOff() throws Exception {
    // in maintenance
    inputMaintenance.maintenance = "off";

    MdOutputMaintenance outputMaintenance = (MdOutputMaintenance) MdExecute.execute(inputMaintenance);
    System.out.println(MdJson.toJsonStringFriendly(outputMaintenance));

    assertEquals("off", outputMaintenance.maintenance);

    // check success
    MdInputDiff inputDiff = new MdInputDiff();
    inputDiff.host = "127.0.0.1";
    inputDiff.port = 23306;
    inputDiff.user = "root";
    inputDiff.pass = "pass";
    inputDiff.charset = "utf8mb4";
    inputDiff.baseDatabase = "base";
    inputDiff.compareDatabase = "compare";
    inputDiff.dbType = DbType.MYSQL.getDbType();

    try {
      MdOutputDiff outputDiff = (MdOutputDiff) MdExecute.execute(inputDiff);
      assertNotNull(outputDiff);
    } catch (MdExceptionAbstract e) {
      fail("not hear.");
    }
  }
}
