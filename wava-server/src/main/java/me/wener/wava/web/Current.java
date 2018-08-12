package me.wener.wava.web;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 注入当前安全上下文中支持的对象,例如当前的用户或授权客户端等
 *
 * @author wener
 * @since 16/4/17
 */
@Target({ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiIgnore
public @interface Current {

  /**
   * 如果该参数为必须的,但没找到则抛出授权失败的异常
   *
   * @return 该注入是否必须
   */
  boolean required() default true;
}
