package org.ashakiran.webapp.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.ashakiran.model.User;

import org.ashakiran.service.MailEngine;
import org.ashakiran.service.UserManager;

import org.ashakiran.webapp.util.RequestUtil;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;


/**
 * Simple class to retrieve and send a password hint to users.
 *
 * @author   <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version  $Revision$, $Date$
 */
@Controller
@RequestMapping("/passwordHint*")
public class PasswordHintController {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** DOCUMENT ME! */
  protected MailEngine        mailEngine    = null;

  /** DOCUMENT ME! */
  protected SimpleMailMessage message       = null;
  private final Log           log           = LogFactory.getLog(PasswordHintController.class);
  private MessageSource       messageSource = null;
  private UserManager         userManager   = null;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   request  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  @RequestMapping(method = RequestMethod.GET)
  public ModelAndView handleRequest(HttpServletRequest request) throws Exception {
    if (log.isDebugEnabled()) {
      log.debug("entering 'handleRequest' method...");
    }

    String                username = request.getParameter("username");
    MessageSourceAccessor text     = new MessageSourceAccessor(messageSource, request.getLocale());

    // ensure that the username has been sent
    if (username == null) {
      log.warn("Username not specified, notifying user that it's a required field.");
      request.setAttribute("error", text.getMessage("errors.required", text.getMessage("user.username")));

      return new ModelAndView("login");
    }

    if (log.isDebugEnabled()) {
      log.debug("Processing Password Hint...");
    }

    // look up the user's information
    try {
      User user = userManager.getUserByUsername(username);

      StringBuffer msg = new StringBuffer();
      msg.append("Your password hint is: ").append(user.getPasswordHint());
      msg.append("\n\nLogin at: ").append(RequestUtil.getAppURL(request));

      message.setTo(user.getEmail());

      String subject = '[' + text.getMessage("webapp.name") + "] "
        + text.getMessage("user.passwordHint");
      message.setSubject(subject);
      message.setText(msg.toString());
      mailEngine.send(message);

      saveMessage(request, text.getMessage("login.passwordHint.sent", new Object[] { username, user.getEmail() }));
    } catch (UsernameNotFoundException e) {
      log.warn(e.getMessage());
      saveError(request, text.getMessage("login.passwordHint.error", new Object[] { username }));
    } catch (MailException me) {
      log.warn(me.getMessage());
      saveError(request, me.getCause().getLocalizedMessage());
    }

    return new ModelAndView(new RedirectView(request.getContextPath()));
  } // end method handleRequest

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  request  DOCUMENT ME!
   * @param  error    DOCUMENT ME!
   */
  @SuppressWarnings("unchecked")
  public void saveError(HttpServletRequest request, String error) {
    List errors = (List) request.getSession().getAttribute("errors");

    if (errors == null) {
      errors = new ArrayList();
    }

    errors.add(error);
    request.getSession().setAttribute("errors", errors);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  // this method is also in BaseForm Controller
  /**
   * DOCUMENT ME!
   *
   * @param  request  DOCUMENT ME!
   * @param  msg      DOCUMENT ME!
   */
  @SuppressWarnings("unchecked")
  public void saveMessage(HttpServletRequest request, String msg) {
    List messages = (List) request.getSession().getAttribute(BaseFormController.MESSAGES_KEY);

    if (messages == null) {
      messages = new ArrayList();
    }

    messages.add(msg);
    request.getSession().setAttribute(BaseFormController.MESSAGES_KEY, messages);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  mailEngine  DOCUMENT ME!
   */
  @Autowired public void setMailEngine(MailEngine mailEngine) {
    this.mailEngine = mailEngine;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  message  DOCUMENT ME!
   */
  @Autowired public void setMessage(SimpleMailMessage message) {
    this.message = message;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  messageSource  DOCUMENT ME!
   */
  @Autowired public void setMessageSource(MessageSource messageSource) {
    this.messageSource = messageSource;
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
} // end class PasswordHintController
