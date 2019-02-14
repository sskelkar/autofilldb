package autofilldbtest;

import autofilldbtest.setup.DBTest;
import org.junit.Test;

import static org.testcontainers.shaded.com.google.common.collect.ImmutableMap.of;

public class AutoFillNotNullColumnsTest extends DBTest {
  @Test( /* no exception expected */)
  public void shouldPopulateIntTypeColumnsWithinConstraints() {
    //when
    runSql(
      "create table type_int(" +
        "  id integer," +
        "  not_null_column integer not null," +
        "  digit_limit_column integer(4) not null," +
        "  unique_value_column integer not null unique)");

    autoFill.into("type_int", of("id", 10));
    autoFill.into("type_int", of("id", 20));
  }

  @Test( /* no exception expected */)
  public void shouldPopulateVarcharTypeColumnsWithinConstraints() {
    //when
    runSql(
      "create table type_varchar(" +
        "  id integer," +
        "  not_null_column_1 varchar(10) not null," +
        "  not_null_column_2 varchar(40) not null," +
        "  unique_value_column varchar(4) not null unique)");

    //then
    autoFill.into("type_varchar", of("id", 10));
    autoFill.into("type_varchar", of("id", 20));
  }

  @Test( /* no exception expected */)
  public void shouldPopulateDatetimeTypeColumnsWithinConstraints() {
    //when
    runSql(
      "create table type_datetime(" +
        "  id integer," +
        "  not_null_column datetime not null," +
        "  unique_value_column datetime not null unique)");

    //then
    autoFill.into("type_datetime", of("id", 10));
    autoFill.into("type_datetime", of("id", 20));
  }
}