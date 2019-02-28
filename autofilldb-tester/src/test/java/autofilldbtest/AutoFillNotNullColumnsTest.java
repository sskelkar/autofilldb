package autofilldbtest;

import autofilldbtest.setup.DBTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.testcontainers.shaded.com.google.common.collect.ImmutableMap.of;

public class AutoFillNotNullColumnsTest extends DBTest {
  @Autowired
  private DataSource dataSource;

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

  @Test( /* no exception expected */)
  public void shouldPopulateBitTypeColumnsWithinConstraints() {
    //when
    runSql(
      "create table type_bit(" +
        "  id integer," +
        "  not_null_column bit(1) not null," +
        "  column_with_default bit(1) not null default b'1')");

    Map<String, Object> values = autoFill.into("type_bit", of("id", 10));

    //then
    assertEquals(0, values.get("not_null_column"));
    assertEquals(1, values.get("column_with_default"));
  }
}