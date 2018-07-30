package com.github.wenerme.wava.constraints;

import com.google.common.net.InternetDomainName;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2017/3/21
 */
public class DomainConstraintValidator implements ConstraintValidator<Domain, String> {

  private boolean underPublicSuffix;

  @Override
  public void initialize(Domain constraintAnnotation) {
    underPublicSuffix = constraintAnnotation.underPublicSuffix();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }

    InternetDomainName name;
    try {
      name = InternetDomainName.from(value);
    } catch (Exception e) {
      return false;
    }
    if (underPublicSuffix) {
      if (!name.isUnderPublicSuffix()) {
        return false;
      }
    }

    return true;
  }
}
