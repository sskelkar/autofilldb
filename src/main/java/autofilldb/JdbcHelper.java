package autofilldb;

import org.apache.logging.log4j.util.Strings;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

  public Object populate(String tableName, Map<String, Object> valuesProvidedByUser) {
    Map<String, Object> columnsToPopulate = new HashMap<>();
    try {
      jdbcTemplate.query("desc " + tableName, rch -> {
        if (rch.getString(NULL).equals("NO")) {
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

    valuesProvidedByUser.forEach((key, value) -> columnsToPopulate.put(key, value));

    String query = String.format("insert into %s (", tableName);

    List<String> columns = new ArrayList<>();
    List<String> values = new ArrayList<>();
    columnsToPopulate.forEach((key, value) -> {
      columns.add(key);
      values.add(value.toString());
    });
    query += Strings.join(columns, ',');
    query += ") values(";
    query += Strings.join(values, ',');
    query += ")";
    System.out.println(query);
    jdbcTemplate.execute(query);
    return null;//return primary key value
  }

  private Object getValue(String type, String key, String defaultValue) {
    type = type.toLowerCase();
    if (type.startsWith("int")) {
      return new IntValue(type, key, defaultValue).value();
    } else if (type.startsWith("varchar")) {
      return new VarcharValue(type, key, defaultValue).value();
    } else if (type.startsWith("datetime")) {
      return new DatetimeValue(type, key, defaultValue).value();
    }
    return null;
  }
}
