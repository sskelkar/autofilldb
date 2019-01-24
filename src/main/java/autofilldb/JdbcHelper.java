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
import static java.util.Collections.emptyMap;
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

  public Map<String, Object> populate(String tableName, Map<String, Object> valuesProvidedByUser) {
    Map<String, Object> columnsToPopulate = new HashMap<>();

    getColumnDefinitions(tableName).forEach(column -> {
      if (!column.isNullable()) {
        if (column.hasForeignKeyConstraint()) {
          Map<String, Object> valuesInReferencedForeignTable = populate(column.getForeignKeyTable(), emptyMap());
          columnsToPopulate.put(column.getName(), valuesInReferencedForeignTable.get(column.getForeignKeyColumn()));
        } else {
          columnsToPopulate.put(column.getName(), generateValueFor(column));
        }
      }
    });

    valuesProvidedByUser.forEach(columnsToPopulate::put);

    jdbcTemplate.execute(createInsertionQuery(tableName, columnsToPopulate));
    return columnsToPopulate;
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

  private List<ColumnDefinition> getColumnDefinitions(String tableName) {
    String query = String.format("select c.column_name, c.column_type,  c.is_nullable as nullable, c.column_key as column_constraint, c.column_default as default_value, kcu.referenced_table_name as foreign_table, kcu.referenced_column_name as foreign_column\n" +
      "from information_schema.columns  c\n" +
      "left join information_schema.key_column_usage kcu on (kcu.column_name = c.column_name and kcu.table_schema=c.table_schema and kcu.table_name=c.table_name)\n" +
      "where c.table_schema=(select database() from dual) and c.table_name='%s'", tableName);

    List<ColumnDefinition> columnDefinitions = new ArrayList<>();
    jdbcTemplate.query(query, result -> {
      columnDefinitions.add(new ColumnDefinition(result));
    });

    return columnDefinitions;
  }
}
