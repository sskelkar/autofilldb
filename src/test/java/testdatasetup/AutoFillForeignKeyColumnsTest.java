package testdatasetup;

import autofilldb.Insert;
import org.junit.Test;

public class AutoFillForeignKeyColumnsTest extends DBTest {

  @Test( /* no exception expected */)
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

    new Insert(jdbcTemplate).into("department").value("id", 10).go();
    new Insert(jdbcTemplate).into("department").value("id", 20).go();
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
    new Insert(jdbcTemplate).into("employee").go();
  }
}