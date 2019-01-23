package autofilldb;

import autofilldb.datatype.DataTypeHandler;
import autofilldb.datatype.DatetimeHandler;
import autofilldb.datatype.IntegerHandler;
import autofilldb.datatype.VarcharHandler;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static org.apache.logging.log4j.util.Strings.join;

public class JdbcHelper {
  private JdbcTemplate jdbcTemplate;
  private List<DataTypeHandler> dataTypeHandlerHandlers = new ArrayList<>();

  public JdbcHelper(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
    dataTypeHandlerHandlers.add(new IntegerHandler());
    dataTypeHandlerHandlers.add(new VarcharHandler());
    dataTypeHandlerHandlers.add(new DatetimeHandler());
  }

  public Object populate(String tableName, Map<String, Object> valuesProvidedByUser) {
    Map<String, Object> columnsToPopulate = new HashMap<>();

    jdbcTemplate.query("desc " + tableName, result -> {
      ColumnDefinition column = new ColumnDefinition(result);
      if (!column.isNullable()) {
        columnsToPopulate.put(column.getName(), generateValueFor(column));
      }
    });

    valuesProvidedByUser.forEach(columnsToPopulate::put);

    jdbcTemplate.execute(createInsertionQuery(tableName, columnsToPopulate));
    return null;//return primary key value
  }

  private String createInsertionQuery(String tableName, Map<String, Object> columnsToPopulate) {
    return format("insert into %s (", tableName)
        .concat(join(columnsToPopulate.keySet(), ','))
        .concat(") values(")
        .concat(join(columnsToPopulate.values(), ','))
        .concat(")");
  }

  private Object generateValueFor(ColumnDefinition column) {
    return dataTypeHandlerHandlers.stream()
        .filter(dataTypeHandler -> dataTypeHandler.canHandle(column.getDataType()))
        .findFirst()
        .map(dataTypeHandler -> dataTypeHandler.value(column))
        .orElseThrow(() -> new UnsupportedOperationException(column.getDataType() + " not supported yet :("));
  }
}
