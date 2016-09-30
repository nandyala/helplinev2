package org.ashakiran.dao;

import org.ashakiran.model.Role;


/**
 * Role Data Access Object (DAO) interface.
 *
 * @author   <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version  $Revision$, $Date$
 */
public interface RoleDao extends GenericDao<Role, Long> {
  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * Gets role information based on rolename.
   *
   * @param   rolename  the rolename
   *
   * @return  populated role object
   */
  Role getRoleByName(String rolename);

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Removes a role from the database by name.
   *
   * @param  rolename  the role's rolename
   */
  void removeRole(String rolename);
}
