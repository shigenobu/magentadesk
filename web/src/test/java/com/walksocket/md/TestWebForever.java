package com.walksocket.md;

import com.walksocket.md.input.MdInputDiff;
import com.walksocket.md.input.MdInputMaintenance;
import com.walksocket.md.input.MdInputSync;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestWebForever {

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

  @Test
  public void testForever() {
    server.stopMessageService();
    server.waitFor();
  }
}
