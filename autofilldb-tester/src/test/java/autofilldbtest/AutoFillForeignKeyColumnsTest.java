package autofilldbtest;

import org.junit.Test;

import static org.testcontainers.shaded.com.google.common.collect.ImmutableMap.of;

public class AutoFillForeignKeyColumnsTest extends DBTest {

  @Test(/* no exception expected */)
  public void shouldAutomaticallyCreateReferencedForeignTableRow() {
    //when
    jdbcTemplate.execute(
      "create table organisation(" +
        "  id integer," +
        "  country_code varchar(2) not null," +
        "  primary key(id))");

    jdbcTemplate.execute(
      "create table department(" +
        "  id integer," +
        "  organisation_id integer not null," +
        "  primary key(id)," +
        "  foreign key(organisation_id) references organisation(id))");

    insert.go("department", of("id", 10));
    insert.go("department", of("id", 20));
  }

  @Test( /* no exception expected */)
  public void shouldAutomaticallyCreateReferencedForeignTableRowInHierarchy() {
    //when
    jdbcTemplate.execute(
      "create table organisation(" +
        "  id integer," +
        "  country_code varchar(2) not null," +
        "  primary key(id))");

    jdbcTemplate.execute(
      "create table department(" +
        "  id varchar(36)," +
        "  organisation_id integer not null," +
        "  primary key(id)," +
        "  foreign key(organisation_id) references organisation(id))");


    jdbcTemplate.execute(
      "create table employee(" +
        "  id integer," +
        "  department_id varchar(36) not null," +
        "  primary key(id)," +
        "  foreign key(department_id) references department(id))");

    //then
    insert.go("employee");
  }
}