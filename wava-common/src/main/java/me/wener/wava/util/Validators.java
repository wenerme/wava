package me.wener.wava.util;

import java.util.Locale;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.MessageInterpolator;
import javax.validation.Validation;
import javax.validation.Validator;
import me.wener.wava.error.Errors;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.hibernate.validator.messageinterpolation.AbstractMessageInterpolator;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2019-07-05
 */
public interface Validators {

  static Validator validator() {
    return Holder.VALIDATOR.get();
  }

  static Validator use(Validator validator) {
    Holder.VALIDATOR.set(validator);
    return Holder.VALIDATOR.get();
  }

  static <T> Set<ConstraintViolation<T>> validate(T target) {
    return validator().validate(target);
  }

  static <T> T check(T target) {
    Set<ConstraintViolation<T>> violations = validate(target);
    if (!violations.isEmpty()) {
      String message =
          violations.stream()
              .map(v -> String.format("%s: %s", v.getPropertyPath(), v.getMessage()))
              .collect(Collectors.joining(","));
      throw Errors.badRequest().withData(violations).asException(message);
    }
    return target;
  }

  class Holder {
    private static final AtomicReference<Validator> VALIDATOR = new AtomicReference<>(build());

    private static Validator build() {
      Configuration<?> conf = Validation.byDefaultProvider().configure();
      // hv is optional
      if (conf.getClass().getSimpleName().equals("HibernateValidatorConfiguration")) {
        // try force SIMPLIFIED_CHINESE
        HibernateValidatorConfiguration configure = (HibernateValidatorConfiguration) conf;
        PlatformResourceBundleLocator locator =
            new PlatformResourceBundleLocator(AbstractMessageInterpolator.USER_VALIDATION_MESSAGES);
        ResourceBundleMessageInterpolator interpolator =
            new ResourceBundleMessageInterpolator(locator);
        configure.messageInterpolator(
            new MessageInterpolator() {
              @Override
              public String interpolate(String messageTemplate, Context context) {
                return interpolate(messageTemplate, context, Locale.SIMPLIFIED_CHINESE);
              }

              @Override
              public String interpolate(String messageTemplate, Context context, Locale locale) {
                return interpolator.interpolate(messageTemplate, context, locale);
              }
            });
      }
      return conf.buildValidatorFactory().getValidator();
    }
  }
}
