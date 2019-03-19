package com.github.sskelkar.autofilldb;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.sskelkar.autofilldb.Util.enquote;
import static java.lang.String.format;
import static java.lang.String.join;
import static java.util.stream.Collectors.toList;

final class AutomaticPopulater {
  private List<DataTypeHandler> dataTypeHandlerHandlers = new ArrayList<>();
  private DataSource dataSource;
  private RowCounter rowCounter;

  AutomaticPopulater(DataSource dataSource) {
    this.dataSource = dataSource;
    rowCounter = new RowCounter(dataSource);
    dataTypeHandlerHandlers.add(new IntegerHandler());
    dataTypeHandlerHandlers.add(new VarcharHandler());
    dataTypeHandlerHandlers.add(new DatetimeHandler());
    dataTypeHandlerHandlers.add(new BitHandler());
  }

  Map<String, Object> populate(String tableName, Map<String, Object> valuesProvidedByUser) {
    Map<String, Object> columnsToPopulate = new HashMap<>();

    new TableMetadata(dataSource).get(tableName).forEach(column -> {
      if (!column.isNullable()) {
        columnsToPopulate.put(column.getName(), getValueFor(column, valuesProvidedByUser));
      }
    });

    valuesProvidedByUser.forEach((key, value) -> {
      if (value instanceof String) {
        value = enquote((String) value);
      }
      columnsToPopulate.put(key, value);
    });

    new RowInserter(dataSource).insert(createInsertionQuery(tableName, columnsToPopulate));

    return valuesThatWereInserted(columnsToPopulate);
  }

  private Map<String, Object> valuesThatWereInserted(Map<String, Object> columnsToPopulate) {
    Map<String, Object> valuesThatWereInserted = new HashMap<>();
    columnsToPopulate.forEach((key, value) -> {
      Object actualValue = value;
      if (value instanceof String) {
        actualValue = Util.removeQuotes((String) value);
      }
      valuesThatWereInserted.put(key, actualValue);
    });
    return valuesThatWereInserted;
  }

  private Object getValueFor(ColumnDefinition column, Map<String, Object> valuesProvidedByUser) {
    Object value;

    if (column.hasForeignKeyConstraint()) {
      value = getValueForForeignKeyConstraint(column, valuesProvidedByUser);
    } else if (column.hasUniqueOrPrimaryConstraint()) {
      value = handlerFor(column.getDataType()).uniqueValue(column);
    } else {
      value = handlerFor(column.getDataType()).value(column.getDefaultValue());
    }
    return value;
  }

  private Object getValueForForeignKeyConstraint(ColumnDefinition column, Map<String, Object> valuesProvidedByUser) {
    Map<String, Object> availableValues = new HashMap<>();
    if (valuesProvidedByUser.get(column.getName()) != null) {
      availableValues.put(column.getForeignKeyColumn(), valuesProvidedByUser.get(column.getName()));
    }

    Object foreignValue;
    if(rowCounter.getCountFor(column.getForeignKeyTable(), column.getForeignKeyColumn(), valuesProvidedByUser.get(column.getName())) > 0) {
      foreignValue = valuesProvidedByUser.get(column.getName());
    } else {
      Map<String, Object> valuesInReferencedForeignTable = populate(column.getForeignKeyTable(), availableValues);
      foreignValue = valuesInReferencedForeignTable.get(column.getForeignKeyColumn());
    }
    return foreignValue instanceof String ? enquote((String) foreignValue) : foreignValue;
  }

  private String createInsertionQuery(String tableName, Map<String, Object> columnsToPopulate) {
    return format("insert into %s (", tableName)
      .concat(join(",", columnsToPopulate.keySet()))
      .concat(") values(")
      .concat(join(",", columnsToPopulate.values().stream().map(Object::toString).collect(toList())))
      .concat(")");
  }

  private DataTypeHandler handlerFor(String dataType) {
    return dataTypeHandlerHandlers.stream()
      .filter(dataTypeHandler -> dataTypeHandler.canHandle(dataType))
      .findFirst()
      .orElseThrow(() -> new UnsupportedOperationException(dataType + " is not supported yet :("));
  }
}
