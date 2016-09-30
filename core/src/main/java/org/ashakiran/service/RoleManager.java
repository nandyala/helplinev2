package org.ashakiran.service;

import java.util.List;

import org.ashakiran.model.Role;


/**
 * Business Service Interface to handle communication between web and persistence layer.
 *
 * @author   <a href="mailto:dan@getrolling.com">Dan Kibler</a>
 * @version  $Revision$, $Date$
 */
public interface RoleManager extends GenericManager<Role, Long> {
  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  Role getRole(String rolename);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  List getRoles(Role role);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  void removeRole(String rolename);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  Role saveRole(Role role);
} // end interface RoleManager
