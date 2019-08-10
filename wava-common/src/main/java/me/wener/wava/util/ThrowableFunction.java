package me.wener.wava.util;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2019-07-19
 */
@FunctionalInterface
public interface ThrowableFunction<R, T, E extends Throwable> {

  R apply(T t) throws E;
}
