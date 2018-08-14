package me.wener.wava.constraints;

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
import javax.validation.constraints.Pattern;

/**
 * 身份证格式验证
 *
 * @author <a href=http://github.com/wenerme>wener</a>
 * @see <a href="https://zh.wikipedia.org/wiki/中华人民共和国公民身份号码">中华人民共和国公民身份号码</a>
 * @since 16/4/28
 */
@Documented
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@ReportAsSingleViolation
@Repeatable(IdentityCardNumber.List.class)
@Constraint(validatedBy = {})
@Pattern(regexp = "^\\d{14,17}X?$")
public @interface IdentityCardNumber {

  String message() default "{com.github.wenerme.wava.constraints.IdentityCardNumber.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  /** Defines several {@code @PhoneNumber} annotations on the same element. */
  @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
  @Retention(RUNTIME)
  @Documented
  @interface List {

    IdentityCardNumber[] value();
  }
}
