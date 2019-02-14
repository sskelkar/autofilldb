package autofilldbtest;

import autofilldbtest.setup.DBTest;
import org.junit.Test;
import org.springframework.jdbc.BadSqlGrammarException;

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
}
