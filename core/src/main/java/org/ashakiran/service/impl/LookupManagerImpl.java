package org.ashakiran.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.ashakiran.dao.LookupDao;

import org.ashakiran.model.LabelValue;
import org.ashakiran.model.Role;

import org.ashakiran.service.LookupManager;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


/**
 * Implementation of LookupManager interface to talk to the persistence layer.
 *
 * @author   <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version  $Revision$, $Date$
 */
@Service("lookupManager")
public class LookupManagerImpl implements LookupManager {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** DOCUMENT ME! */
  @Autowired LookupDao dao;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  public List<LabelValue> getAllRoles() {
    List<Role>       roles = dao.getRoles();
    List<LabelValue> list  = new ArrayList<LabelValue>();

    for (Role role1 : roles) {
      list.add(new LabelValue(role1.getName(), role1.getName()));
    }

    return list;
  }
}
