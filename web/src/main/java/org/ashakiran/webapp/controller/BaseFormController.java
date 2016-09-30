package org.ashakiran.webapp.controller;

import java.io.Serializable;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.ashakiran.Constants;

import org.ashakiran.model.User;

import org.ashakiran.service.MailEngine;
import org.ashakiran.service.UserManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;

import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;

import org.springframework.mail.SimpleMailMessage;

import org.springframework.validation.Validator;

import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;


/**
 * Implementation of <strong>SimpleFormController</strong> that contains convenience methods for subclasses. For
 * example, getting the current user and saving messages/errors. This class is intended to be a base class for all Form
 * controllers.
 *
 * <p><a href="BaseFormController.java.html"><i>View Source</i></a></p>
 *
 * @author   <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version  $Revision$, $Date$
 */
public class BaseFormController implements ServletContextAware {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  /** DOCUMENT ME! */
  public static final String MESSAGES_KEY = "successMessages";

  /** DOCUMENT ME! */
  public static final String ERRORS_MESSAGES_KEY = "errors";

  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** DOCUMENT ME! */
  protected String cancelView;

  /** DOCUMENT ME! */
  protected final transient Log log = LogFactory.getLog(getClass());

  /** DOCUMENT ME! */
  protected MailEngine mailEngine = null;

  /** DOCUMENT ME! */
  protected SimpleMailMessage message = null;

  /** DOCUMENT ME! */
  protected String successView;

  /** DOCUMENT ME! */
  protected String templateName = "accountCreated.vm";

  /** DOCUMENT ME! */
  @Autowired(required = false)
  Validator validator;

  private MessageSourceAccessor messages;
  private ServletContext        servletContext;
  private UserManager           userManager = null;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public final String getCancelView() {
    // Default to successView if cancelView is invalid
    if ((this.cancelView == null) || (this.cancelView.length() == 0)) {
      return getSuccessView();
    }

    return this.cancelView;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Convenience method to get the Configuration HashMap from the servlet context.
   *
   * @return  the user's populated form from the session
   */
  public Map getConfiguration() {
    Map config = (HashMap) servletContext.getAttribute(Constants.CONFIG);

    // so unit tests don't puke when nothing's been set
    if (config == null) {
      return new HashMap();
    }

    return config;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public final String getSuccessView() {
    return this.successView;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Convenience method for getting a i18n key's value. Calling getMessageSourceAccessor() is used because the
   * RequestContext variable is not set in unit tests b/c there's no DispatchServlet Request.
   *
   * @param   msgKey  DOCUMENT ME!
   * @param   locale  the current locale
   *
   * @return  convenience method for getting a i18n key's value.
   */
  public String getText(String msgKey, Locale locale) {
    return messages.getMessage(msgKey, locale);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Convenient method for getting a i18n key's value with a single string argument.
   *
   * @param   msgKey  DOCUMENT ME!
   * @param   arg     DOCUMENT ME!
   * @param   locale  the current locale
   *
   * @return  convenient method for getting a i18n key's value with a single string argument.
   */
  public String getText(String msgKey, String arg, Locale locale) {
    return getText(msgKey, new Object[] { arg }, locale);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Convenience method for getting a i18n key's value with arguments.
   *
   * @param   msgKey  DOCUMENT ME!
   * @param   args    DOCUMENT ME!
   * @param   locale  the current locale
   *
   * @return  convenience method for getting a i18n key's value with arguments.
   */
  public String getText(String msgKey, Object[] args, Locale locale) {
    return messages.getMessage(msgKey, args, locale);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public UserManager getUserManager() {
    return this.userManager;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  request  DOCUMENT ME!
   * @param  error    DOCUMENT ME!
   */
  @SuppressWarnings("unchecked")
  public void saveError(HttpServletRequest request, String error) {
    List errors = (List) request.getSession().getAttribute(ERRORS_MESSAGES_KEY);

    if (errors == null) {
      errors = new ArrayList();
    }

    errors.add(error);
    request.getSession().setAttribute(ERRORS_MESSAGES_KEY, errors);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  request  DOCUMENT ME!
   * @param  msg      DOCUMENT ME!
   */
  @SuppressWarnings("unchecked")
  public void saveMessage(HttpServletRequest request, String msg) {
    List messages = (List) request.getSession().getAttribute(MESSAGES_KEY);

    if (messages == null) {
      messages = new ArrayList();
    }

    messages.add(msg);
    request.getSession().setAttribute(MESSAGES_KEY, messages);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   cancelView  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public final BaseFormController setCancelView(String cancelView) {
    this.cancelView = cancelView;

    return this;
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
  @Autowired public void setMessages(MessageSource messageSource) {
    messages = new MessageSourceAccessor(messageSource);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.web.context.ServletContextAware#setServletContext(javax.servlet.ServletContext)
   */
  @Override public void setServletContext(ServletContext servletContext) {
    this.servletContext = servletContext;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   successView  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public final BaseFormController setSuccessView(String successView) {
    this.successView = successView;

    return this;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  templateName  DOCUMENT ME!
   */
  public void setTemplateName(String templateName) {
    this.templateName = templateName;
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

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  protected ServletContext getServletContext() {
    return servletContext;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Set up a custom property editor for converting form inputs to real objects.
   *
   * @param  request  the current request
   * @param  binder   the data binder
   */
  @InitBinder protected void initBinder(HttpServletRequest request,
    ServletRequestDataBinder binder) {
    binder.registerCustomEditor(Integer.class, null,
      new CustomNumberEditor(Integer.class, null, true));
    binder.registerCustomEditor(Long.class, null,
      new CustomNumberEditor(Long.class, null, true));
    binder.registerCustomEditor(byte[].class,
      new ByteArrayMultipartFileEditor());

    SimpleDateFormat dateFormat = new SimpleDateFormat(getText("date.format", request.getLocale()));
    dateFormat.setLenient(false);
    binder.registerCustomEditor(Date.class, null,
      new CustomDateEditor(dateFormat, true));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Convenience message to send messages to users, includes app URL as footer.
   *
   * @param  user  the user to send a message to.
   * @param  msg   the message to send.
   * @param  url   the URL of the application.
   */
  protected void sendUserMessage(User user, String msg, String url) {
    if (log.isDebugEnabled()) {
      log.debug("sending e-mail to user [" + user.getEmail() + "]...");
    }

    message.setTo(user.getFullName() + "<" + user.getEmail() + ">");

    Map<String, Serializable> model = new HashMap<String, Serializable>();
    model.put("user", user);

    // TODO: once you figure out how to get the global resource bundle in
    // WebWork, then figure it out here too.  In the meantime, the Username
    // and Password labels are hard-coded into the template.
    // model.put("bundle", getTexts());
    model.put("message", msg);
    model.put("applicationURL", url);
    mailEngine.sendMessage(message, templateName, model);
  }
} // end class BaseFormController
