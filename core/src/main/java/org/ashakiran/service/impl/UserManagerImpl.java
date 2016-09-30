package org.ashakiran.service.impl;

import java.io.Serializable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;

import org.apache.commons.lang.StringUtils;

import org.ashakiran.dao.UserDao;

import org.ashakiran.model.User;

import org.ashakiran.service.MailEngine;
import org.ashakiran.service.UserExistsException;
import org.ashakiran.service.UserManager;
import org.ashakiran.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.mail.SimpleMailMessage;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;


/**
 * Implementation of UserManager interface.
 *
 * @author   <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version  $Revision$, $Date$
 */
@Service("userManager")
@WebService(
  serviceName       = "UserService",
  endpointInterface = "org.ashakiran.service.UserService"
)
public class UserManagerImpl extends GenericManagerImpl<User, Long> implements UserManager, UserService {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  private MailEngine        mailEngine;
  private SimpleMailMessage message;
  private PasswordEncoder   passwordEncoder;

  private String               passwordRecoveryTemplate = "passwordRecovery.vm";
  private PasswordTokenManager passwordTokenManager;
  private String               passwordUpdatedTemplate  = "passwordUpdated.vm";
  private UserDao              userDao;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   user         DOCUMENT ME!
   * @param   urlTemplate  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Override public String buildRecoveryPasswordUrl(final User user, final String urlTemplate) {
    final String token    = generateRecoveryToken(user);
    final String username = user.getUsername();

    return StringUtils.replaceEach(urlTemplate,
        new String[] { "{username}", "{token}" },
        new String[] { username, token });
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   user  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Override public String generateRecoveryToken(final User user) {
    return passwordTokenManager.generateRecoveryToken(user);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  @Override public User getUser(final String userId) {
    return userDao.get(new Long(userId));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   *
   * @param   username  the login name of the human
   *
   * @return  User the populated user object
   *
   * @throws  org.springframework.security.core.userdetails.UsernameNotFoundException  thrown when username not found
   */
  @Override public User getUserByUsername(final String username) throws UsernameNotFoundException {
    return (User) userDao.loadUserByUsername(username);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  @Override public List<User> getUsers() {
    return userDao.getAllDistinct();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  @Override public boolean isRecoveryTokenValid(final String username, final String token) {
    return isRecoveryTokenValid(getUserByUsername(username), token);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   user   DOCUMENT ME!
   * @param   token  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Override public boolean isRecoveryTokenValid(final User user, final String token) {
    return passwordTokenManager.isRecoveryTokenValid(user, token);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  @Override public void removeUser(final User user) {
    if (log.isDebugEnabled()) {
      log.debug("removing user: " + user);
    }

    userDao.remove(user);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  @Override public void removeUser(final String userId) {
    if (log.isDebugEnabled()) {
      log.debug("removing user: " + userId);
    }

    userDao.remove(new Long(userId));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  @Override public User saveUser(final User user) throws UserExistsException {
    if (user.getVersion() == null) {
      // if new user, lowercase userId
      user.setUsername(user.getUsername().toLowerCase());
    }

    // Get and prepare password management-related artifacts
    boolean passwordChanged = false;

    if (passwordEncoder != null) {
      // Check whether we have to encrypt (or re-encrypt) the password
      if (user.getVersion() == null) {
        // New user, always encrypt
        passwordChanged = true;
      } else {
        // Existing user, check password in DB
        final String currentPassword = userDao.getUserPassword(user.getId());

        if (currentPassword == null) {
          passwordChanged = true;
        } else {
          if (!currentPassword.equals(user.getPassword())) {
            passwordChanged = true;
          }
        }
      }

      // If password was changed (or new user), encrypt it
      if (passwordChanged) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
      }
    } else {
      log.warn("PasswordEncoder not set, skipping password encryption...");
    } // end if-else

    try {
      return userDao.saveUser(user);
    } catch (final Exception e) {
      e.printStackTrace();
      log.warn(e.getMessage());
      throw new UserExistsException("User '" + user.getUsername() + "' already exists!");
    }
  } // end method saveUser

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  @Override public List<User> search(final String searchTerm) {
    return super.search(searchTerm, User.class);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  @Override public void sendPasswordRecoveryEmail(final String username, final String urlTemplate) {
    if (log.isDebugEnabled()) {
      log.debug("Sending password recovery token to user: " + username);
    }

    final User   user = getUserByUsername(username);
    final String url  = buildRecoveryPasswordUrl(user, urlTemplate);

    sendUserEmail(user, passwordRecoveryTemplate, url, "Password Recovery");
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  mailEngine  DOCUMENT ME!
   */
  @Autowired(required = false)
  public void setMailEngine(final MailEngine mailEngine) {
    this.mailEngine = mailEngine;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  message  DOCUMENT ME!
   */
  @Autowired(required = false)
  public void setMailMessage(final SimpleMailMessage message) {
    this.message = message;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  passwordEncoder  DOCUMENT ME!
   */
  @Autowired
  @Qualifier("passwordEncoder")
  public void setPasswordEncoder(final PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Velocity template name to send users a password recovery mail (default passwordRecovery.vm).
   *
   * @param  passwordRecoveryTemplate  the Velocity template to use (relative to classpath)
   *
   * @see    org.ashakiran.service.MailEngine#sendMessage(org.springframework.mail.SimpleMailMessage, String,
   *         java.util.Map)
   */
  public void setPasswordRecoveryTemplate(final String passwordRecoveryTemplate) {
    this.passwordRecoveryTemplate = passwordRecoveryTemplate;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  passwordTokenManager  DOCUMENT ME!
   */
  @Autowired(required = false)
  public void setPasswordTokenManager(final PasswordTokenManager passwordTokenManager) {
    this.passwordTokenManager = passwordTokenManager;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Velocity template name to inform users their password was updated (default passwordUpdated.vm).
   *
   * @param  passwordUpdatedTemplate  the Velocity template to use (relative to classpath)
   *
   * @see    org.ashakiran.service.MailEngine#sendMessage(org.springframework.mail.SimpleMailMessage, String,
   *         java.util.Map)
   */
  public void setPasswordUpdatedTemplate(final String passwordUpdatedTemplate) {
    this.passwordUpdatedTemplate = passwordUpdatedTemplate;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  userDao  DOCUMENT ME!
   */
  @Autowired @Override public void setUserDao(final UserDao userDao) {
    this.dao     = userDao;
    this.userDao = userDao;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  @Override public User updatePassword(final String username, final String currentPassword, final String recoveryToken,
    final String newPassword, final String applicationUrl) throws UserExistsException {
    User user = getUserByUsername(username);

    if (isRecoveryTokenValid(user, recoveryToken)) {
      if (log.isDebugEnabled()) {
        log.debug("Updating password from recovery token for user: " + username);
      }

      user.setPassword(newPassword);
      user = saveUser(user);
      passwordTokenManager.invalidateRecoveryToken(user, recoveryToken);

      sendUserEmail(user, passwordUpdatedTemplate, applicationUrl, "Password Updated");

      return user;
    } else if (StringUtils.isNotBlank(currentPassword)) {
      if (passwordEncoder.matches(currentPassword, user.getPassword())) {
        if (log.isDebugEnabled()) {
          log.debug("Updating password (providing current password) for user:" + username);
        }

        user.setPassword(newPassword);
        user = saveUser(user);

        return user;
      }
    }

    // or throw exception
    return null;
  } // end method updatePassword

  //~ ------------------------------------------------------------------------------------------------------------------

  private void sendUserEmail(final User user, final String template, final String url, final String subject) {
    message.setTo(user.getFullName() + "<" + user.getEmail() + ">");
    message.setSubject(subject);

    final Map<String, Serializable> model = new HashMap<String, Serializable>();
    model.put("user", user);
    model.put("applicationURL", url);

    mailEngine.sendMessage(message, template, model);
  }
} // end class UserManagerImpl
