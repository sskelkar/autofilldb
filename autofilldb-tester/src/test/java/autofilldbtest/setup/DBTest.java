package autofilldbtest.setup;

import com.github.sskelkar.autofilldb.AutoFill;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
public abstract class DBTest extends AbstractTransactionalJUnit4SpringContextTests {
  @Autowired
  protected AutoFill autoFill;

  protected void runSql(String sql) {
    jdbcTemplate.execute(sql);
  }

  @Before
  public void setUp() throws Exception {
    jdbcTemplate.execute("drop database if exists test");
    jdbcTemplate.execute("create database test");
    jdbcTemplate.execute("use test");
  }
}

