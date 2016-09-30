package org.ashakiran.service;

import java.util.List;

import org.ashakiran.dao.UserDao;

import org.ashakiran.model.User;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * Business Service Interface to handle communication between web and persistence layer.
 *
 * @author   <a href="mailto:matt@raibledesigns.com">Matt Raible</a> Modified by <a href="mailto:dan@getrolling.com">Dan
 *           Kibler</a>
 * @version  $Revision$, $Date$
 */
public interface UserManager extends GenericManager<User, Long> {
  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * Builds a recovery password url by replacing placeholders with username and generated recovery token.
   *
   * <p>UrlTemplate should include two placeholders '{username}' for username and ' {token}' for the recovery token.</p>
   *
   * @param   user         DOCUMENT ME!
   * @param   urlTemplate  DOCUMENT ME! template including two placeholders '{username}' and ' {token}'
   *
   * @return  builds a recovery password url by replacing placeholders with username and generated recovery token.
   */
  String buildRecoveryPasswordUrl(User user, String urlTemplate);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   user  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  String generateRecoveryToken(User user);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Retrieves a user by userId. An exception is thrown if user not found
   *
   * @param   userId  the identifier for the user
   *
   * @return  User
   */
  User getUser(String userId);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Finds a user by their username.
   *
   * @param   username  the user's username used to login
   *
   * @return  User a populated user object
   *
   * @throws  org.springframework.security.core.userdetails.UsernameNotFoundException  exception thrown when user not
   *                                                                                   found
   * @throws  UsernameNotFoundException                                                DOCUMENT ME!
   */
  User getUserByUsername(String username) throws UsernameNotFoundException;

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Retrieves a list of all users.
   *
   * @return  List
   */
  List<User> getUsers();

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   username  DOCUMENT ME!
   * @param   token     DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  boolean isRecoveryTokenValid(String username, String token);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   user   DOCUMENT ME!
   * @param   token  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  boolean isRecoveryTokenValid(User user, String token);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Removes a user from the database.
   *
   * @param  user  the user to remove
   */
  void removeUser(User user);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Removes a user from the database by their userId.
   *
   * @param  userId  the user's id
   */
  void removeUser(String userId);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Saves a user's information.
   *
   * @param   user  the user's information
   *
   * @throws  UserExistsException  thrown when user already exists
   *
   * @return  user the updated user object
   */
  User saveUser(User user) throws UserExistsException;

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Search a user for search terms.
   *
   * @param   searchTerm  the search terms.
   *
   * @return  a list of matches, or all if no searchTerm.
   */
  List<User> search(String searchTerm);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Sends a password recovery email to username.
   *
   * @param  username     DOCUMENT ME!
   * @param  urlTemplate  url template including two placeholders '{username}' and ' {token}'
   */
  void sendPasswordRecoveryEmail(String username, String urlTemplate);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Convenience method for testing - allows you to mock the PasswordEncoder and set it on an interface.
   *
   * @param  passwordEncoder  the PasswordEncoder implementation to use
   */
  void setPasswordEncoder(PasswordEncoder passwordEncoder);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Convenience method for testing - allows you to mock the DAO and set it on an interface.
   *
   * @param  userDao  the UserDao implementation to use
   */
  void setUserDao(UserDao userDao);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   username         DOCUMENT ME!
   * @param   currentPassword  DOCUMENT ME!
   * @param   recoveryToken    DOCUMENT ME!
   * @param   newPassword      DOCUMENT ME!
   * @param   applicationUrl   DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  UserExistsException
   */
  User updatePassword(String username, String currentPassword, String recoveryToken, String newPassword,
    String applicationUrl) throws UserExistsException;
} // end interface UserManager
