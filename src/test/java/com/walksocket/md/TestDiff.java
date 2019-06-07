package com.walksocket.md;

import com.walksocket.md.input.MdInputDiff;
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
    MdLogger.open("stderr");
  }

  @Before
  public void testBefore() {
    inputDiff = new MdInputDiff();
    inputDiff.host = "127.0.0.1";
    inputDiff.port = 13306;
    inputDiff.user = "root";
    inputDiff.pass = "pass";
    inputDiff.charset = "utf8mb4";
    inputDiff.baseDatabase = "base";
    inputDiff.compareDatabase = "compare";
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
        "mismatchRecordTables:t_blob_primary",
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_blob_primary")).findFirst().isPresent());
    Assert.assertTrue(
        "mismatchRecordTables:t_data_lower_upper",
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_data_lower_upper")).findFirst().isPresent());
    Assert.assertTrue(
        "mismatchRecordTables:t_diff",
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_diff")).findFirst().isPresent());

    // matchTables
    Assert.assertTrue(
        "matchTables:T_ALL_UPPER",
        outputDiff.matchTables.stream().filter(o -> o.tableName.equals("T_ALL_UPPER")).findFirst().isPresent());
    Assert.assertTrue(
        "matchTables:t_all_types",
        outputDiff.matchTables.stream().filter(o -> o.tableName.equals("t_all_types")).findFirst().isPresent());
    Assert.assertTrue(
        "matchTables:t_lower_UPPER",
        outputDiff.matchTables.stream().filter(o -> o.tableName.equals("t_lower_UPPER")).findFirst().isPresent());
    Assert.assertTrue(
        "matchTables:t_system_versioned_invisible",
        outputDiff.matchTables.stream().filter(o -> o.tableName.equals("t_system_versioned_invisible")).findFirst().isPresent());
    Assert.assertTrue(
        "matchTables:t_system_versioned_visible",
        outputDiff.matchTables.stream().filter(o -> o.tableName.equals("t_system_versioned_visible")).findFirst().isPresent());
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
}
