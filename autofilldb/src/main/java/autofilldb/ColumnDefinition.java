package autofilldb;

import java.sql.ResultSet;
import java.sql.SQLException;

import static autofilldb.Util.isNullOrEmpty;

final public class ColumnDefinition {
  private String dataType;
  private String constraint;
  private String defaultValue;
  private String name;
  private boolean isNullable;
  private String foreignKeyTable;
  private String foreignKeyColumn;

  public ColumnDefinition(ResultSet column) throws SQLException {
    this.dataType = column.getString("column_type");
    this.constraint = column.getString("column_constraint");
    this.defaultValue = column.getString("default_value");
    this.name = column.getString("column_name");
    this.foreignKeyTable = column.getString("foreign_table");
    this.foreignKeyColumn = column.getString("foreign_column");
    this.isNullable = column.getString("nullable").equals("YES");

  }

  public String getDataType() {
    return dataType;
  }

  public String getDefaultValue() {
    return defaultValue;
  }

  public String getName() {
    return name;
  }

  public boolean isNullable() {
    return isNullable;
  }

  public String getForeignKeyTable() {
    return foreignKeyTable;
  }

  public String getForeignKeyColumn() {
    return foreignKeyColumn;
  }

  public boolean hasForeignKeyConstraint() {
    return foreignKeyTable != null;
  }

  public boolean hasUniqueOrPrimaryConstraint() {
    return !isNullOrEmpty(constraint) && (constraint.equals("PRI") || constraint.equals("UNI"));
  }
}
