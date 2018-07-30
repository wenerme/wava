package com.github.wenerme.wava.web.springfox;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 在无法直接使用枚举类型时,使用该注解指向实际的枚举类型,以便于生成接口描述文档
 *
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 16/6/27
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RUNTIME)
public @interface ApiEnumConstant {

  Class<? extends Enum> value();
}
