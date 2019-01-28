package autofilldb;

import org.junit.Test;

import static org.testcontainers.shaded.com.google.common.collect.ImmutableMap.of;

public class AutoFillNotNullColumnsTest extends DBTest {

  @Test( /* no exception expected */)
  public void shouldPopulateIntTypeColumnsWithinConstraints() {
    //when
    jdbcTemplate.execute(
        "create table type_int(" +
            "  id integer," +
            "  not_null_column integer not null," +
            "  digit_limit_column integer(4) not null," +
            "  unique_value_column integer not null unique)");

    insert.go("type_int", of("id", 10));
    insert.go("type_int", of("id", 20));
  }

  @Test( /* no exception expected */)
  public void shouldPopulateVarcharTypeColumnsWithinConstraints() {
    //when
    jdbcTemplate.execute(
        "create table type_varchar(" +
            "  id integer," +
            "  not_null_column_1 varchar(10) not null," +
            "  not_null_column_2 varchar(40) not null," +
            "  unique_value_column varchar(4) not null unique)");

    //then
    insert.go("type_varchar", of("id", 10));
    insert.go("type_varchar", of("id", 20));
  }

  @Test( /* no exception expected */)
  public void shouldPopulateDatetimeTypeColumnsWithinConstraints() {
    //when
    jdbcTemplate.execute(
        "create table type_datetime(" +
            "  id integer," +
            "  not_null_column datetime not null," +
            "  unique_value_column datetime not null unique)");

    //then
    insert.go("type_datetime", of("id", 10));
    insert.go("type_datetime", of("id", 20));
  }
}