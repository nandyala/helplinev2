package org.ashakiran.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * DOCUMENT ME!
 *
 * @author   $author$
 * @version  $Revision$, $Date$
 */
@Entity
@Table(name = "Grievance")
public class Grievance extends CoreBaseObject implements Serializable {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  /** DOCUMENT ME! */
  protected static final long serialVersionUID = 8536265342519184362L;

  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** DOCUMENT ME! */
  @Column(
    name   = "firstName",
    length = 48
  )
  protected String firstName;

  /** DOCUMENT ME! */
  @Column(
    name   = "grievanceDescription",
    length = 255
  )
  protected String grievanceDescription;

  /** DOCUMENT ME! */
  @Column(
    name     = "grievanceId",
    nullable = false
  )
  @GeneratedValue(strategy = IDENTITY)
  @Id protected Long grievanceId;

  /** second last name. */
  @Column(
    name   = "lastName",
    length = 48
  )
  protected String lastName;

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

    Grievance aCase = (Grievance) o;

    return com.google.common.base.Objects.equal(grievanceDescription, aCase.grievanceDescription)
      && com.google.common.base.Objects.equal(firstName, aCase.firstName)
      && com.google.common.base.Objects.equal(lastName, aCase.lastName);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public String getFirstName() {
    return firstName;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public String getGrievanceDescription() {
    return grievanceDescription;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public Long getGrievanceId() {
    return grievanceId;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public String getLastName() {
    return lastName;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Override public int hashCode() {
    final int prime  = 31;
    int       result = 1;
    result = (prime * result);
    result = (prime * result) + ((firstName == null) ? 0 : firstName.hashCode());

    return result;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  firstName  DOCUMENT ME!
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  grievanceDescription  DOCUMENT ME!
   */
  public void setGrievanceDescription(String grievanceDescription) {
    this.grievanceDescription = grievanceDescription;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  grievanceId  DOCUMENT ME!
   */
  public void setGrievanceId(Long grievanceId) {
    this.grievanceId = grievanceId;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  lastName  DOCUMENT ME!
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
} // end class Grievance
