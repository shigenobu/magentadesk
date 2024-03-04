package com.walksocket.md;

import com.walksocket.md.input.MdInputDiff;
import com.walksocket.md.input.member.MdInputMemberOption;
import com.walksocket.md.output.MdOutputDiff;

public class TestMulti {

  private static MdInputDiff inputDiff;
  private static MdOutputDiff outputDiff1 = null;
  private static MdOutputDiff outputDiff2 = null;

  public static void main(String[] args) throws Exception {
    MdEnv.setDebug();
    MdEnv.setPretty();
    MdEnv.setLimitLength(3);
    MdEnv.setIsWait(true);
    MdEnv.setLimitMismatchCount(10000);

    MdDate.init(60 * 60 * 9);
    MdLogger.open("stderr");

    inputDiff = new MdInputDiff();
    inputDiff.host = "127.0.0.1";
    inputDiff.port = 13306;
    inputDiff.user = "root";
    inputDiff.pass = "pass";
    inputDiff.charset = "utf8mb4";
    inputDiff.baseDatabase = "base";
    inputDiff.compareDatabase = "compare";

    inputDiff.option = new MdInputMemberOption();
    inputDiff.option.includeTableLikePatterns.add("t\\_inet4");

    Thread t1 = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          outputDiff1 = (MdOutputDiff) MdExecute.execute(inputDiff);
          System.out.println(MdJson.toJsonStringFriendly(outputDiff1));
        } catch (Exception e) {
        }
      }
    });

    Thread t2 = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          outputDiff2 = (MdOutputDiff) MdExecute.execute(inputDiff);
          System.out.println(MdJson.toJsonStringFriendly(outputDiff2));
        } catch (Exception e) {
        }
      }
    });

    t1.start();
    t2.start();

    t1.join();
    t2.join();

    if (outputDiff1.mismatchRecordTables.get(0).mismatchCount != outputDiff2.mismatchRecordTables.get(0).mismatchCount) {
      throw new Exception("Mismatch result");
    }
  }
}
