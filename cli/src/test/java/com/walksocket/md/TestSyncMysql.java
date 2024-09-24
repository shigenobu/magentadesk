package com.walksocket.md;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.walksocket.md.bash.MdBashCommand;
import com.walksocket.md.db.MdDbFactory.DbType;
import com.walksocket.md.exception.MdExceptionAbstract;
import com.walksocket.md.input.MdInputDiff;
import com.walksocket.md.input.MdInputSync;
import com.walksocket.md.input.member.MdInputMemberCommand;
import com.walksocket.md.input.member.MdInputMemberHttp;
import com.walksocket.md.input.member.MdInputMemberOption;
import com.walksocket.md.output.MdOutputDiff;
import com.walksocket.md.output.MdOutputSync;
import com.walksocket.md.output.member.MdOutputMemberCommandResult;
import com.walksocket.md.output.member.MdOutputMemberHttpResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodName.class)
public class TestSyncMysql {

  private MdInputDiff inputDiff;

  private MdInputSync inputSync;

  @BeforeAll
  public static void beforeClass() throws IOException {
    MdEnv.setDebug();
    MdEnv.setPretty();
    MdDate.init(60 * 60 * 9);
    MdLogger.open("stderr");
  }

  @BeforeEach
  public void before() {
    inputDiff = new MdInputDiff();
    inputDiff.host = "127.0.0.1";
    inputDiff.port = 23306;
    inputDiff.user = "root";
    inputDiff.pass = "pass";
    inputDiff.charset = "utf8mb4";
    inputDiff.baseDatabase = "base";
    inputDiff.compareDatabase = "compare";
    inputDiff.dbType = DbType.MYSQL.getDbType();

    inputSync = new MdInputSync();
    inputSync.host = "127.0.0.1";
    inputSync.port = 23306;
    inputSync.user = "root";
    inputSync.pass = "pass";
    inputSync.charset = "utf8mb4";
    inputSync.dbType = DbType.MYSQL.getDbType();
  }

  @AfterEach
  public void after() {
    MdBash.exec(new MdBashCommand("mysql -h 127.0.0.1 -P 23306 -u root -ppass < ../docker/mysql/init/1_base.sql", 300));
    MdBash.exec(new MdBashCommand("mysql -h 127.0.0.1 -P 23306 -u root -ppass < ../docker/mysql/init/2_compare.sql", 300));
  }

  @Test
  public void testSyncAllTypes() throws Exception {
    // diff
    inputDiff.option = new MdInputMemberOption();
    inputDiff.option.includeTableLikePatterns.add("t\\_all\\_types");
    inputDiff.validate();
    MdOutputDiff outputDiff = (MdOutputDiff) MdExecute.execute(inputDiff);

    // sync
    inputSync.summaryId = outputDiff.summaryId;
    inputSync.run = true;
    inputSync.validate();
    MdOutputSync outputSync = (MdOutputSync) MdExecute.execute(inputSync);
    System.out.println(MdJson.toJsonStringFriendly(outputSync));

    // re diff
    outputDiff = (MdOutputDiff) MdExecute.execute(inputDiff);
    System.out.println(MdJson.toJsonStringFriendly(outputDiff));

    // match
    assertTrue(
        outputDiff.matchTables.stream().filter(o -> o.tableName.equals("t_all_types")).findFirst().isPresent(),
        "matchTables:t_all_types");
  }

  @Test
  public void testSyncByDiffSeq() throws Exception {
    // diff
    inputDiff.option = new MdInputMemberOption();
    inputDiff.option.includeTableLikePatterns.add("t\\_diff");
    inputDiff.validate();
    MdOutputDiff outputDiff = (MdOutputDiff) MdExecute.execute(inputDiff);

    // sync
    inputSync.summaryId = outputDiff.summaryId;
    inputSync.diffSeqs = new ArrayList<>();
    inputSync.diffSeqs.add(
        outputDiff.mismatchRecordTables
            .stream()
            .filter(o -> o.tableName.equals("t_diff"))
            .findFirst().get()
            .records
            .stream()
            .map(o -> o.diffSeq)
            .findFirst()
            .get());
    inputSync.run = true;
    inputSync.validate();
    MdOutputSync outputSync = (MdOutputSync) MdExecute.execute(inputSync);
    System.out.println(MdJson.toJsonStringFriendly(outputSync));

    // check change
    assertTrue(outputSync.reflectedRecordTables.get(0).records.get(0).changes.get(0));

    // re diff
    outputDiff = (MdOutputDiff) MdExecute.execute(inputDiff);
    System.out.println(MdJson.toJsonStringFriendly(outputDiff));

    // mismatch
    assertTrue(
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_diff")).findFirst().isPresent(),
        "mismatchRecordTables:t_diff");
  }

  @Test
  public void testSyncForceByDiffSeq() throws Exception {
    // diff
    inputDiff.option = new MdInputMemberOption();
    inputDiff.option.includeTableLikePatterns.add("t\\_diff");
    inputDiff.validate();
    MdOutputDiff outputDiff = (MdOutputDiff) MdExecute.execute(inputDiff);

    // sync
    inputSync.summaryId = outputDiff.summaryId;
    inputSync.diffSeqs = new ArrayList<>();
    inputSync.diffSeqs.add(
        outputDiff.mismatchRecordTables
            .stream()
            .filter(o -> o.tableName.equals("t_diff"))
            .findFirst().get()
            .records
            .stream()
            .filter(o -> o.baseValues.stream().allMatch(b -> b == null))
            .map(o -> o.diffSeq)
            .findFirst()
            .get());
    inputSync.run = true;
    inputSync.force = true;
    inputSync.validate();
    MdOutputSync outputSync = (MdOutputSync) MdExecute.execute(inputSync);
    System.out.println(MdJson.toJsonStringFriendly(outputSync));

    // re diff
    outputDiff = (MdOutputDiff) MdExecute.execute(inputDiff);
    System.out.println(MdJson.toJsonStringFriendly(outputDiff));

    // mismatch
    assertTrue(
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_diff")).findFirst().isPresent(),
        "mismatchRecordTables:t_diff");
  }

  @Test
  public void testSyncForceByAll() throws Exception {
    // diff
    inputDiff.option = new MdInputMemberOption();
    inputDiff.option.includeTableLikePatterns.add("t\\_diff");
    inputDiff.validate();
    MdOutputDiff outputDiff = (MdOutputDiff) MdExecute.execute(inputDiff);

    // sync
    inputSync.summaryId = outputDiff.summaryId;
    inputSync.run = true;
    inputSync.force = true;
    inputSync.validate();
    MdOutputSync outputSync = (MdOutputSync) MdExecute.execute(inputSync);
    System.out.println(MdJson.toJsonStringFriendly(outputSync));

    // check previous value
    outputSync.reflectedRecordTables.get(0).records.forEach(r -> {
      if (r.values.get(0) != null && r.values.get(0).equals("3")) {
        assertNull(
            r.previousValues.get(0),
            "null - compare");
        System.out.println(r.previousValues.get(0) == null);
      }
      if (r.previousValues.get(0) != null && r.previousValues.get(0).equals("2")) {
        assertNull(
            r.values.get(0),
            "null - base");
        System.out.println(r.values.get(0) == null);
      }
    });

    // re diff
    outputDiff = (MdOutputDiff) MdExecute.execute(inputDiff);
    System.out.println(MdJson.toJsonStringFriendly(outputDiff));

    // match
    assertTrue(
        outputDiff.matchTables.stream().filter(o -> o.tableName.equals("t_diff")).findFirst().isPresent(),
        "matchTables:t_diff");
  }

  @Test
  public void testSyncFull() throws Exception {
    // diff
    inputDiff.option = new MdInputMemberOption();
    inputDiff.option.ignoreAutoIncrement = true;
    inputDiff.option.ignoreComment = true;
    inputDiff.option.ignorePartitions = true;
    inputDiff.option.ignoreDefaultForSequence = true;
    inputDiff.option.includeTableLikePatterns.add("%");
    inputDiff.validate();
    MdOutputDiff outputDiff = (MdOutputDiff) MdExecute.execute(inputDiff);

    // sync
    inputSync.summaryId = outputDiff.summaryId;
    inputSync.run = true;
    inputSync.force = true;
    inputSync.validate();
    MdOutputSync outputSync = (MdOutputSync) MdExecute.execute(inputSync);
    System.out.println(MdJson.toJsonStringFriendly(outputSync));

    // re diff
    outputDiff = (MdOutputDiff) MdExecute.execute(inputDiff);
    System.out.println(MdJson.toJsonStringFriendly(outputDiff));

    // mismatchRecordTables
    assertTrue(
        outputDiff.mismatchRecordTables.size() == 0,
        "mismatchRecordTables");
  }

  @Test
  public void testDuplicateUnique() throws Exception {
    // diff
    inputDiff.option = new MdInputMemberOption();
    inputDiff.option.includeTableLikePatterns.add("t\\_dup\\_unique");
    inputDiff.validate();
    MdOutputDiff outputDiff = (MdOutputDiff) MdExecute.execute(inputDiff);

    // sync
    inputSync.summaryId = outputDiff.summaryId;
    inputSync.run = true;
    inputSync.force = true;
    inputSync.validate();
    MdOutputSync outputSync = (MdOutputSync) MdExecute.execute(inputSync);
    System.out.println(MdJson.toJsonStringFriendly(outputSync));

    // re diff
    outputDiff = (MdOutputDiff) MdExecute.execute(inputDiff);
    System.out.println(MdJson.toJsonStringFriendly(outputDiff));

    // match
    assertTrue(
        outputDiff.matchTables.stream().filter(o -> o.tableName.equals("t_dup_unique")).findFirst().isPresent(),
        "matchTables:t_dup_unique");
  }

  @Test
  public void testCommandAndHttp() throws Exception {
    // diff
    inputDiff.option = new MdInputMemberOption();
    inputDiff.option.includeTableLikePatterns.add("t\\_all\\_types");
    inputDiff.validate();
    MdOutputDiff outputDiff = (MdOutputDiff) MdExecute.execute(inputDiff);

    // sync
    inputSync.summaryId = outputDiff.summaryId;
    inputSync.run = true;

    inputSync.commandsBeforeCommit = new ArrayList<>();
    inputSync.commandsAfterCommit = new ArrayList<>();
    inputSync.httpCallbackBeforeCommit = new ArrayList<>();
    inputSync.httpCallbackAfterCommit = new ArrayList<>();

    inputSync.commandsBeforeCommit.add(new MdInputMemberCommand("exit 0", 10, Arrays.asList(0)));
    inputSync.commandsAfterCommit.add(new MdInputMemberCommand("exit 23", 10, Arrays.asList(23)));
    inputSync.httpCallbackBeforeCommit.add(new MdInputMemberHttp("http://localhost:9000/before.php", 10, Arrays.asList(200)));
    inputSync.httpCallbackAfterCommit.add(new MdInputMemberHttp("http://localhost:9000/after.php", 10, Arrays.asList(201)));
    inputSync.validate();
    System.out.println(MdJson.toJsonStringFriendly(inputSync));

    MdOutputSync outputSync = (MdOutputSync) MdExecute.execute(inputSync);
    System.out.println(MdJson.toJsonStringFriendly(outputSync));
    for (MdOutputMemberCommandResult commandResult : outputSync.commandResultsBeforeCommit) {
      assertEquals(0, commandResult.code);
    }
    for (MdOutputMemberCommandResult commandResult : outputSync.commandResultsAfterCommit) {
      assertEquals(23, commandResult.code);
    }
    for (MdOutputMemberHttpResult httpResult : outputSync.httpResultsBeforeCommit) {
      assertEquals(200, httpResult.status);
    }
    for (MdOutputMemberHttpResult httpResult : outputSync.httpResultsAfterCommit) {
      assertEquals(201, httpResult.status);
    }
  }

  @Test
  public void testCommandError() throws Exception {
    // diff
    inputDiff.option = new MdInputMemberOption();
    inputDiff.option.includeTableLikePatterns.add("t\\_all\\_types");
    inputDiff.validate();
    MdOutputDiff outputDiff = (MdOutputDiff) MdExecute.execute(inputDiff);

    // sync
    inputSync.summaryId = outputDiff.summaryId;
    inputSync.run = true;

    inputSync.commandsBeforeCommit = new ArrayList<>();
    inputSync.commandsAfterCommit = new ArrayList<>();
    inputSync.httpCallbackBeforeCommit = new ArrayList<>();
    inputSync.httpCallbackAfterCommit = new ArrayList<>();

    inputSync.commandsBeforeCommit.add(new MdInputMemberCommand("exit 1", 10, Arrays.asList(0)));
    inputSync.validate();
    System.out.println(MdJson.toJsonStringFriendly(inputSync));

    try {
      MdOutputSync outputSync = (MdOutputSync) MdExecute.execute(inputSync);
      System.out.println(MdJson.toJsonStringFriendly(outputSync));
      throw new IllegalAccessException();
    } catch (MdExceptionAbstract e) {
      assertEquals(MdExceptionAbstract.ExitCode.NOT_SUCCESS_CODE, e.getExitCode());
      e.printStackTrace();
    }
  }

  @Test
  public void testHttpError() throws Exception {
    // diff
    inputDiff.option = new MdInputMemberOption();
    inputDiff.option.includeTableLikePatterns.add("t\\_all\\_types");
    inputDiff.validate();
    MdOutputDiff outputDiff = (MdOutputDiff) MdExecute.execute(inputDiff);

    // sync
    inputSync.summaryId = outputDiff.summaryId;
    inputSync.run = true;

    inputSync.commandsBeforeCommit = new ArrayList<>();
    inputSync.commandsAfterCommit = new ArrayList<>();
    inputSync.httpCallbackBeforeCommit = new ArrayList<>();
    inputSync.httpCallbackAfterCommit = new ArrayList<>();

    inputSync.httpCallbackBeforeCommit.add(new MdInputMemberHttp("http://localhost:9000/before_error.php", 10, Arrays.asList(200)));
    inputSync.validate();
    System.out.println(MdJson.toJsonStringFriendly(inputSync));

    try {
      MdOutputSync outputSync = (MdOutputSync) MdExecute.execute(inputSync);
      System.out.println(MdJson.toJsonStringFriendly(outputSync));
      throw new IllegalAccessException();
    } catch (MdExceptionAbstract e) {
      assertEquals(MdExceptionAbstract.ExitCode.NOT_SUCCESS_STATUS, e.getExitCode());
      e.printStackTrace();
    }
  }
}
