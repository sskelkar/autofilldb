package autofilldb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Map;

import static java.util.Collections.emptyMap;

@Component
public final class Insert {

  @Autowired
  private DataSource dataSource;

  public Map<String, Object> go(String tableName, Map<String, Object> columnValues) {
    return new AutoInsertor(new JdbcTemplate(dataSource)).populate(tableName, columnValues);
  }

  public Map<String, Object> go(String tableName) {
    return go(tableName, emptyMap());
  }
}
