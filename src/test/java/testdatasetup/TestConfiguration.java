package testdatasetup;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.MySQLContainer;

import javax.sql.DataSource;

@Configuration
@ComponentScan("testdatasetup")
public class TestConfiguration {
  @Bean
  public DataSource dataSource() {
    MySQLContainer mysql = new MySQLContainer("mysql:5.6.42");
    mysql.withInitScript("database.sql").start();
    HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setDriverClassName(mysql.getDriverClassName());
    hikariConfig.setJdbcUrl(mysql.getJdbcUrl());
    hikariConfig.setUsername(mysql.getUsername());
    hikariConfig.setPassword(mysql.getPassword());

    return new HikariDataSource(hikariConfig);
  }

}
