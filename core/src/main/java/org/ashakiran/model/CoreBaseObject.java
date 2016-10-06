package org.ashakiran.model;


import java.io.Serializable;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

import org.directwebremoting.convert.ObjectConverter;


/**
 * DOCUMENT ME!
 *
 * @author   $author$
 * @version  $Revision$, $Date$
 */
@DataTransferObject(converter = ObjectConverter.class)
@MappedSuperclass public abstract class CoreBaseObject extends BaseObject implements Serializable {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final long serialVersionUID = -5137348947151932448L;

  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** DOCUMENT ME! */
  @Column(
    name      = "createDate",
    nullable  = false,
    updatable = false
  )
  @RemoteProperty
  @Temporal(TemporalType.TIMESTAMP)
  protected Date                 createDate;

  /** Update date. */
  @Column(name = "lastUpdateDate")
  @RemoteProperty
  @Temporal(TemporalType.TIMESTAMP)
  protected Date lastUpdateDate;

  // protected Integer version = 0;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Set createDate when new an object.
   */
  public CoreBaseObject() {
    this.createDate     = new Date();
    this.lastUpdateDate = createDate;
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null) {
      return false;
    }

    if (getClass() != obj.getClass()) {
      return false;
    }

    final CoreBaseObject other = (CoreBaseObject) obj;

    if (this.createDate == null) {
      if (other.getCreateDate() != null) {
        return false;
      }
    } else if (!this.createDate.equals(other.getCreateDate())) {
      return false;
    }

    return true;
  } // end method equals

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  the createDate
   *
   * @hibernate.property
   *   not-null = "true"
   *   update   = "false"
   */
  public Date getCreateDate() {
    return createDate;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  // /**
  // * @return the version
  // * @hibernate.version
  // */
  // public Integer getVersion() {
  // return this.version;
  // }

  /**
   * DOCUMENT ME!
   *
   * @return  the lastUpdateDate
   *
   * @hibernate.property
   *   not-null = "false"
   */
  public Date getLastUpdateDate() {
    return lastUpdateDate;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override public int hashCode() {
    final int PRIME  = 31;
    int       result = 31;
    result = (PRIME * result)
      + ((this.createDate == null) ? 0 : this.createDate.hashCode());

    return result;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  createDate  the createDate to set
   */
  public void setCreateDate(Date createDate) {
    if (createDate == null) {
      this.createDate = new Date();
    } else {
      this.createDate = createDate;
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  // /**
  // * @param version
  // * the version to set
  // */
  // public void setVersion(Integer version) {
  // this.version = version;
  // }

  /**
   * DOCUMENT ME!
   *
   * @param  lastUpdateDate  the lastUpdateDate to set
   */
  public void setLastUpdateDate(Date lastUpdateDate) {
    this.lastUpdateDate = lastUpdateDate;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Constructs a <code>String</code> with all attributes in name = value format.
   *
   * @return  a <code>String</code> representation of this object.
   */
  @Override public String toString() {
    final String TAB = "    ";

    StringBuilder retValue = new StringBuilder();

    retValue.append("BaseObject ( ").append("createDate = ").append(
      this.createDate).append(TAB).append("updateDate = ").append(
      this.lastUpdateDate).append(TAB).append(" )");

    return retValue.toString();
  }
} // end class CoreBaseObject
