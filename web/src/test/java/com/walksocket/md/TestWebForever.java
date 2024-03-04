package com.walksocket.md;

import com.walksocket.md.html.endpoint.MdHtmlEndpointAbstract;

import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TestWebForever {

  private static MdServer server;

  @BeforeAll
  public static void beforeClass() throws Exception {
    MdEnv.setDebug();
    MdEnv.setPretty();
    MdDate.init(60 * 60 * 9);
    MdLogger.open("test.log");

    server = new MdServer("0.0.0.0", 8710);
    server.start();
//    server.stopMessageService();
    Thread.sleep(1000);
  }

  @AfterAll
  public static void afterClass() throws IOException {
    server.shutdown();
  }

  @Test
  public void testForever() throws Exception {
    String dir = new File(".").getAbsoluteFile().getParent();
    MdHtmlEndpointAbstract.setBasePath(new File(dir, "src/main/resources").getAbsolutePath());
    server.waitFor();
  }
}
