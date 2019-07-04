package me.wener.wava.constraints;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2019-07-05
 */
@Documented
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@ReportAsSingleViolation
@Constraint(validatedBy = {})
@Pattern(regexp = "^[0-9]+$")
public @interface Numberic {
  String message() default "{me.wener.wava.constraints.Numberic.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
