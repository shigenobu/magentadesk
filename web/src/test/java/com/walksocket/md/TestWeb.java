package com.walksocket.md;

import com.walksocket.md.bash.MdBashCommand;
import com.walksocket.md.http.MdHttpClient;
import com.walksocket.md.input.MdInputDiff;
import com.walksocket.md.input.MdInputMaintenance;
import com.walksocket.md.input.MdInputSync;
import com.walksocket.md.input.member.MdInputMemberOption;
import com.walksocket.md.output.MdOutputDiff;
import com.walksocket.md.output.MdOutputMaintenance;
import com.walksocket.md.output.MdOutputSync;
import com.walksocket.md.api.endpoint.MdApiEndpointAbstract;
import org.junit.*;

import java.util.Arrays;

public class TestWeb {

  private static MdServer server;

  private MdInputDiff inputDiff;

  private MdInputSync inputSync;

  private MdInputMaintenance inputMaintenance;

  @BeforeClass
  public static void beforeClass() throws Exception {
    MdEnv.setDebug();
    MdEnv.setPretty();
    MdEnv.setLimitLength(3);
    MdDate.init(60 * 60 * 9);
    MdLogger.open("stdout");

    server = new MdServer("0.0.0.0", 8710);
    server.start();
    Thread.sleep(1000);
  }

  @AfterClass
  public static void afterClass() {
    server.shutdown();
  }

  @Before
  public void before() {
    inputDiff = new MdInputDiff();
    inputDiff.host = "127.0.0.1";
    inputDiff.port = 13306;
    inputDiff.user = "root";
    inputDiff.pass = "pass";
    inputDiff.charset = "utf8mb4";
    inputDiff.baseDatabase = "base";
    inputDiff.compareDatabase = "compare";

    inputSync = new MdInputSync();
    inputSync.host = "127.0.0.1";
    inputSync.port = 13306;
    inputSync.user = "root";
    inputSync.pass = "pass";
    inputSync.charset = "utf8mb4";

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
  public void after() {
    MdBash.exec(new MdBashCommand("mysql -h 127.0.0.1 -P 13306 -u root -ppass < ../docker/mysql/init/1_base.sql", 300));
    MdBash.exec(new MdBashCommand("mysql -h 127.0.0.1 -P 13306 -u root -ppass < ../docker/mysql/init/2_compare.sql", 300));
  }

  @Test
  public void testDiffSync() throws InterruptedException {
    // ----------------------------------------
    // diff
    inputDiff.option = new MdInputMemberOption();
    inputDiff.option.includeTableLikePatterns.add("t\\_all\\_types");

    MdHttpClient clientDiffReserve = new MdHttpClient("http://localhost:8710/api/diff/reserve.json", 1);
    MdHttpClient.MdHttpClientResponse responseDiffReserve = clientDiffReserve.doPost(MdJson.toJsonString(inputDiff));
    MdLogger.trace(responseDiffReserve);
    String executionIdDiffReserve = responseDiffReserve.getHeader(MdApiEndpointAbstract.HEADER_EXECUTION_ID);

    MdOutputDiff outputDiff = null;
    for (int i = 0; i < 10; i++) {
      MdHttpClient clientDiffCheck = new MdHttpClient("http://localhost:8710/api/diff/check.json", 1);
      clientDiffCheck.setHeader(MdApiEndpointAbstract.HEADER_EXECUTION_ID, executionIdDiffReserve);
      MdHttpClient.MdHttpClientResponse responseDiffCheck = clientDiffCheck.doPost(null);
      MdLogger.trace(responseDiffCheck);
      if (responseDiffCheck.getStatus() == 200) {
        outputDiff = MdJson.toObject(responseDiffCheck.getBodyString(), MdOutputDiff.class);
        break;
      }

      Thread.sleep(3000);
    }

    System.out.println(MdJson.toJsonStringFriendly(outputDiff));
    Assert.assertNotNull(outputDiff);

    // ----------------------------------------
    // sync
    inputSync.summaryId = outputDiff.summaryId;
    inputSync.run = true;

    MdHttpClient clientSyncReserve = new MdHttpClient("http://localhost:8710/api/sync/reserve.json", 1);
    MdHttpClient.MdHttpClientResponse responseSyncReserve = clientSyncReserve.doPost(MdJson.toJsonString(inputSync));
    MdLogger.trace(responseSyncReserve);
    String executionIdSyncReserve = responseSyncReserve.getHeader(MdApiEndpointAbstract.HEADER_EXECUTION_ID);

    MdOutputSync outputSync = null;
    for (int i = 0; i < 10; i++) {
      MdHttpClient clientSyncCheck = new MdHttpClient("http://localhost:8710/api/sync/check.json", 1);
      clientSyncCheck.setHeader(MdApiEndpointAbstract.HEADER_EXECUTION_ID, executionIdSyncReserve);
      MdHttpClient.MdHttpClientResponse responseSyncCheck = clientSyncCheck.doPost(null);
      MdLogger.trace(responseSyncCheck);
      if (responseSyncCheck.getStatus() == 200) {
        outputSync = MdJson.toObject(responseSyncCheck.getBodyString(), MdOutputSync.class);
        break;
      }

      Thread.sleep(3000);
    }

    System.out.println(MdJson.toJsonStringFriendly(outputSync));
    Assert.assertNotNull(outputSync);
  }

  @Test
  public void testMaintenance() throws InterruptedException {
    for (String m : Arrays.asList("on", "off")) {
      inputMaintenance.maintenance = m;

      MdHttpClient clientMaintenanceReserve = new MdHttpClient("http://localhost:8710/api/maintenance/reserve.json", 1);
      MdHttpClient.MdHttpClientResponse responseMaintenanceReserve = clientMaintenanceReserve.doPost(MdJson.toJsonString(inputMaintenance));
      MdLogger.trace(responseMaintenanceReserve);
      String executionIdDiffReserve = responseMaintenanceReserve.getHeader(MdApiEndpointAbstract.HEADER_EXECUTION_ID);

      MdOutputMaintenance outputMaintenance = null;
      for (int i = 0; i < 10; i++) {
        MdHttpClient clientMaintenanceCheck = new MdHttpClient("http://localhost:8710/api/maintenance/check.json", 1);
        clientMaintenanceCheck.setHeader(MdApiEndpointAbstract.HEADER_EXECUTION_ID, executionIdDiffReserve);
        MdHttpClient.MdHttpClientResponse responseMaintenanceCheck = clientMaintenanceCheck.doPost(null);
        MdLogger.trace(responseMaintenanceCheck);
        if (responseMaintenanceCheck.getStatus() == 200) {
          outputMaintenance = MdJson.toObject(responseMaintenanceCheck.getBodyString(), MdOutputMaintenance.class);
          break;
        }

        Thread.sleep(3000);
      }

      System.out.println(MdJson.toJsonStringFriendly(outputMaintenance));
      Assert.assertNotNull(outputMaintenance);
    }
  }
}
