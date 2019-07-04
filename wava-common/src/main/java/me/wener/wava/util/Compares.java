package me.wener.wava.util;

import java.util.Optional;
import me.wener.wava.error.Errors;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2019-07-05
 */
public interface Compares {

  @SafeVarargs
  static <T> Optional<T> tryFirstNonNull(T... args) {
    for (T t : args) {
      if (t != null) {
        return Optional.of(t);
      }
    }
    return Optional.empty();
  }

  @SafeVarargs
  static <T> T firstNonNull(T a, T... args) {
    if (a != null) {
      return a;
    }
    for (T t : args) {
      if (t != null) {
        return t;
      }
    }
    throw Errors.badRequest().asException("firstNonNull get all null");
  }

  static <T extends Comparable<T>> T clamp(T val, T min, T max) {
    if (val == null) {
      return null;
    }
    if (lt(val, min)) {
      return min;
    }
    if (gt(val, max)) {
      return max;
    }
    return val;
  }

  static <T extends Comparable<T>> T clamp(T val, T min, T max, T def) {
    return val == null ? def : clamp(val, min, max);
  }

  static <T extends Comparable<T>> T little(T a, T b) {
    return lte(a, b) ? a : b;
  }

  static <T extends Comparable<T>> T bigger(T a, T b) {
    return gte(a, b) ? a : b;
  }

  static <T extends Comparable<T>> boolean lt(T a, T b) {
    return a.compareTo(b) < 0;
  }

  static <T extends Comparable<T>> boolean lte(T a, T b) {
    return a.compareTo(b) <= 0;
  }

  static <T extends Comparable<T>> boolean gt(T a, T b) {
    return a.compareTo(b) > 0;
  }

  static <T extends Comparable<T>> boolean gte(T a, T b) {
    return a.compareTo(b) >= 0;
  }

  static <T extends Comparable<T>> boolean between(T a, T min, T max) {
    return gte(a, min) && lte(a, max);
  }
}
