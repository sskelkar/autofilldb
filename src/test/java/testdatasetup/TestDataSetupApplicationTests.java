package testdatasetup;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfiguration.class)
public abstract class TestDataSetupApplicationTests extends AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	private DataSource dataSource;

	protected void runSql(String sql) {
		jdbcTemplate.execute(sql);
	}
}

