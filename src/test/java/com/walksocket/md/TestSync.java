package com.walksocket.md;

import com.walksocket.md.bash.MdBashCommand;
import com.walksocket.md.exception.MdExceptionInvalidInput;
import com.walksocket.md.input.MdInputDiff;
import com.walksocket.md.input.MdInputSync;
import com.walksocket.md.input.member.MdInputMemberOption;
import com.walksocket.md.mariadb.MdMariadbConnection;
import com.walksocket.md.output.MdOutputDiff;
import com.walksocket.md.output.MdOutputSync;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestSync {

  private MdInputDiff inputDiff;

  private MdInputSync inputSync;

  @BeforeClass
  public static void beforeClass() throws IOException {
    MdEnv.setDebug();
    MdEnv.setPretty();
    MdLogger.setAddSeconds(60 * 60 * 9);
    MdLogger.open("stderr");
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
  }

  @After
  public void after() {
    MdBash.exec(new MdBashCommand("mysql -h 127.0.0.1 -P 13306 -u root -ppass base < `pwd`/test/base.sql", 300));
    MdBash.exec(new MdBashCommand("mysql -h 127.0.0.1 -P 13306 -u root -ppass compare < `pwd`/test/compare.sql", 300));
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
    Assert.assertTrue(
        "matchTables:t_all_types",
        outputDiff.matchTables.stream().filter(o -> o.tableName.equals("t_all_types")).findFirst().isPresent());
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

    // re diff
    outputDiff = (MdOutputDiff) MdExecute.execute(inputDiff);
    System.out.println(MdJson.toJsonStringFriendly(outputDiff));

    // mismatch
    Assert.assertTrue(
        "mismatchRecordTables:t_diff",
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_diff")).findFirst().isPresent());
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
    Assert.assertTrue(
        "mismatchRecordTables:t_diff",
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_diff")).findFirst().isPresent());
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
    Assert.assertTrue(
        "mismatchRecordTables",
        outputDiff.mismatchRecordTables.size() == 0);
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
    Assert.assertTrue(
        "matchTables:t_dup_unique",
        outputDiff.matchTables.stream().filter(o -> o.tableName.equals("t_dup_unique")).findFirst().isPresent());
  }
}
