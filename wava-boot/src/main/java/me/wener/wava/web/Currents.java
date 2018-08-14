package me.wener.wava.web;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;
import java.util.Optional;
import javax.annotation.Nonnull;
import me.wener.wava.error.Errors;
import me.wener.wava.util.Spring;
import org.springframework.web.context.request.RequestAttributes;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2017/4/20
 */
public interface Currents {

  @SuppressWarnings("unchecked")
  @Nonnull
  static <T> T require(Class<T> type) {
    T t = (T) Holder.getCurrentMap().get(type);
    if (t == null) {
      throw Errors.unauthorized().asException();
    }
    return t;
  }

  @SuppressWarnings("unchecked")
  static <T> Optional<T> resolve(Class<T> type) {
    return Optional.ofNullable((T) Holder.getCurrentMap().get(type));
  }

  @SuppressWarnings("unchecked")
  static void put(Object o) {
    put((Class<? super Object>) Spring.getClass(o), o);
  }

  static <T> void put(Class<? super T> type, T o) {
    Holder.getCurrentMap().put(type, o);
  }

  final class Holder {

    private static final String NAME = "@Current";

    @SuppressWarnings("unchecked")
    private static ClassToInstanceMap<Object> getCurrentMap() {
      RequestAttributes attributes = SpringWeb.currentRequestAttributes();
      ClassToInstanceMap<Object> map =
          (MutableClassToInstanceMap<Object>)
              attributes.getAttribute(NAME, RequestAttributes.SCOPE_REQUEST);
      if (map == null) {
        map = MutableClassToInstanceMap.create();
        attributes.setAttribute(NAME, map, RequestAttributes.SCOPE_REQUEST);
      }
      return map;
    }
  }
}
