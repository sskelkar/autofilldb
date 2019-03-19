package com.github.sskelkar.autofilldb;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.github.sskelkar.autofilldb.Util.enquote;
import static java.lang.String.format;

final class RowCounter {
  private static final String ROW_COUNT_QUERY = "select count(1) as count from %s where %s=%s";

  private DataSource dataSource;

  RowCounter(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  int getCountFor(String foreignTable, String foreignKeyColumn, Object value) {
    if(value == null) return 0;
    String columnValue = value instanceof String ? enquote((String) value) : value.toString();
    String query = String.format(ROW_COUNT_QUERY, foreignTable, foreignKeyColumn, columnValue);

    try (Connection con = dataSource.getConnection();
         Statement statement = con.createStatement()) {

      ResultSet rs = statement.executeQuery(query);
      rs.next();
      return rs.getInt("count");
    } catch (SQLException e) {
      throw new RuntimeException(format("Error while executing query - %s", query), e);
    }
  }
}
