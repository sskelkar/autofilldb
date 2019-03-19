package autofilldbtest;

import autofilldbtest.setup.DBTest;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.testcontainers.shaded.com.google.common.collect.ImmutableMap.of;

public class AutoFillForeignKeyColumnsTest extends DBTest {

  @Test(/* no exception expected */)
  public void shouldAutomaticallyCreateReferencedForeignTableRow() {
    //when
    runSql(
      "create table organisation(" +
        "  id integer," +
        "  country_code varchar(2) not null," +
        "  primary key(id))");

    runSql(
      "create table department(" +
        "  id integer," +
        "  organisation_id integer not null," +
        "  primary key(id)," +
        "  foreign key(organisation_id) references organisation(id))");

    autoFill.into("department", of("id", 10));
    autoFill.into("department", of("id", 20));
  }

  @Test( /* no exception expected */)
  public void shouldAutomaticallyCreateReferencedForeignTableRowInHierarchy() {
    //when
    runSql(
      "create table organisation(" +
        "  id integer," +
        "  country_code varchar(2) not null," +
        "  primary key(id))");

    runSql(
      "create table department(" +
        "  id varchar(36)," +
        "  organisation_id integer not null," +
        "  primary key(id)," +
        "  foreign key(organisation_id) references organisation(id))");

    runSql(
      "create table employee(" +
        "  id integer," +
        "  department_id varchar(36) not null," +
        "  primary key(id)," +
        "  foreign key(department_id) references department(id))");

    //then
    autoFill.into("employee");
  }

  @Test(/* no exception expected */)
  public void shouldAutomaticallyInsertValueWhenReferencedColumnIsNotTheKeyColumn() {
    //when
    runSql(
      "create table organisation(" +
        "  id integer," +
        "  country_code varchar(2) not null unique," +
        "  primary key(id))");

    runSql(
      "create table department(" +
        "  id integer," +
        "  country_code varchar(2) not null," +
        "  primary key(id)," +
        "  foreign key(country_code) references organisation(country_code))");

    autoFill.into("department", of("id", 10));
    autoFill.into("department", of("id", 20));
  }


  @Test(/* no exception expected */)
  public void shouldAutoCreateARowInReferencedTableForGivenForeignKeyValue() {
    runSql(
      "create table organisation(" +
        "  id integer," +
        "  country_code varchar(2) not null unique," +
        "  primary key(id))");

    runSql(
      "create table department(" +
        "  id integer," +
        "  country_code varchar(2) not null," +
        "  primary key(id)," +
        "  foreign key(country_code) references organisation(country_code))");

    //when
    Map<String, Object> columnValues = autoFill.into("department", of("id", 10, "country_code", "IN"));

    //then
    assertEquals(10, columnValues.get("id"));
    assertEquals("IN", columnValues.get("country_code"));
  }

  @Test(/* no exception expected */)
  public void shouldAutoCreateARowInReferencedTableWhenReferencedColumnHasDifferentName() {
    runSql(
      "create table organisation(" +
        "  id integer," +
        "  country_code varchar(2) not null unique," +
        "  primary key(id))");

    runSql(
      "create table department(" +
        "  id integer," +
        "  code varchar(2) not null," +
        "  primary key(id)," +
        "  foreign key(code) references organisation(country_code))");

    //when
    Map<String, Object> columnValues = autoFill.into("department", of("id", 10, "code", "IN"));

    //then
    assertEquals(10, columnValues.get("id"));
    assertEquals("IN", columnValues.get("code"));
  }

  @Test(/* no exception expected */)
  public void shouldAutoFillAForeignRowOnlyIfItDoesNotExist() {
    runSql(
      "create table organisation(" +
        "  id integer," +
        "  country_code varchar(2) not null unique," +
        "  primary key(id))");

    runSql(
      "create table department(" +
        "  id integer," +
        "  code varchar(2) not null," +
        "  primary key(id)," +
        "  foreign key(code) references organisation(country_code))");

    //when
    autoFill.into("organisation", of("country_code", "IN"));
    Map<String, Object> columnValues = autoFill.into("department", of("id", 10, "code", "IN"));

    //then
    assertEquals(10, columnValues.get("id"));
    assertEquals("IN", columnValues.get("code"));
  }
}