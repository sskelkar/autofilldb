package testdatasetup;

import testdatasetup.repository.EmployeeRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TestDataSetupApplication {

  public static void main(String[] args) {
    ConfigurableApplicationContext app = SpringApplication.run(TestDataSetupApplication.class, args);
    EmployeeRepository employeeRepository = app.getBean(EmployeeRepository.class);

    employeeRepository.findAll()
      .forEach(emp ->
        System.out.println(emp.getId() + " - " + emp.getName() + " - " + emp.getCreated() + " - "
          + emp.getDepartment().getName() + " - " + emp.getDepartment().getOrganisation().getCountryCode())
      );
  }

}

