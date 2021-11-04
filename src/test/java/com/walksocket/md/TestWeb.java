package com.walksocket.md;

import com.walksocket.md.http.MdHttpClient;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

public class TestWeb {

  private static MdWeb web;

  @BeforeClass
  public static void beforeClass() throws Exception {
    MdEnv.setDebug();
    MdEnv.setPretty();
    MdEnv.setLimitLength(3);
    MdDate.init(60 * 60 * 9);
    MdLogger.open("stderr");

    web = new MdWeb("0.0.0.0", 8710);
    web.start();
    Thread.sleep(1000);
  }

  @AfterClass
  public static void afterClass() {
    web.shutdown();
  }

  @Test
  public void testSimple() throws InterruptedException {


    MdHttpClient client = new MdHttpClient("http://localhost:8710/api/diff/reserve.json", 1);
    MdHttpClient.MdHttpClientResponse response = client.doPost("{}");
    MdLogger.trace(response);

  }
}
