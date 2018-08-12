package me.wener.wava.error;

import java.util.function.Function;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 17/01/2018
 */
public interface ErrorResolver extends Function<Throwable, ErrorDetailException> {}
