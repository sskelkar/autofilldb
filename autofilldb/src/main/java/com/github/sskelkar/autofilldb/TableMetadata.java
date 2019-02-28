package com.github.sskelkar.autofilldb;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

final class TableMetadata {
  private static final String TABLE_INFO_QUERY =
    "select c.column_name, c.column_type,  c.is_nullable as nullable, c.column_key as column_constraint, c.column_default as default_value, kcu.referenced_table_name as foreign_table, kcu.referenced_column_name as foreign_column\n" +
      "from information_schema.columns  c\n" +
      "left join information_schema.key_column_usage kcu on (kcu.column_name = c.column_name and kcu.table_schema=c.table_schema and kcu.table_name=c.table_name)\n" +
      "where c.table_schema=(select database() from dual) and c.table_name='%s'";

  private DataSource dataSource;

  public TableMetadata(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  List<ColumnDefinition> get(String tableName) {
    List<ColumnDefinition> columnDefinitions = new ArrayList<>();
    String query = String.format(TABLE_INFO_QUERY, tableName);

    try (Connection con = dataSource.getConnection();
         Statement statement = con.createStatement()) {

      ResultSet rs = statement.executeQuery(query);
      while (rs.next()) {
        columnDefinitions.add(new ColumnDefinition(rs));
      }
    } catch (SQLException e) {
      throw new RuntimeException(format("Error while executing query - %s", tableName), e);
    }

    return columnDefinitions;
  }
}
