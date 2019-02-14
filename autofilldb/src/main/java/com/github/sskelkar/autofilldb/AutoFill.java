package com.github.sskelkar.autofilldb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;

/**
 * <p>This class makes it easier to populate a database table row in a Spring project. It accepts a table name and a map of column name and value pair.
 * It can insert a new row using the provided values. If the user hasn't specified values for certain columns that have a not null
 * constraint on them, AutoFill would automatically populate them with fake data under following circumstances:</p>
 *
 *
 * <pre>   1. Suppose a table has not null constraints on some columns.
 *      AutoFill would identify such columns populate them with valid fake data of appropriate data type.
 *      If the column has a unique constraint as well, a unique fake value would be generated for each row.</pre>
 * <pre>   2. A column has a foreign key constraint.
 *      AutoFill would ensure that a valid value is populated into this column that satisfies the foreign key constraint.</pre>
 *
 * This class requires an injectable bean of type <tt>javax.sql.DataSource</tt> in the Spring container.
 */
@Component
public final class AutoFill {

  @Autowired
  private DataSource dataSource;

  private AutoFill() {}

  /**
   * Insert a new row into a table and populate it using the passed in column name-value pairs. Any column that has
   * a not null or a foreign key constraint but isn't specified in <tt>columnValues</tt>, will be automatically populated.
   * The method will throw RuntimeException if the table name is invalid, any column name specified in <tt>columnValues</tt> is invalid
   * or column value is of a different data type than the column definition.
   * @param tableName name of the table in which a row has to be inserted
   * @param columnValues name-value pairs of the columns that should be inserted in the new row
   * @return name-values pairs of all the columns that were populated in the new row
   */
  public final Map<String, Object> into(String tableName, Map<String, Object> columnValues) {
    return new AutomaticPopulater(new JdbcTemplate(dataSource)).populate(tableName, new HashMap<>(columnValues));
  }

  /**
   * Insert a new row into a table and automatically populate all of its columns that have a not null or foreign key constraint.
   * The method will throw RuntimeException if the table name is invalid.
   * @param tableName name of the table in which a row has to be inserted
   * @return name-values pairs of all the columns that were populated in the new row
   */
  public final Map<String, Object> into(String tableName) {
    return into(tableName, emptyMap());
  }
}
