package com.walksocket.md;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
//        "existsOnlyBaseTables:t_only_base",
        outputDiff.existsOnlyBaseTables.stream().filter(o -> o.tableName.equals("t_only_base")).findFirst().isPresent());

    // existsOnlyCompareTables
    assertTrue(
//        "existsOnlyCompareTables:t_only_compare",
        outputDiff.existsOnlyCompareTables.stream().filter(o -> o.tableName.equals("t_only_compare")).findFirst().isPresent());

    // forceExcludeTables
    assertTrue(
//        "forceExcludeTables:t_foreign_had",
        outputDiff.forceExcludeTables.stream().filter(o -> o.tableName.equals("t_foreign_had")).findFirst().isPresent());
    assertTrue(
//        "forceExcludeTables:t_myisam",
        outputDiff.forceExcludeTables.stream().filter(o -> o.tableName.equals("t_myisam")).findFirst().isPresent());
    assertTrue(
//        "forceExcludeTables:t_view_ok",
        outputDiff.forceExcludeTables.stream().filter(o -> o.tableName.equals("t_view_ok")).findFirst().isPresent());
    assertTrue(
//        "forceExcludeTables:t_has_trigger_mismatch",
        outputDiff.forceExcludeTables.stream().filter(o -> o.tableName.equals("t_has_trigger_mismatch")).findFirst().isPresent());

    // incorrectDefinitionTables
    assertTrue(
//        "incorrectDefinitionTables:t_no_primary",
        outputDiff.incorrectDefinitionTables.stream().filter(o -> o.tableName.equals("t_no_primary")).findFirst().isPresent());
    assertTrue(
//        "incorrectDefinitionTables:t_foreign_has",
        outputDiff.incorrectDefinitionTables.stream().filter(o -> o.tableName.equals("t_foreign_has")).findFirst().isPresent());
    assertTrue(
//        "incorrectDefinitionTables:t_invalid_charset",
        outputDiff.incorrectDefinitionTables.stream().filter(o -> o.tableName.equals("t_invalid_charset")).findFirst().isPresent());

    // mismatchDefinitionTables
    assertTrue(
//        "mismatchDefinitionTables:t_wrong_auto_increment",
        outputDiff.mismatchDefinitionTables.stream().filter(o -> o.tableName.equals("t_wrong_auto_increment")).findFirst().isPresent());
    assertTrue(
//        "mismatchDefinitionTables:t_partition_mismatch",
        outputDiff.mismatchDefinitionTables.stream().filter(o -> o.tableName.equals("t_partition_mismatch")).findFirst().isPresent());
    assertTrue(
//        "mismatchDefinitionTables:t_comment_mismatch",
        outputDiff.mismatchDefinitionTables.stream().filter(o -> o.tableName.equals("t_comment_mismatch")).findFirst().isPresent());

    // mismatchRecordTables
    assertTrue(
//        "mismatchRecordTables:t_all_types",
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_all_types")).findFirst().isPresent());
    assertTrue(
//        "mismatchRecordTables:t_blob_primary",
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_blob_primary")).findFirst().isPresent());
    assertTrue(
//        "mismatchRecordTables:t_data_lower_upper",
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_data_lower_upper")).findFirst().isPresent());
    assertTrue(
//        "mismatchRecordTables:t_diff",
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_diff")).findFirst().isPresent());
    assertEquals(
        2,
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_diff")).findFirst().get().mismatchCount);
//    assertTrue(
////        "mismatchRecordTables:t_utf8_diff",
//        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_utf8_diff")).findFirst().isPresent());
//    assertTrue(
////        "mismatchRecordTables:t_article",
//        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_article")).findFirst().isPresent());
//    assertTrue(
////        "mismatchRecordTables:t_movie",
//        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_movie")).findFirst().isPresent());
//    assertTrue(
////        "mismatchRecordTables:t_dup_unique",
//        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_dup_unique")).findFirst().isPresent());
//
    // matchTables
    assertTrue(
//        "matchTables:T_ALL_UPPER",
        outputDiff.matchTables.stream().filter(o -> o.tableName.equals("T_ALL_UPPER")).findFirst().isPresent());
    assertTrue(
//        "matchTables:t_lower_UPPER",
        outputDiff.matchTables.stream().filter(o -> o.tableName.equals("t_lower_UPPER")).findFirst().isPresent());
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
//        "matchTables:t_wrong_auto_increment",
        outputDiff.matchTables.stream().filter(o -> o.tableName.equals("t_wrong_auto_increment")).findFirst().isPresent());
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
//        "matchTables:t_comment_mismatch",
        outputDiff.matchTables.stream().filter(o -> o.tableName.equals("t_comment_mismatch")).findFirst().isPresent());
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
//        "matchTables:t_partition_mismatch",
        outputDiff.matchTables.stream().filter(o -> o.tableName.equals("t_partition_mismatch")).findFirst().isPresent());
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
//        "t_diff:overflow:false",
        outputDiff.mismatchRecordTables.get(0).overflow);
    assertEquals(
//        "t_diff:mismatchCount",
        2, outputDiff.mismatchRecordTables.get(0).mismatchCount);
    assertFalse(
//        "t_diff:records",
        outputDiff.mismatchRecordTables.get(0).records.isEmpty());

    // set env
    MdEnv.setLimitMismatchCount(1);

    // diff
    inputDiff.option = new MdInputMemberOption();
    inputDiff.option.includeTableLikePatterns.add("t\\_diff");
    inputDiff.validate();
    outputDiff = (MdOutputDiff) MdExecute.execute(inputDiff);
    System.out.println(MdJson.toJsonStringFriendly(outputDiff));
    assertTrue(
//        "t_diff:overflow:true",
        outputDiff.mismatchRecordTables.get(0).overflow);
    assertEquals(
//        "t_diff:mismatchCount",
        2, outputDiff.mismatchRecordTables.get(0).mismatchCount);
    assertTrue(
//        "t_diff:records",
        outputDiff.mismatchRecordTables.get(0).records.isEmpty());
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
//        "mismatchRecordTables:t_article",
        outputDiff1.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_article")).findFirst().isPresent());
    assertTrue(
//        "mismatchRecordTables:t_movie",
        outputDiff1.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_movie")).findFirst().isPresent());

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
//        "matchTables:t_article",
        outputDiff2.matchTables.stream().filter(o -> o.tableName.equals("t_article")).findFirst().isPresent());
    assertTrue(
//        "matchTables:t_movie",
        outputDiff2.matchTables.stream().filter(o -> o.tableName.equals("t_movie")).findFirst().isPresent());

    // checksum
    assertEquals(
//        "checksum:t_article",
        "fake_base_t_article",
        outputDiff2.matchTables.stream().filter(o -> o.tableName.equals("t_article")).findFirst().get().baseChecksum);
    assertEquals(
//        "checksum:t_article",
        "fake_compare_t_article",
        outputDiff2.matchTables.stream().filter(o -> o.tableName.equals("t_article")).findFirst().get().compareChecksum);
  }
}
