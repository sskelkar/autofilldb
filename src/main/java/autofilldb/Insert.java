package autofilldb;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.Map;

public class Insert {
  private JdbcTemplate jdbcTemplate;
  private String tableName;
  private Map<String, Object> columnValues = new HashMap<>();

  public Insert(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public Insert into(String tableName) {
    this.tableName = tableName;
    return this;
  }

  public Insert value(String columnName, Object value) {
    columnValues.put(columnName, value);
    return this;
  }

  public Object go() {
    return new JdbcHelper(jdbcTemplate).populate(tableName, columnValues);
  }
}
