package com.walksocket.md;

import com.walksocket.md.exception.MdExceptionInvalidInput;
import com.walksocket.md.input.MdInputDiff;
import com.walksocket.md.input.member.MdInputMemberCondition;
import com.walksocket.md.input.member.MdInputMemberOption;
import com.walksocket.md.output.MdOutputDiff;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.io.IOException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDiff {

  private MdInputDiff inputDiff;

  @BeforeClass
  public static void beforeClass() throws IOException {
    MdEnv.setDebug();
    MdEnv.setPretty();
    MdEnv.setLimitLength(3);
    MdDate.init(60 * 60 * 9);
    MdLogger.open("stderr");
  }

  @Before
  public void testBefore() throws Exception {
    inputDiff = new MdInputDiff();
    inputDiff.host = "127.0.0.1";
    inputDiff.port = 13306;
    inputDiff.user = "root";
    inputDiff.pass = "pass";
    inputDiff.charset = "utf8mb4";
    inputDiff.baseDatabase = "base";
    inputDiff.compareDatabase = "compare";

    MdEnv.setLimitMismatchCount(10000);
  }

  @Test
  public void test00NoIgnore() throws Exception {
    MdOutputDiff outputDiff = (MdOutputDiff) MdExecute.execute(inputDiff);
    System.out.println(MdJson.toJsonStringFriendly(outputDiff));

    // existsOnlyBaseTables
    Assert.assertTrue(
        "existsOnlyBaseTables:t_only_base",
        outputDiff.existsOnlyBaseTables.stream().filter(o -> o.tableName.equals("t_only_base")).findFirst().isPresent());

    // existsOnlyCompareTables
    Assert.assertTrue(
        "existsOnlyCompareTables:t_only_compare",
        outputDiff.existsOnlyCompareTables.stream().filter(o -> o.tableName.equals("t_only_compare")).findFirst().isPresent());

    // forceExcludeTables
    Assert.assertTrue(
        "forceExcludeTables:s_seq",
        outputDiff.forceExcludeTables.stream().filter(o -> o.tableName.equals("s_seq")).findFirst().isPresent());
    Assert.assertTrue(
        "forceExcludeTables:t_foreign_had",
        outputDiff.forceExcludeTables.stream().filter(o -> o.tableName.equals("t_foreign_had")).findFirst().isPresent());
    Assert.assertTrue(
        "forceExcludeTables:t_myisam",
        outputDiff.forceExcludeTables.stream().filter(o -> o.tableName.equals("t_myisam")).findFirst().isPresent());
    Assert.assertTrue(
        "forceExcludeTables:t_view_ok",
        outputDiff.forceExcludeTables.stream().filter(o -> o.tableName.equals("t_view_ok")).findFirst().isPresent());
    Assert.assertTrue(
        "forceExcludeTables:t_has_trigger_mismatch",
        outputDiff.forceExcludeTables.stream().filter(o -> o.tableName.equals("t_has_trigger_mismatch")).findFirst().isPresent());

    // incorrectDefinitionTables
    Assert.assertTrue(
        "incorrectDefinitionTables:t_no_primary",
        outputDiff.incorrectDefinitionTables.stream().filter(o -> o.tableName.equals("t_no_primary")).findFirst().isPresent());
    Assert.assertTrue(
        "incorrectDefinitionTables:t_no_primary_system_versioned",
        outputDiff.incorrectDefinitionTables.stream().filter(o -> o.tableName.equals("t_no_primary_system_versioned")).findFirst().isPresent());
    Assert.assertTrue(
        "incorrectDefinitionTables:t_foreign_has",
        outputDiff.incorrectDefinitionTables.stream().filter(o -> o.tableName.equals("t_foreign_has")).findFirst().isPresent());
    Assert.assertTrue(
        "incorrectDefinitionTables:t_invalid_charset",
        outputDiff.incorrectDefinitionTables.stream().filter(o -> o.tableName.equals("t_invalid_charset")).findFirst().isPresent());

    // mismatchDefinitionTables
    Assert.assertTrue(
        "mismatchDefinitionTables:t_has_default_seq",
        outputDiff.mismatchDefinitionTables.stream().filter(o -> o.tableName.equals("t_has_default_seq")).findFirst().isPresent());
    Assert.assertTrue(
        "mismatchDefinitionTables:t_system_versioned",
        outputDiff.mismatchDefinitionTables.stream().filter(o -> o.tableName.equals("t_system_versioned")).findFirst().isPresent());
    Assert.assertTrue(
        "mismatchDefinitionTables:t_system_versioned_mismatch",
        outputDiff.mismatchDefinitionTables.stream().filter(o -> o.tableName.equals("t_system_versioned_mismatch")).findFirst().isPresent());
    Assert.assertTrue(
        "mismatchDefinitionTables:t_wrong_auto_increment",
        outputDiff.mismatchDefinitionTables.stream().filter(o -> o.tableName.equals("t_wrong_auto_increment")).findFirst().isPresent());
    Assert.assertTrue(
        "mismatchDefinitionTables:t_partition_mismatch",
        outputDiff.mismatchDefinitionTables.stream().filter(o -> o.tableName.equals("t_partition_mismatch")).findFirst().isPresent());
    Assert.assertTrue(
        "mismatchDefinitionTables:t_comment_mismatch",
        outputDiff.mismatchDefinitionTables.stream().filter(o -> o.tableName.equals("t_comment_mismatch")).findFirst().isPresent());

    // mismatchRecordTables
    Assert.assertTrue(
        "mismatchRecordTables:t_all_types",
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_all_types")).findFirst().isPresent());
    Assert.assertTrue(
        "mismatchRecordTables:t_blob_primary",
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_blob_primary")).findFirst().isPresent());
    Assert.assertTrue(
        "mismatchRecordTables:t_data_lower_upper",
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_data_lower_upper")).findFirst().isPresent());
    Assert.assertTrue(
        "mismatchRecordTables:t_diff",
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_diff")).findFirst().isPresent());
    Assert.assertTrue(
        "mismatchRecordTables:t_utf8_diff",
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_utf8_diff")).findFirst().isPresent());
    Assert.assertTrue(
        "mismatchRecordTables:t_article",
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_article")).findFirst().isPresent());
    Assert.assertTrue(
        "mismatchRecordTables:t_movie",
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_movie")).findFirst().isPresent());
    Assert.assertTrue(
        "mismatchRecordTables:t_dup_unique",
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_dup_unique")).findFirst().isPresent());

    // matchTables
    Assert.assertTrue(
        "matchTables:T_ALL_UPPER",
        outputDiff.matchTables.stream().filter(o -> o.tableName.equals("T_ALL_UPPER")).findFirst().isPresent());
    Assert.assertTrue(
        "matchTables:t_lower_UPPER",
        outputDiff.matchTables.stream().filter(o -> o.tableName.equals("t_lower_UPPER")).findFirst().isPresent());
    Assert.assertTrue(
        "matchTables:t_system_versioned_invisible",
        outputDiff.matchTables.stream().filter(o -> o.tableName.equals("t_system_versioned_invisible")).findFirst().isPresent());
    Assert.assertTrue(
        "matchTables:t_system_versioned_visible",
        outputDiff.matchTables.stream().filter(o -> o.tableName.equals("t_system_versioned_visible")).findFirst().isPresent());
    Assert.assertTrue(
        "matchTables:t_system_versioned_not_diff",
        outputDiff.matchTables.stream().filter(o -> o.tableName.equals("t_system_versioned_not_diff")).findFirst().isPresent());
  }

  @Test
  public void test11IgnoreAutoIncrement() throws Exception {
    inputDiff.option = new MdInputMemberOption();
    inputDiff.option.ignoreAutoIncrement = true;
    inputDiff.option.includeTableLikePatterns.add("t\\_wrong\\_auto\\_increment");

    MdOutputDiff outputDiff = (MdOutputDiff) MdExecute.execute(inputDiff);
    System.out.println(MdJson.toJsonStringFriendly(outputDiff));

    // matchTables
    Assert.assertTrue(
        "matchTables:t_wrong_auto_increment",
        outputDiff.matchTables.stream().filter(o -> o.tableName.equals("t_wrong_auto_increment")).findFirst().isPresent());
  }

//  @Test
//  public void test12IgnoreSystemVersioned() throws Exception {
//    inputDiff.option = new MdInputMemberOption();
//    inputDiff.option.includeTableLikePatterns.add("t\\_system\\_versioned");
//    inputDiff.option.includeTableLikePatterns.add("t\\_system\\_versioned\\_mismatch");
//
//    MdOutputDiff outputDiff = (MdOutputDiff) MdExecute.execute(inputDiff);
//    System.out.println(MdJson.toJsonStringFriendly(outputDiff));
//
//    // mismatch
//    // (notice) base and system versioned table type is always not match.
//    Assert.assertTrue(
//        "matchTables:t_system_versioned_mismatch",
//        outputDiff.mismatchDefinitionTables.stream().filter(o -> o.tableName.equals("t_system_versioned_mismatch")).findFirst().isPresent());
//
//    // matchTables
//    Assert.assertTrue(
//        "matchTables:t_system_versioned",
//        outputDiff.matchTables.stream().filter(o -> o.tableName.equals("t_system_versioned")).findFirst().isPresent());
//  }

  @Test
  public void test13IgnoreDefaultForSequence() throws Exception {
    inputDiff.option = new MdInputMemberOption();
    inputDiff.option.ignoreDefaultForSequence = true;
    inputDiff.option.includeTableLikePatterns.add("t\\_has\\_default\\_seq");

    MdOutputDiff outputDiff = (MdOutputDiff) MdExecute.execute(inputDiff);
    System.out.println(MdJson.toJsonStringFriendly(outputDiff));

    // matchTables
    Assert.assertTrue(
        "matchTables:t_has_default_seq",
        outputDiff.matchTables.stream().filter(o -> o.tableName.equals("t_has_default_seq")).findFirst().isPresent());
  }

  @Test
  public void test14IgnoreComment() throws Exception {
    inputDiff.option = new MdInputMemberOption();
    inputDiff.option.ignoreComment = true;
    inputDiff.option.includeTableLikePatterns.add("t\\_comment\\_mismatch");

    MdOutputDiff outputDiff = (MdOutputDiff) MdExecute.execute(inputDiff);
    System.out.println(MdJson.toJsonStringFriendly(outputDiff));

    // matchTables
    Assert.assertTrue(
        "matchTables:t_comment_mismatch",
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
    Assert.assertTrue(
        "matchTables:t_partition_mismatch",
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
    Assert.assertFalse("t_diff:overflow:false", outputDiff.mismatchRecordTables.get(0).overflow);
    Assert.assertEquals("t_diff:mismatchCount", 2, outputDiff.mismatchRecordTables.get(0).mismatchCount);
    Assert.assertFalse("t_diff:records", outputDiff.mismatchRecordTables.get(0).records.isEmpty());

    // set env
    MdEnv.setLimitMismatchCount(1);

    // diff
    inputDiff.option = new MdInputMemberOption();
    inputDiff.option.includeTableLikePatterns.add("t\\_diff");
    inputDiff.validate();
    outputDiff = (MdOutputDiff) MdExecute.execute(inputDiff);
    System.out.println(MdJson.toJsonStringFriendly(outputDiff));
    Assert.assertTrue("t_diff:overflow:true", outputDiff.mismatchRecordTables.get(0).overflow);
    Assert.assertEquals("t_diff:mismatchCount", 2, outputDiff.mismatchRecordTables.get(0).mismatchCount);
    Assert.assertTrue("t_diff:records", outputDiff.mismatchRecordTables.get(0).records.isEmpty());
  }

  @Test
  public void test21Generated() throws Exception {
    inputDiff.option = new MdInputMemberOption();
    inputDiff.option.includeTableLikePatterns.add("t\\_diff\\_generated");
    inputDiff.option.includeTableLikePatterns.add("t\\_diff\\_generated\\_stored");

    MdOutputDiff outputDiff = (MdOutputDiff) MdExecute.execute(inputDiff);
    System.out.println(MdJson.toJsonStringFriendly(outputDiff));

    // mismatchRecordTables
    Assert.assertEquals(
        2,
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_diff_generated")).findFirst().get().columns.size());
    Assert.assertEquals(
        2,
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_diff_generated")).findFirst().get().records.get(0).baseValues.size());
    Assert.assertEquals(
        2,
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_diff_generated")).findFirst().get().records.get(0).compareValues.size());
    Assert.assertEquals(
        2,
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_diff_generated_stored")).findFirst().get().columns.size());
    Assert.assertEquals(
        2,
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_diff_generated_stored")).findFirst().get().records.get(0).baseValues.size());
    Assert.assertEquals(
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
    Assert.assertTrue(
        "mismatchRecordTables:t_article",
        outputDiff1.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_article")).findFirst().isPresent());
    Assert.assertTrue(
        "mismatchRecordTables:t_movie",
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
    Assert.assertTrue(
        "matchTables:t_article",
        outputDiff2.matchTables.stream().filter(o -> o.tableName.equals("t_article")).findFirst().isPresent());
    Assert.assertTrue(
        "matchTables:t_movie",
        outputDiff2.matchTables.stream().filter(o -> o.tableName.equals("t_movie")).findFirst().isPresent());

    // checksum
    Assert.assertEquals("checksum:t_article",
        "fake_base_t_article",
        outputDiff2.matchTables.stream().filter(o -> o.tableName.equals("t_article")).findFirst().get().baseChecksum);
    Assert.assertEquals("checksum:t_article",
        "fake_compare_t_article",
        outputDiff2.matchTables.stream().filter(o -> o.tableName.equals("t_article")).findFirst().get().compareChecksum);
  }

  @Test
  public void test41UuidDataType() throws Exception {
    inputDiff.option = new MdInputMemberOption();
    inputDiff.option.includeTableLikePatterns.add("t\\_uuid");

    MdOutputDiff outputDiff = (MdOutputDiff) MdExecute.execute(inputDiff);
    System.out.println(MdJson.toJsonStringFriendly(outputDiff));

    // mismatchRecordTables
    Assert.assertTrue(
        "mismatchRecordTables:t_uuid",
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_uuid")).findFirst().isPresent());
  }

  @Test
  public void test42Inet6DataType() throws Exception {
    inputDiff.option = new MdInputMemberOption();
    inputDiff.option.includeTableLikePatterns.add("t\\_inet6");

    MdOutputDiff outputDiff = (MdOutputDiff) MdExecute.execute(inputDiff);
    System.out.println(MdJson.toJsonStringFriendly(outputDiff));

    // mismatchRecordTables
    Assert.assertTrue(
        "mismatchRecordTables:t_inet6",
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_inet6")).findFirst().isPresent());
  }

  @Test
  public void test43Inet4DataType() throws Exception {
    inputDiff.option = new MdInputMemberOption();
    inputDiff.option.includeTableLikePatterns.add("t\\_inet4");

    MdOutputDiff outputDiff = (MdOutputDiff) MdExecute.execute(inputDiff);
    System.out.println(MdJson.toJsonStringFriendly(outputDiff));

    // mismatchRecordTables
    Assert.assertTrue(
        "mismatchRecordTables:t_inet4",
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_inet4")).findFirst().isPresent());
  }
}
