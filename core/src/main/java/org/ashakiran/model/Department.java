package org.ashakiran.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.search.annotations.Indexed;

import com.google.common.base.Objects;


/**
 * Created by knandyala on 9/28/2016.
 *
 * @author   $author$
 * @version  $Revision$, $Date$
 */
@Entity @Indexed
@Table(name = "Department")
public class Department extends CoreBaseObject implements Serializable {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final long serialVersionUID = -7032689701375471451L;

  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** DOCUMENT ME! */
  @Column(
    name   = "departmentDescription",
    length = 255
  )
  protected String departmentDescription;

  /** DOCUMENT ME! */
  @Column(
    name     = "departmentId",
    nullable = false
  )
  @GeneratedValue(strategy = IDENTITY)
  @Id protected Long departmentId;

  /** DOCUMENT ME! */
  @Column(
    name   = "departmentName",
    length = 96
  )
  protected String departmentName;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * @see  org.ashakiran.model.BaseObject#equals(Object)
   */
  @Override public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }

    if (!super.equals(o)) {
      return false;
    }

    Department that = (Department) o;

    return Objects.equal(departmentDescription, that.departmentDescription)
      && Objects.equal(departmentId, that.departmentId)
      && Objects.equal(departmentName, that.departmentName);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public String getDepartmentDescription() {
    return departmentDescription;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public Long getDepartmentId() {
    return departmentId;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public String getDepartmentName() {
    return departmentName;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.ashakiran.model.BaseObject#hashCode()
   */
  @Override public int hashCode() {
    return Objects.hashCode(super.hashCode(), departmentDescription, departmentId, departmentName);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  departmentDescription  DOCUMENT ME!
   */
  public void setDepartmentDescription(String departmentDescription) {
    this.departmentDescription = departmentDescription;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  departmentId  DOCUMENT ME!
   */
  public void setDepartmentId(Long departmentId) {
    this.departmentId = departmentId;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  departmentName  DOCUMENT ME!
   */
  public void setDepartmentName(String departmentName) {
    this.departmentName = departmentName;
  }
} // end class Department
