package org.ashakiran.webapp.util;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.util.ValidatorUtils;

import org.springframework.validation.Errors;

import org.springmodules.validation.commons.FieldChecks;


/**
 * ValidationUtil Helper class for performing custom validations that aren't already included in the core Commons
 * Validator.
 *
 * <p><a href="ValidationUtil.java.html"><i>View Source</i></a></p>
 *
 * @author   <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version  $Revision$, $Date$
 */
public class ValidationUtil {
  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * Validates that two fields match.
   *
   * @param   bean    DOCUMENT ME!
   * @param   va      DOCUMENT ME!
   * @param   field   DOCUMENT ME!
   * @param   errors  DOCUMENT ME!
   *
   * @return  validates that two fields match.
   */
  public static boolean validateTwoFields(Object bean, ValidatorAction va,
    Field field, Errors errors) {
    String value      = ValidatorUtils.getValueAsString(bean, field.getProperty());
    String sProperty2 = field.getVarValue("secondProperty");
    String value2     = ValidatorUtils.getValueAsString(bean, sProperty2);

    if (!GenericValidator.isBlankOrNull(value)) {
      try {
        if (!value.equals(value2)) {
          FieldChecks.rejectValue(errors, field, va);

          return false;
        }
      } catch (Exception e) {
        FieldChecks.rejectValue(errors, field, va);

        return false;
      }
    }

    return true;
  }
} // end class ValidationUtil
