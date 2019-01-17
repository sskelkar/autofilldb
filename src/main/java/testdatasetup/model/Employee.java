package testdatasetup.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
public class Employee {
  @Id
  private Integer id;

  private String name;

  private LocalDate created;

  @ManyToOne
  @JoinColumn(name = "department_id")
  private Department department;

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public LocalDate getCreated() {
    return created;
  }

  public Department getDepartment() {
    return department;
  }
}
