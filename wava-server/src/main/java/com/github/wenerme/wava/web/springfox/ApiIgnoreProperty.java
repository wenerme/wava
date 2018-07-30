package com.github.wenerme.wava.web.springfox;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import io.swagger.annotations.ApiModelProperty;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 在接口上隐藏部分模型属性
 *
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 16/6/22
 * @deprecated 使用 {@link ApiModelProperty#hidden()}
 */
@Deprecated
@Documented
@Target({ElementType.FIELD})
@Retention(RUNTIME)
public @interface ApiIgnoreProperty {}
