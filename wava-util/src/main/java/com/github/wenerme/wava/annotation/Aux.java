package com.github.wenerme.wava.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Optional;

/**
 * 注解辅助操作接口
 *
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 16/8/30
 */
public interface Aux {

  static Optional<String> getDescribe(AnnotatedElement ele) {
    Describe describe = ele.getAnnotation(Describe.class);
    if (describe != null) {
      return Optional.of(describe.value());
    }
    return Optional.empty();
  }

  static Optional<String> getDescribe(Enum ele) {
    try {
      return getDescribe(ele.getDeclaringClass().getField(ele.name()));
    } catch (NoSuchFieldException e) {
      throw new AssertionError();
    }
  }

  static boolean isDeprecated(AnnotatedElement ele) {
    return ele.getAnnotation(Deprecated.class) != null;
  }

  static <T extends Annotation> Optional<T> getAnnotation(
      AnnotatedElement ele, Class<T> annotation) {
    return Optional.ofNullable(ele.getAnnotation(annotation));
  }
}
