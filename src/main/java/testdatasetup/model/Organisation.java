package testdatasetup.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Organisation {
  @Id
  private Integer id;

  private String name;

  private String countryCode;

  @OneToMany(mappedBy="organisation")
  private List<Department> departments;

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getCountryCode() {
    return countryCode;
  }

  public List<Department> getDepartments() {
    return departments;
  }
}
