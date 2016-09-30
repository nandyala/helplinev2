package org.ashakiran.webapp.controller;

import org.ashakiran.Constants;

import org.ashakiran.dao.SearchException;

import org.ashakiran.service.UserManager;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


/**
 * Simple class to retrieve a list of users from the database.
 *
 * <p>
 * </p>
 *
 * <p><a href="UserController.java.html"><i>View Source</i></a></p>
 *
 * @author   <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version  $Revision$, $Date$
 */
@Controller
@RequestMapping("/admin/users*")
public class UserController {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  private UserManager userManager = null;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   query  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  @RequestMapping(method = RequestMethod.GET)
  public ModelAndView handleRequest(@RequestParam(
      required = false,
      value    = "q"
    ) String query) throws Exception {
    Model model = new ExtendedModelMap();

    try {
      model.addAttribute(Constants.USER_LIST, userManager.search(query));
    } catch (SearchException se) {
      model.addAttribute("searchError", se.getMessage());
      model.addAttribute(userManager.getUsers());
    }

    return new ModelAndView("admin/userList", model.asMap());
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  userManager  DOCUMENT ME!
   */
  @Autowired public void setUserManager(UserManager userManager) {
    this.userManager = userManager;
  }
} // end class UserController
