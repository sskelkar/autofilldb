package autofilldb;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static java.util.Collections.emptyMap;
import static org.apache.logging.log4j.util.Strings.join;

class AutomaticPopulater {
  private JdbcTemplate jdbcTemplate;
  private List<DataTypeHandler> dataTypeHandlerHandlers = new ArrayList<>();

  AutomaticPopulater(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
    dataTypeHandlerHandlers.add(new IntegerHandler());
    dataTypeHandlerHandlers.add(new VarcharHandler());
    dataTypeHandlerHandlers.add(new DatetimeHandler());
  }

  Map<String, Object> populate(String tableName, Map<String, Object> valuesProvidedByUser) {
    Map<String, Object> columnsToPopulate = new HashMap<>();

    getColumnDefinitions(tableName).forEach(column -> {
      if (!column.isNullable()) {
        columnsToPopulate.put(column.getName(), getValueFor(column));
      }
    });

    valuesProvidedByUser.forEach(columnsToPopulate::put);

    jdbcTemplate.execute(createInsertionQuery(tableName, columnsToPopulate));
    return columnsToPopulate;
  }

  private Object getValueFor(ColumnDefinition column) {
    Object value;

    if (column.hasForeignKeyConstraint()) {
      value = getValueForForeignKeyConstraint(column);
    } else if (column.hasUniqueOrPrimaryConstraint()) {
      value = handlerFor(column.getDataType()).uniqueValue(column);
    } else {
      value = handlerFor(column.getDataType()).value(column.getDefaultValue());
    }
    return value;
  }

  private Object getValueForForeignKeyConstraint(ColumnDefinition column) {
    Map<String, Object> valuesInReferencedForeignTable = populate(column.getForeignKeyTable(), emptyMap());
    return valuesInReferencedForeignTable.get(column.getForeignKeyColumn());
  }

  private String createInsertionQuery(String tableName, Map<String, Object> columnsToPopulate) {
    return format("insert into %s (", tableName)
      .concat(join(columnsToPopulate.keySet(), ','))
      .concat(") values(")
      .concat(join(columnsToPopulate.values(), ','))
      .concat(")");
  }

  private DataTypeHandler handlerFor(String dataType) {
    return dataTypeHandlerHandlers.stream()
      .filter(dataTypeHandler -> dataTypeHandler.canHandle(dataType))
      .findFirst()
      .orElseThrow(() -> new UnsupportedOperationException(dataType + " is not supported yet :("));
  }

  private List<ColumnDefinition> getColumnDefinitions(String tableName) {
    String query = String.format(
      "select c.column_name, c.column_type,  c.is_nullable as nullable, c.column_key as column_constraint, c.column_default as default_value, kcu.referenced_table_name as foreign_table, kcu.referenced_column_name as foreign_column\n" +
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
