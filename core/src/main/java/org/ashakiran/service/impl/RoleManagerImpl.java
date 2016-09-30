package org.ashakiran.service.impl;

import java.util.List;

import org.ashakiran.dao.RoleDao;

import org.ashakiran.model.Role;

import org.ashakiran.service.RoleManager;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


/**
 * Implementation of RoleManager interface.
 *
 * @author   <a href="mailto:dan@getrolling.com">Dan Kibler</a>
 * @version  $Revision$, $Date$
 */
@Service("roleManager")
public class RoleManagerImpl extends GenericManagerImpl<Role, Long> implements RoleManager {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** DOCUMENT ME! */
  RoleDao roleDao;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new RoleManagerImpl object.
   *
   * @param  roleDao  DOCUMENT ME!
   */
  @Autowired public RoleManagerImpl(RoleDao roleDao) {
    super(roleDao);
    this.roleDao = roleDao;
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  public Role getRole(String rolename) {
    return roleDao.getRoleByName(rolename);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  public List<Role> getRoles(Role role) {
    return dao.getAll();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  public void removeRole(String rolename) {
    roleDao.removeRole(rolename);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  public Role saveRole(Role role) {
    return dao.save(role);
  }
} // end class RoleManagerImpl
