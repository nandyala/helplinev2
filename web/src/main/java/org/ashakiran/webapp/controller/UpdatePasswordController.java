package org.ashakiran.webapp.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import org.ashakiran.model.User;

import org.ashakiran.webapp.util.RequestUtil;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


/**
 * Update Password Controller.
 *
 * @author   ivangsa
 * @version  $Revision$, $Date$
 */
@Controller public class UpdatePasswordController extends BaseFormController {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  /** DOCUMENT ME! */
  public static final String RECOVERY_PASSWORD_TEMPLATE = "/updatePassword?username={username}&token={token}";

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   username         DOCUMENT ME!
   * @param   token            DOCUMENT ME!
   * @param   currentPassword  DOCUMENT ME!
   * @param   password         DOCUMENT ME!
   * @param   request          DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  Exception
   * @throws  AccessDeniedException  DOCUMENT ME!
   */
  @RequestMapping(
    value  = "/updatePassword*",
    method = RequestMethod.POST
  )
  public ModelAndView onSubmit(@RequestParam(
      value    = "username",
      required = true
    ) final String username,
    @RequestParam(
      value    = "token",
      required = false
    ) final String token,
    @RequestParam(
      value    = "currentPassword",
      required = false
    ) final String currentPassword,
    @RequestParam(
      value    = "password",
      required = true
    ) final String password,
    final HttpServletRequest request) throws Exception {
    if (log.isDebugEnabled()) {
      log.debug("PasswordRecoveryController onSubmit for username: " + username);
    }

    final Locale locale = request.getLocale();

    if (StringUtils.isEmpty(password)) {
      saveError(request, getText("errors.required", getText("updatePassword.newPassword.label", locale), locale));

      return showForm(username, null, request);
    }

    User          user       = null;
    final boolean usingToken = StringUtils.isNotBlank(token);

    if (usingToken) {
      if (log.isDebugEnabled()) {
        log.debug("Updating Password for username " + username + ", using reset token");
      }

      user = getUserManager().updatePassword(username, null, token, password,
          RequestUtil.getAppURL(request));

    } else {
      if (log.isDebugEnabled()) {
        log.debug("Updating Password for username " + username + ", using current password");
      }

      if (!username.equals(request.getRemoteUser())) {
        throw new AccessDeniedException("You do not have permission to modify other users password.");
      }

      user = getUserManager().updatePassword(username, currentPassword, null, password,
          RequestUtil.getAppURL(request));
    }

    if (user != null) {
      saveMessage(request, getText("updatePassword.success", new Object[] { username }, locale));
    } else {
      if (usingToken) {
        saveError(request, getText("updatePassword.invalidToken", locale));
      } else {
        saveError(request, getText("updatePassword.invalidPassword", locale));

        return showForm(username, null, request);
      }
    }

    return new ModelAndView("redirect:/");
  } // end method onSubmit

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   username  DOCUMENT ME!
   * @param   request   DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @RequestMapping(
    value  = "/requestRecoveryToken*",
    method = RequestMethod.GET
  )
  public String requestRecoveryToken(@RequestParam(
      value    = "username",
      required = true
    ) final String username,
    final HttpServletRequest request) {
    if (log.isDebugEnabled()) {
      log.debug("Sending recovery token to user " + username);
    }

    try {
      getUserManager().sendPasswordRecoveryEmail(username, RequestUtil.getAppURL(request) + RECOVERY_PASSWORD_TEMPLATE);
    } catch (final UsernameNotFoundException ignored) {
      // lets ignore this
    }

    saveMessage(request, getText("updatePassword.recoveryToken.sent", request.getLocale()));

    return "redirect:/";
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   username  DOCUMENT ME!
   * @param   token     DOCUMENT ME!
   * @param   request   DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @RequestMapping(
    value      = "/updatePassword*",
    method     = RequestMethod.GET
  )
  public ModelAndView showForm(@RequestParam(
      value    = "username",
      required = false
    ) String username,
    @RequestParam(
      value    = "token",
      required = false
    ) final String token,
    final HttpServletRequest request) {
    if (StringUtils.isBlank(username)) {
      username = request.getRemoteUser();
    }

    if (StringUtils.isNotBlank(token) && !getUserManager().isRecoveryTokenValid(username, token)) {
      saveError(request, getText("updatePassword.invalidToken", request.getLocale()));

      return new ModelAndView("redirect:/");
    }

    return new ModelAndView("updatePasswordForm").addObject("username", username).addObject("token", token);
  }

} // end class UpdatePasswordController
