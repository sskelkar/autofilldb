package testdatasetup;

import autofilldb.Insert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AutoFillTest extends DBTest {

  @Test( /* no exception expected */)
  public void shouldPopulateIntTypeColumnsWithinConstraints() {
    //when
    jdbcTemplate.execute(
            "create table type_int(" +
            "  id integer," +
            "  not_null_column integer not null," +
            "  digit_limit_column integer(4) not null," +
            "  unique_value_column integer not null unique)");

    new Insert(jdbcTemplate).into("type_int").value("id", 10).go();
    new Insert(jdbcTemplate).into("type_int").value("id", 20).go();
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
    new Insert(jdbcTemplate).into("type_varchar").value("id", 10).go();
    new Insert(jdbcTemplate).into("type_varchar").value("id", 20).go();
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
    new Insert(jdbcTemplate).into("type_datetime").value("id", 10).go();
    new Insert(jdbcTemplate).into("type_datetime").value("id", 20).go();
  }
}