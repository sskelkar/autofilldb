package testdatasetup.repository;

import net.andreinc.mockneat.MockNeat;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import testdatasetup.Insert;
import testdatasetup.TestDataSetupApplicationTests;
import testdatasetup.model.Employee;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class EmployeeRepositoryTest extends TestDataSetupApplicationTests {
  @Autowired
  private EmployeeRepository repository;

  @Test
  public void name() {
    System.out.println(">>>>>>>>>>>>>>> start");
    jdbcTemplate. query("desc employee", rch -> {
      System.out.println(">>"+rch.getString(1));
    });
    System.out.println(">>>>>>>>>>>> end");
  }

  @Test
  public void shouldReturnAllEmployees() {

    //given
//    runSql("insert into employee values(1, 'jason', '2018-01-01 00:00:00', '80c76f');");
    new Insert(jdbcTemplate).into("employee")
      .value("id", 1)
      .value("name", "jason")
      .value("created", "2018-01-01 00:00:00")
      .value("department_id", "80c76f")
      .go();
    //when

    //then
    assertEquals(1, repository.findAll().size());
  }
}