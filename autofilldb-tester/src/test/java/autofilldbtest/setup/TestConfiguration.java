package autofilldbtest.setup;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.testcontainers.containers.MySQLContainer;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.github.sskelkar.autofilldb")
public class TestConfiguration {
  @Bean
  public DataSource dataSource() {
    MySQLContainer mysql = new MySQLContainer("mysql:5.6.42").withDatabaseName("autofilldbtest");
    mysql.start();
    HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setDriverClassName(mysql.getDriverClassName());
    hikariConfig.setJdbcUrl(mysql.getJdbcUrl());
    hikariConfig.setUsername(mysql.getUsername());
    hikariConfig.setPassword(mysql.getPassword());

    return new HikariDataSource(hikariConfig);
  }

  @Bean
  public PlatformTransactionManager transactionManager(DataSource dataSource) {
    final DataSourceTransactionManager txManager = new DataSourceTransactionManager();
    txManager.setDataSource(dataSource);

    return txManager;
  }

}
