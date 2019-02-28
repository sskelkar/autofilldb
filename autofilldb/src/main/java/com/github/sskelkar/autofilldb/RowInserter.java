package com.github.sskelkar.autofilldb;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static java.lang.String.format;

final class RowInserter {

  private DataSource dataSource;

  public RowInserter(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public void insert(String sql) {

    try (Connection con = dataSource.getConnection();
         Statement statement = con.createStatement()) {

      statement.execute(sql);
    } catch (SQLException e) {
      throw new RuntimeException(format("Error while executing query - %s", sql), e);
    }
  }
}
