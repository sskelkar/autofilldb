package autofilldb;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public final class Insert {
  private DataSource dataSource;
  private String tableName;
  private Map<String, Object> columnValues = new HashMap<>();

  public Insert(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public Insert into(String tableName) {
    this.tableName = tableName;
    return this;
  }

  public Insert value(String columnName, Object value) {
    assert columnName != null;
    columnValues.put(columnName, value);
    return this;
  }

  public Insert values(Map<String, Object> columnValues) {
    assert columnValues != null;
    this.columnValues.putAll(columnValues);
    return this;
  }

  public Map<String, Object> go() {
    return new AutoInsertor(new JdbcTemplate(dataSource)).populate(tableName, columnValues);
  }
}
