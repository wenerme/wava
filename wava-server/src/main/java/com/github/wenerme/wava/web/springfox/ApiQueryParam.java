package com.github.wenerme.wava.web.springfox;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 标识该参数为查询参数,用在将 query 参数转换为一个对象时
 *
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 16/6/7
 */
@Documented
@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RUNTIME)
@Inherited
public @interface ApiQueryParam {}
