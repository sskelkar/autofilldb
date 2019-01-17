package testdatasetup.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Department {
  @Id
  private String id;

  private String name;

  private Integer established;

  @ManyToOne
  @JoinColumn(name = "organisation_id")
  private Organisation organisation;

  @OneToMany(mappedBy = "department")
  private List<Employee> employees;

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Integer getEstablished() {
    return established;
  }

  public Organisation getOrganisation() {
    return organisation;
  }

  public List<Employee> getEmployees() {
    return employees;
  }
}
