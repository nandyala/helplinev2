package org.ashakiran.dao;

import java.util.List;

import org.ashakiran.model.User;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * User Data Access Object (GenericDao) interface.
 *
 * @author   <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version  $Revision$, $Date$
 */
public interface UserDao extends GenericDao<User, Long> {
  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * Retrieves the password in DB for a user.
   *
   * @param   userId  the user's id
   *
   * @return  the password in DB, if the user is already persisted
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  String getUserPassword(Long userId);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Gets a list of users ordered by the uppercase version of their username.
   *
   * @return  List populated list of users
   */
  List<User> getUsers();

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Gets users information based on login name.
   *
   * @param   username  the user's username
   *
   * @return  userDetails populated userDetails object
   *
   * @throws  org.springframework.security.core.userdetails.UsernameNotFoundException  thrown when user not found in
   *                                                                                   database
   * @throws  UsernameNotFoundException                                                DOCUMENT ME!
   */
  @Transactional UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Saves a user's information.
   *
   * @param   user  the object to be saved
   *
   * @return  the persisted User object
   */
  User saveUser(User user);

} // end interface UserDao
