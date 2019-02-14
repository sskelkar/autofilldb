package autofilldbtest;

import autofilldbtest.setup.DBTest;
import org.junit.Test;
import org.springframework.jdbc.BadSqlGrammarException;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.testcontainers.shaded.com.google.common.collect.ImmutableMap.of;

public class UsageTest extends DBTest {

  @Test(expected = BadSqlGrammarException.class)
  public void shouldThrowErrorIfAnInvalidColumnNameIsPassed() {
    //when
    runSql(
      "create table type_int(" +
        "  id integer," +
        "  not_null_column integer not null," +
        "  digit_limit_column integer(4) not null," +
        "  unique_value_column integer not null unique)");

    autoFill.into("type_int", of("id", 10, "nonexistent_column", 1));
  }

  @Test /* no exception expected */
  public void shouldReturnAMapOfAllColumnValuesUsedToPopulateTheRow() {
    //when
    runSql(
      "create table type_int(" +
        "  id integer," +
        "  not_null_column integer not null," +
        "  digit_limit_column integer(4) not null," +
        "  unique_value_column integer not null unique)");

    Map<String, Object> columnValues = autoFill.into("type_int", of("id", 10));

    //then
    assertEquals(10, columnValues.get("id"));
    assertNotNull(columnValues.get("not_null_column"));
    assertNotNull(columnValues.get("digit_limit_column"));
    assertNotNull(columnValues.get("unique_value_column"));
  }

  @Test(expected = BadSqlGrammarException.class)
  public void shouldThrowErrorIfTableDoesNotExist() {
    //when
    autoFill.into("type_int", of("id", 10));
  }

  @Test(expected = BadSqlGrammarException.class)
  public void shouldThrowErrorIfASpecifiedColumnValueIsOfWrongDataType() {
    //when
    runSql(
      "create table type_int(" +
        "  id integer," +
        "  varchar_column varchar2(10) not null," +
        "  digit_limit_column integer(4) not null," +
        "  unique_value_column integer not null unique)");
    autoFill.into("type_int", of("varchar_column", 10));
  }
}
