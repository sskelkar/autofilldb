package testdatasetup;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.Map;

public class JdbcHelper {
  private static final int FIELD = 1;
  private static final int TYPE = 2;
  private static final int NULL = 3;
  private static final int KEY = 4;
  private static final int DEFAULT = 5;
  private JdbcTemplate jdbcTemplate;

  public JdbcHelper(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public Object populate(String tableName, Map<String,Object> columnValues) {
    Map<String, Object> columnsToPopulate = new HashMap<>();
    try {
      jdbcTemplate.query("desc " + tableName, rch -> {
        if(rch.getString(NULL).equals("NO")) {
          String type = rch.getString(TYPE);
          String key = rch.getString(KEY);
          String defaultValue = rch.getString(DEFAULT);
          columnsToPopulate.put(rch.getString(FIELD), getValue(type, key, defaultValue));
        }
      });
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
    System.out.println(columnsToPopulate);
    return null;
  }

  private Object getValue(String type, String key, String defaultValue) {
      return null;
  }
}
