package com.github.wenerme.wava.constraints;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2017/5/9
 */
@Documented
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@ReportAsSingleViolation
@Repeatable(Domain.List.class)
@Constraint(validatedBy = DomainConstraintValidator.class)
public @interface Domain {

  String message() default "{com.github.wenerme.wava.constraints.Domain.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  /** @see com.google.common.net.InternetDomainName#isUnderPublicSuffix() */
  boolean underPublicSuffix() default false;

  @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
  @Retention(RUNTIME)
  @Documented
  @interface List {

    Domain[] value();
  }
}
