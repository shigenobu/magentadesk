package com.walksocket.md;

import com.walksocket.md.input.MdInputDiff;
import com.walksocket.md.output.MdOutputAbstract;
import com.walksocket.md.output.MdOutputDiff;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestDiffNoIgnore {

  private MdInputDiff inputDiff;

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
  public void test() throws Exception {
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

    // incorrectDefinitionTables
    Assert.assertTrue(
        "incorrectDefinitionTables:t_no_primary",
        outputDiff.incorrectDefinitionTables.stream().filter(o -> o.tableName.equals("t_no_primary")).findFirst().isPresent());
    Assert.assertTrue(
        "incorrectDefinitionTables:t_foreign_has",
        outputDiff.incorrectDefinitionTables.stream().filter(o -> o.tableName.equals("t_foreign_has")).findFirst().isPresent());

    // mismatchDefinitionTables
    Assert.assertTrue(
        "mismatchDefinitionTables:t_has_default_seq",
        outputDiff.mismatchDefinitionTables.stream().filter(o -> o.tableName.equals("t_has_default_seq")).findFirst().isPresent());

    // mismatchRecordTables
    Assert.assertTrue(
        "mismatchRecordTables:t_blob_primary",
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_blob_primary")).findFirst().isPresent());
    Assert.assertTrue(
        "mismatchRecordTables:t_data_lower_upper",
        outputDiff.mismatchRecordTables.stream().filter(o -> o.tableName.equals("t_data_lower_upper")).findFirst().isPresent());

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
  }
}
