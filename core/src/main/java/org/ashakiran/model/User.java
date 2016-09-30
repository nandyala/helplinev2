package org.ashakiran.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import org.codehaus.jackson.annotate.JsonIgnore;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


/**
 * This class represents the basic "user" object in AppFuse that allows for authentication and user management. It
 * implements Spring Security's UserDetails interface.
 *
 * @author   <a href="mailto:matt@raibledesigns.com">Matt Raible</a> Updated by Dan Kibler (dan@getrolling.com) Extended
 *           to implement Spring UserDetails interface by David Carter david@carter.net
 * @version  $Revision$, $Date$
 */
@Entity @Indexed
@Table(name = "app_user")
@XmlRootElement public class User extends BaseObject implements Serializable, UserDetails {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final long serialVersionUID = 3832626162173359411L;

  //~ Instance fields --------------------------------------------------------------------------------------------------

  private boolean accountExpired;
  private boolean accountLocked;
  private Address address            = new Address();
  private String  confirmPassword;
  private boolean credentialsExpired;
  private String  email;     // required; unique
  private boolean enabled;
  private String  firstName; // required

  private Long      id;
  private String    lastName; // required
  private String    password; // required
  private String    passwordHint;
  private String    phoneNumber;
  private Set<Role> roles            = new HashSet<Role>();
  private String    username; // required
  private Integer   version;
  private String    website;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Default constructor - creates a new instance with no values set.
   */
  public User() { }

  /**
   * Create a new instance and set the username.
   *
   * @param  username  login name for user.
   */
  public User(final String username) {
    this.username = username;
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * Adds a role for the user.
   *
   * @param  role  the fully instantiated role
   */
  public void addRole(Role role) {
    getRoles().add(role);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof User)) {
      return false;
    }

    final User user = (User) o;

    return !((username != null) ? (!username.equals(user.getUsername())) : (user.getUsername() != null));

  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Embedded @IndexedEmbedded public Address getAddress() {
    return address;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * GrantedAuthority[] an array of roles.
   *
   * @return  GrantedAuthority[] an array of roles.
   *
   * @see     org.springframework.security.core.userdetails.UserDetails#getAuthorities()
   */
  @JsonIgnore // needed for UserApiITest in appfuse-ws archetype
  @Transient public Set<GrantedAuthority> getAuthorities() {
    Set<GrantedAuthority> authorities = new LinkedHashSet<>();
    authorities.addAll(roles);

    return authorities;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @JsonIgnore @Transient @XmlTransient public String getConfirmPassword() {
    return confirmPassword;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Column(
    nullable = false,
    unique   = true
  )
  @Field public String getEmail() {
    return email;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Column(
    name     = "first_name",
    nullable = false,
    length   = 50
  )
  @Field public String getFirstName() {
    return firstName;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Returns the full name.
   *
   * @return  firstName + ' ' + lastName
   */
  @Transient public String getFullName() {
    return firstName + ' ' + lastName;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @DocumentId
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Id public Long getId() {
    return id;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Column(
    name     = "last_name",
    nullable = false,
    length   = 50
  )
  @Field public String getLastName() {
    return lastName;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Column(nullable = false)
  @JsonIgnore @XmlTransient public String getPassword() {
    return password;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Column(name = "password_hint")
  @XmlTransient public String getPasswordHint() {
    return passwordHint;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Column(name = "phone_number")
  @Field(analyze = Analyze.NO)
  public String getPhoneNumber() {
    return phoneNumber;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Convert user roles to LabelValue objects for convenience.
   *
   * @return  a list of LabelValue objects with role information
   */
  @Transient public List<LabelValue> getRoleList() {
    List<LabelValue> userRoles = new ArrayList<LabelValue>();

    if (this.roles != null) {
      for (Role role : roles) {
        // convert the user's roles to LabelValue Objects
        userRoles.add(new LabelValue(role.getName(), role.getName()));
      }
    }

    return userRoles;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Fetch(FetchMode.SELECT)
  @JoinTable(
    name               = "user_role",
    joinColumns        = { @JoinColumn(name = "user_id") },
    inverseJoinColumns = @JoinColumn(name = "role_id")
  )
  @ManyToMany(fetch = FetchType.EAGER)
  public Set<Role> getRoles() {
    return roles;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Column(
    nullable = false,
    length   = 50,
    unique   = true
  )
  @Field public String getUsername() {
    return username;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Version public Integer getVersion() {
    return version;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Field public String getWebsite() {
    return website;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  public int hashCode() {
    return ((username != null) ? username.hashCode() : 0);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Column(
    name     = "account_expired",
    nullable = false
  )
  public boolean isAccountExpired() {
    return accountExpired;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Column(
    name     = "account_locked",
    nullable = false
  )
  public boolean isAccountLocked() {
    return accountLocked;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * True if account is still active.
   *
   * @see     org.springframework.security.core.userdetails.UserDetails#isAccountNonExpired()
   *
   * @return  true if account is still active
   */
  @Transient public boolean isAccountNonExpired() {
    return !isAccountExpired();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * False if account is locked.
   *
   * @see     org.springframework.security.core.userdetails.UserDetails#isAccountNonLocked()
   *
   * @return  false if account is locked
   */
  @Transient public boolean isAccountNonLocked() {
    return !isAccountLocked();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Column(
    name     = "credentials_expired",
    nullable = false
  )
  public boolean isCredentialsExpired() {
    return credentialsExpired;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * True if credentials haven't expired.
   *
   * @see     org.springframework.security.core.userdetails.UserDetails#isCredentialsNonExpired()
   *
   * @return  true if credentials haven't expired
   */
  @Transient public boolean isCredentialsNonExpired() {
    return !credentialsExpired;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Column(name = "account_enabled")
  public boolean isEnabled() {
    return enabled;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  accountExpired  DOCUMENT ME!
   */
  public void setAccountExpired(boolean accountExpired) {
    this.accountExpired = accountExpired;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  accountLocked  DOCUMENT ME!
   */
  public void setAccountLocked(boolean accountLocked) {
    this.accountLocked = accountLocked;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  address  DOCUMENT ME!
   */
  public void setAddress(Address address) {
    this.address = address;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  confirmPassword  DOCUMENT ME!
   */
  public void setConfirmPassword(String confirmPassword) {
    this.confirmPassword = confirmPassword;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  credentialsExpired  DOCUMENT ME!
   */
  public void setCredentialsExpired(boolean credentialsExpired) {
    this.credentialsExpired = credentialsExpired;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  email  DOCUMENT ME!
   */
  public void setEmail(String email) {
    this.email = email;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  enabled  DOCUMENT ME!
   */
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
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
   * @param  id  DOCUMENT ME!
   */
  public void setId(Long id) {
    this.id = id;
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

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  password  DOCUMENT ME!
   */
  public void setPassword(String password) {
    this.password = password;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  passwordHint  DOCUMENT ME!
   */
  public void setPasswordHint(String passwordHint) {
    this.passwordHint = passwordHint;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  phoneNumber  DOCUMENT ME!
   */
  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  roles  DOCUMENT ME!
   */
  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  username  DOCUMENT ME!
   */
  public void setUsername(String username) {
    this.username = username;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  version  DOCUMENT ME!
   */
  public void setVersion(Integer version) {
    this.version = version;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  website  DOCUMENT ME!
   */
  public void setWebsite(String website) {
    this.website = website;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  public String toString() {
    ToStringBuilder sb = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("username", this.username)
      .append("enabled", this.enabled).append("accountExpired", this.accountExpired).append("credentialsExpired",
        this.credentialsExpired).append("accountLocked", this.accountLocked);

    if (roles != null) {
      sb.append("Granted Authorities: ");

      int i = 0;

      for (Role role : roles) {
        if (i > 0) {
          sb.append(", ");
        }

        sb.append(role.toString());
        i++;
      }
    } else {
      sb.append("No Granted Authorities");
    }

    return sb.toString();
  }
} // end class User
