package com.walksocket.md;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.walksocket.md.bash.MdBashCommand;
import com.walksocket.md.db.MdDbFactory.DbType;
import com.walksocket.md.input.MdInputDiff;
import com.walksocket.md.input.member.MdInputMemberCondition;
import com.walksocket.md.input.member.MdInputMemberOption;
import com.walksocket.md.output.MdOutputDiff;
import java.io.IOException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodName.class)
public class TestDiffMysql {

  private MdInputDiff inputDiff;

  @BeforeAll
  public static void beforeClass() throws IOException {
    MdEnv.setDebug();
    MdEnv.setPretty();
//    MdEnv.setLimitLength(3);
    MdDate.init(60 * 60 * 9);
    MdLogger.open("stderr");

    MdBash.exec(new MdBashCommand("mysql -h 127.0.0.1 -P 23306 -u root -ppass -e 'drop database if exists base'", 300));
    MdBash.exec(new MdBashCommand("mysql -h 127.0.0.1 -P 23306 -u root -ppass -e 'drop database if exists compare'", 300));
    MdBash.exec(new MdBashCommand("mysql -h 127.0.0.1 -P 23306 -u root -ppass < ../docker/mysql/init/1_base.sql", 300));
    MdBash.exec(new MdBashCommand("mysql -h 127.0.0.1 -P 23306 -u root -ppass < ../docker/mysql/init/2_compare.sql", 300));
  }

  @BeforeEach
  public void testBefore() throws Exception {
    inputDiff = new MdInputDiff();
    inputDiff.host = "127.0.0.1";
    inputDiff.port = 23306;
    inputDiff.user = "root";
    inputDiff.pass = "pass";
    inputDiff.charset = "utf8mb4";
    inputDiff.baseDatabase = "base";
    inputDiff.compareDatabase = "compare";
    inputDiff.dbType = DbType.MYSQL.getDbType();

    MdEnv.setLimitMismatchCount(10000);
  }

  @Test
  public void test00NoIgnore() throws Exception {
    MdOutputDiff outputDiff = (MdOutputDiff) MdExecute.execute(inputDiff);
    System.out.println(MdJson.toJsonStringFriendly(outputDiff));

    // existsOnlyBaseTables
    assertTrue(
        outputDiff.existsOnlyBaseTables.stream().filter(o -> o.tableName.equals("t_only_base")).findFirst().isPresent(),
        "existsOnlyBaseTables:t_only_base");

    // existsOnlyCompareTables
    assertTrue(
        outputDiff.existsOnlyCompareTables.stream().filter(o -> o.tableName.equals("t_only_compare")).findFirst().isPresent(),
        "existsOnlyCompareTables:t_only_compare");

    // forceExcludeTables
    assertTrue(
        outputDiff.forceExcludeTables.stream().filter(o -> o.tableName.equals("t_foreign_had")).findFirst().isPresent(),
        "forceExcludeTables:t_foreign_had");
    assertTrue(
        outputDiff.forceExcludeTables.stream().filter(o -> o.tableName.equals("t_myisam")).findFirst().isPresent(),
        "forceExcludeTables:t_myisam");
    assertTrue(
        outputDiff.forceExcludeTables.stream().filter(o -> o.tableName.equals("t_view_ok")).findFirst().isPresent(),
        "forceExcludeTables:t_view_ok");
    assertTrue(
        outputDiff.forceExcludeTables.stream().filter(o -> o.tableName.equals("t_has_trigger_mismatch")).findFirst().isPresent(),
        "forceExcludeTables:t_has_trigger_mismatch");

    // incorrectDefinitionTables
    assertTrue(
        outputDiff.incorrectDefinitionTables.stream().filter(o -> o.tableName.equals("t_no_primary")).findFirst().isPresent(),
        "incorrectDefinitionTables:t_no_primary");
    assertTrue(
        outputDiff.incorrectDefinitionTables.stream().filter(o -> o.tableName.equals("t_foreign_has")).findFirst().isPresent(),
        "incorrectDefinitionTables:t_foreign_has");
    assertTrue(
        outputDiff.incorrectDefinitionTables.stream().filter(o -> o.tableName.equals("t_invalid_charset")).findFirst().isPresent(),
        "incorrectDefinitionTables:t_invalid_charset");

    // mismatchDefinitionTables
    assertTrue(
        outputDiff.mismatchDefinitionTables.stream().filter(o -> o.tableName.equals("t_wrong_auto_increment")).findFirst().isPresent(),
        "mismatchDefinitionTables:t_wrong_auto_increment");
    assertTrue(
        outputDiff.mismatchDefinitionTables.stream().filter(o -> o.tableName.equals("t_partition_mismatch")).findFirst().isPresent(),
        "mismatchDefinitionTables:t_partition_mismatch");
    assertTrue(
        outputDiff.mismatchDefinitionTables.stream().filter(o -> o.tableName.equals("t_comment_mismatch")).findFirst().isPresent(),
        "mismatchDefinitionTables:t_comment_mismatch");

    // mismatchRecordTables
    assertTrue(
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_all_types")).findFirst().isPresent(),
        "mismatchRecordTables:t_all_types");
    assertTrue(
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_blob_primary")).findFirst().isPresent(),
        "mismatchRecordTables:t_blob_primary");
    assertTrue(
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_data_lower_upper")).findFirst().isPresent(),
        "mismatchRecordTables:t_data_lower_upper");
    assertTrue(
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_diff")).findFirst().isPresent(),
        "mismatchRecordTables:t_diff");
    assertEquals(
        2,
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_diff")).findFirst().get().mismatchCount);
    assertTrue(
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_utf8_diff")).findFirst().isPresent(),
        "mismatchRecordTables:t_utf8_diff");
    assertEquals(
        2,
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_utf8_diff")).findFirst().get().mismatchCount);
    assertTrue(
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_article")).findFirst().isPresent(),
        "mismatchRecordTables:t_article");
    assertEquals(
        4,
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_article")).findFirst().get().mismatchCount);
    assertTrue(
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_movie")).findFirst().isPresent(),
        "mismatchRecordTables:t_movie");
    assertEquals(
        3,
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_movie")).findFirst().get().mismatchCount);
    assertTrue(
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_dup_unique")).findFirst().isPresent(),
        "mismatchRecordTables:t_dup_unique");
    assertEquals(
        2,
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_dup_unique")).findFirst().get().mismatchCount);

    // matchTables
    assertTrue(
        outputDiff.matchTables.stream().filter(o -> o.tableName.equals("T_ALL_UPPER")).findFirst().isPresent(),
        "matchTables:T_ALL_UPPER");
    assertTrue(
        outputDiff.matchTables.stream().filter(o -> o.tableName.equals("t_lower_UPPER")).findFirst().isPresent(),
        "matchTables:t_lower_UPPER");
  }

  @Test
  public void test11IgnoreAutoIncrement() throws Exception {
    inputDiff.option = new MdInputMemberOption();
    inputDiff.option.ignoreAutoIncrement = true;
    inputDiff.option.includeTableLikePatterns.add("t\\_wrong\\_auto\\_increment");

    MdOutputDiff outputDiff = (MdOutputDiff) MdExecute.execute(inputDiff);
    System.out.println(MdJson.toJsonStringFriendly(outputDiff));

    // matchTables
    assertTrue(
        outputDiff.matchTables.stream().filter(o -> o.tableName.equals("t_wrong_auto_increment")).findFirst().isPresent(),
        "matchTables:t_wrong_auto_increment");
  }

  @Test
  public void test14IgnoreComment() throws Exception {
    inputDiff.option = new MdInputMemberOption();
    inputDiff.option.ignoreComment = true;
    inputDiff.option.includeTableLikePatterns.add("t\\_comment\\_mismatch");

    MdOutputDiff outputDiff = (MdOutputDiff) MdExecute.execute(inputDiff);
    System.out.println(MdJson.toJsonStringFriendly(outputDiff));

    // matchTables
    assertTrue(
        outputDiff.matchTables.stream().filter(o -> o.tableName.equals("t_comment_mismatch")).findFirst().isPresent(),
        "matchTables:t_comment_mismatch");
  }

  @Test
  public void test15IgnorePartitions() throws Exception {
    inputDiff.option = new MdInputMemberOption();
    inputDiff.option.ignorePartitions = true;
    inputDiff.option.includeTableLikePatterns.add("t\\_partition\\_mismatch");

    MdOutputDiff outputDiff = (MdOutputDiff) MdExecute.execute(inputDiff);
    System.out.println(MdJson.toJsonStringFriendly(outputDiff));

    // matchTables
    assertTrue(
        outputDiff.matchTables.stream().filter(o -> o.tableName.equals("t_partition_mismatch")).findFirst().isPresent(),
        "matchTables:t_partition_mismatch");
  }

  @Test
  public void test16MismatchOverflow() throws Exception {
    // set env
    MdEnv.setLimitMismatchCount(2);

    // diff
    inputDiff.option = new MdInputMemberOption();
    inputDiff.option.includeTableLikePatterns.add("t\\_diff");
    inputDiff.validate();
    MdOutputDiff outputDiff = (MdOutputDiff) MdExecute.execute(inputDiff);
    System.out.println(MdJson.toJsonStringFriendly(outputDiff));
    assertFalse(
        outputDiff.mismatchRecordTables.get(0).overflow,
        "t_diff:overflow:false");
    assertEquals(
        2, outputDiff.mismatchRecordTables.get(0).mismatchCount,
        "t_diff:mismatchCount");
    assertFalse(
        outputDiff.mismatchRecordTables.get(0).records.isEmpty(),
        "t_diff:records");

    // set env
    MdEnv.setLimitMismatchCount(1);

    // diff
    inputDiff.option = new MdInputMemberOption();
    inputDiff.option.includeTableLikePatterns.add("t\\_diff");
    inputDiff.validate();
    outputDiff = (MdOutputDiff) MdExecute.execute(inputDiff);
    System.out.println(MdJson.toJsonStringFriendly(outputDiff));
    assertTrue(
        outputDiff.mismatchRecordTables.get(0).overflow,
        "t_diff:overflow:true");
    assertEquals(
        2, outputDiff.mismatchRecordTables.get(0).mismatchCount,
        "t_diff:mismatchCount");
    assertTrue(
        outputDiff.mismatchRecordTables.get(0).records.isEmpty(),
        "t_diff:records");
  }

  @Test
  public void test21Generated() throws Exception {
    inputDiff.option = new MdInputMemberOption();
    inputDiff.option.includeTableLikePatterns.add("t\\_diff\\_generated");
    inputDiff.option.includeTableLikePatterns.add("t\\_diff\\_generated\\_stored");

    MdOutputDiff outputDiff = (MdOutputDiff) MdExecute.execute(inputDiff);
    System.out.println(MdJson.toJsonStringFriendly(outputDiff));

    // mismatchRecordTables
    assertEquals(
        2,
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_diff_generated")).findFirst().get().columns.size());
    assertEquals(
        2,
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_diff_generated")).findFirst().get().records.get(0).baseValues.size());
    assertEquals(
        2,
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_diff_generated")).findFirst().get().records.get(0).compareValues.size());
    assertEquals(
        2,
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_diff_generated_stored")).findFirst().get().columns.size());
    assertEquals(
        2,
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_diff_generated_stored")).findFirst().get().records.get(0).baseValues.size());
    assertEquals(
        2,
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_diff_generated_stored")).findFirst().get().records.get(0).compareValues.size());
  }

  @Test
  public void test31AddCondition() throws Exception {
    inputDiff.option = new MdInputMemberOption();
    inputDiff.option.includeTableLikePatterns.add("t_article");
    inputDiff.option.includeTableLikePatterns.add("t_movie");

    // ---
    MdInputMemberCondition c1 = new MdInputMemberCondition();
    c1.tableName = "t_article";
    c1.expression = "up_date > '2020-11-05'";
    inputDiff.conditions.add(c1);

    MdInputMemberCondition c11 = new MdInputMemberCondition();
    c11.tableName = "t_movie";
    c11.expression = "up_date > '2020-11-20'";
    inputDiff.conditions.add(c11);

    MdOutputDiff outputDiff1 = (MdOutputDiff) MdExecute.execute(inputDiff);
    System.out.println(MdJson.toJsonStringFriendly(outputDiff1));

    // mismatchRecordTables
    assertTrue(
        outputDiff1.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_article")).findFirst().isPresent(),
        "mismatchRecordTables:t_article");
    assertTrue(
        outputDiff1.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_movie")).findFirst().isPresent(),
        "mismatchRecordTables:t_movie");

    // ---
    inputDiff.conditions.clear();

    MdInputMemberCondition c2 = new MdInputMemberCondition();
    c2.tableName = "t_article";
    c2.expression = "up_date > '2020-11-15'";
    inputDiff.conditions.add(c2);

    MdInputMemberCondition c21 = new MdInputMemberCondition();
    c21.tableName = "t_movie";
    c21.expression = "up_date <= '2020-10-20'";
    inputDiff.conditions.add(c21);

    MdOutputDiff outputDiff2 = (MdOutputDiff) MdExecute.execute(inputDiff);
    System.out.println(MdJson.toJsonStringFriendly(outputDiff2));

    // matchTables
    assertTrue(
        outputDiff2.matchTables.stream().filter(o -> o.tableName.equals("t_article")).findFirst().isPresent(),
        "matchTables:t_article");
    assertTrue(
        outputDiff2.matchTables.stream().filter(o -> o.tableName.equals("t_movie")).findFirst().isPresent(),
        "matchTables:t_movie");

    // checksum
    assertEquals(
        "fake_base_t_article",
        outputDiff2.matchTables.stream().filter(o -> o.tableName.equals("t_article")).findFirst().get().baseChecksum,
        "checksum:t_article");
    assertEquals(
        "fake_compare_t_article",
        outputDiff2.matchTables.stream().filter(o -> o.tableName.equals("t_article")).findFirst().get().compareChecksum,
        "checksum:t_article");
  }
}
