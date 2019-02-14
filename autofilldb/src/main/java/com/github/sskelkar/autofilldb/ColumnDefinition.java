package com.github.sskelkar.autofilldb;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.github.sskelkar.autofilldb.Util.isNullOrEmpty;

final class ColumnDefinition {
  private String dataType;
  private String constraint;
  private String defaultValue;
  private String name;
  private boolean isNullable;
  private String foreignKeyTable;
  private String foreignKeyColumn;

  ColumnDefinition(ResultSet column) throws SQLException {
    this.dataType = column.getString("column_type");
    this.constraint = column.getString("column_constraint");
    this.defaultValue = column.getString("default_value");
    this.name = column.getString("column_name");
    this.foreignKeyTable = column.getString("foreign_table");
    this.foreignKeyColumn = column.getString("foreign_column");
    this.isNullable = column.getString("nullable").equals("YES");

  }

  String getDataType() {
    return dataType;
  }

  String getDefaultValue() {
    return defaultValue;
  }

  String getName() {
    return name;
  }

  boolean isNullable() {
    return isNullable;
  }

  String getForeignKeyTable() {
    return foreignKeyTable;
  }

  String getForeignKeyColumn() {
    return foreignKeyColumn;
  }

  boolean hasForeignKeyConstraint() {
    return foreignKeyTable != null;
  }

  boolean hasUniqueOrPrimaryConstraint() {
    return !isNullOrEmpty(constraint) && (constraint.equals("PRI") || constraint.equals("UNI"));
  }
}
