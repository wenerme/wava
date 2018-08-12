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

/**
 * 验证字符串是否为合法的 Json 对象字符串, 如果是 Json 数组或字符等其他类型,也不通过
 *
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2017/2/27
 */
@Documented
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@ReportAsSingleViolation
@Repeatable(JsonObject.List.class)
@Constraint(validatedBy = {})
@JsonType(types = JsonType.Type.OBJECT)
public @interface JsonObject {

  String message() default "{com.github.wenerme.wava.constraints.JsonObject.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  /** Defines several {@code @PhoneNumber} annotations on the same element. */
  @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
  @Retention(RUNTIME)
  @Documented
  @interface List {

    JsonObject[] value();
  }
}
