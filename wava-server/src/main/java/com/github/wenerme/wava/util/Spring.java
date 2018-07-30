package com.github.wenerme.wava.util;

import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import org.springframework.aop.support.AopUtils;
import org.springframework.format.datetime.standard.DateTimeContext;
import org.springframework.format.datetime.standard.DateTimeContextHolder;

/**
 * Spring 相关的静态辅助方法, 避免自己找一些上下文的静态方法和静态类
 *
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 16/7/26
 */
@SuppressWarnings("unused")
public interface Spring {
  /*
  查找 spring 中的 ContextHolder
  org.springframework.* ContextHolder
   */

  /** 获取一个对象的真实类, 因为可能该对象为代理后的对象, {@code getClass} 返回的是生成的类 */
  static Class<?> getClass(Object o) {
    // Detect hibernate proxy
    if (o instanceof HibernateProxy) {
      LazyInitializer lazyInitializer = ((HibernateProxy) o).getHibernateLazyInitializer();
      return lazyInitializer.getPersistentClass();
    }
    return AopUtils.getTargetClass(o);
  }

  static DateTimeContext getDateTimeContext() {
    return DateTimeContextHolder.getDateTimeContext();
  }
}
