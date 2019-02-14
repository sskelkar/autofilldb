package autofilldbtest;

import autofilldbtest.setup.DBTest;
import org.junit.Test;

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
}