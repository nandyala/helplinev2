package org.ashakiran.service.impl;

import org.ashakiran.model.User;


/**
 * DOCUMENT ME!
 *
 * @author   $author$
 * @version  $Revision$, $Date$
 */
public interface PasswordTokenManager {
  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  String generateRecoveryToken(User user);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  user   DOCUMENT ME!
   * @param  token  DOCUMENT ME!
   */
  void invalidateRecoveryToken(User user, String token);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  boolean isRecoveryTokenValid(User user, String token);
} // end interface PasswordTokenManager
