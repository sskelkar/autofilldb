package testdatasetup;

import autofilldb.Insert;
import org.junit.Test;

public class AutoFillTest extends AutoFillDbApplicationTests {

  @Test
  public void type_int() {
    new Insert(jdbcTemplate).into("type_int").value("id", 10).go();
  }

  @Test
  public void type_varchar() {
    new Insert(jdbcTemplate).into("type_varchar").value("id", 10).go();
  }
}