package org.ashakiran.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;


/**
 * This class is used to represent an address with address, city, province and postal-code information.
 *
 * @author   <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version  $Revision$, $Date$
 */
@Embeddable @Indexed public class Address extends BaseObject implements Serializable {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final long serialVersionUID = 3617859655330969141L;

  //~ Instance fields --------------------------------------------------------------------------------------------------

  private String address;
  private String city;
  private String country;
  private String postalCode;
  private String province;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * Overridden equals method for object comparison. Compares based on hashCode.
   *
   * @param   o  Object to compare
   *
   * @return  true/false based on hashCode
   */
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Address)) {
      return false;
    }

    final Address address1 = (Address) o;

    return this.hashCode() == address1.hashCode();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Column(length = 150)
  @Field public String getAddress() {
    return address;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Column(length = 50)
  @Field public String getCity() {
    return city;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Column(length = 100)
  @Field public String getCountry() {
    return country;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Column(
    name   = "postal_code",
    length = 15
  )
  @Field(analyze = Analyze.NO)
  public String getPostalCode() {
    return postalCode;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Column(length = 100)
  @Field public String getProvince() {
    return province;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Overridden hashCode method - compares on address, city, province, country and postal code.
   *
   * @return  hashCode
   */
  public int hashCode() {
    int result;
    result = ((address != null) ? address.hashCode() : 0);
    result = (29 * result) + ((city != null) ? city.hashCode() : 0);
    result = (29 * result) + ((province != null) ? province.hashCode() : 0);
    result = (29 * result) + ((country != null) ? country.hashCode() : 0);
    result = (29 * result) + ((postalCode != null) ? postalCode.hashCode() : 0);

    return result;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  address  DOCUMENT ME!
   */
  public void setAddress(String address) {
    this.address = address;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  city  DOCUMENT ME!
   */
  public void setCity(String city) {
    this.city = city;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  country  DOCUMENT ME!
   */
  public void setCountry(String country) {
    this.country = country;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  postalCode  DOCUMENT ME!
   */
  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  province  DOCUMENT ME!
   */
  public void setProvince(String province) {
    this.province = province;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Returns a multi-line String with key=value pairs.
   *
   * @return  a String representation of this class.
   */
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("country", this.country).append("address",
        this.address).append("province", this.province).append("postalCode", this.postalCode).append("city", this.city)
      .toString();
  }
} // end class Address
