package autofilldbtest;

import autofilldbtest.setup.DBTest;
import com.github.sskelkar.autofilldb.AutoFill;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;

import javax.sql.DataSource;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.testcontainers.shaded.com.google.common.collect.ImmutableMap.of;

public class UsageTest extends DBTest {

  @Autowired
  private DataSource dataSource;

  @Test(expected = RuntimeException.class)
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

  @Test(expected = RuntimeException.class)
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

  @Test /*no exception expected*/
  public void shouldAutoPopulatePrimaryKeyWithUniqueValues() {
    //when
    runSql(
      "create table type_int(" +
        "  id integer," +
        "  varchar_column varchar(30) not null," +
        "  digit_limit_column integer(4) not null," +
        "  primary key(id))");

    autoFill.into("type_int", of("varchar_column", "some string"));
    autoFill.into("type_int", of("varchar_column", "some other string"));
  }

  @Test(expected = RuntimeException.class)
  public void shouldNotAllowSQLInjection() {
    //when
    runSql(
      "create table employee(" +
        "  id varchar(50)," +
        "  primary key(id))");



    autoFill.into("employee", of("id", "10'); drop table employee; insert into employee (id) values('10"));
    autoFill.into("employee", of("id", "20" ));
  }

  @Test /*no exception expected*/
  public void canUseMultipleInstancesOfAutoFill() {
    //when
    runSql(
      "create table employee(" +
        "  id varchar(50)," +
        "  primary key(id))");

    new AutoFill(dataSource).into("employee", of("id", "10"));
    new AutoFill(dataSource).into("employee", of("id", "20"));
  }
}
