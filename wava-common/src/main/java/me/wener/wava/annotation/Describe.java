package me.wener.wava.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;

/**
 * 辅助注解 - 用于添加相关的描述
 *
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 16/8/30
 */
@Retention(RUNTIME)
@Documented
@Inherited
public @interface Describe {

  /** 概述 */
  String value();

  /** 明细 */
  String detail() default "";
}
