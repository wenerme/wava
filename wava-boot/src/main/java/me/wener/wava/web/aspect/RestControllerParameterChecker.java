package me.wener.wava.web.aspect;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Consumer;
import org.aspectj.lang.JoinPoint;

/**
 * 用于对 rest 接口参数进行检测的检测器, 只需要注入相应的 {@link Factory} 即可.
 *
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2017/4/20
 */
public interface RestControllerParameterChecker extends Consumer<JoinPoint> {

  /**
   * 用于创建 checker 的工厂类, 支持使用 {@link org.springframework.core.annotation.Order} 或 {@link
   * org.springframework.core.Ordered} 来指定顺序
   */
  interface Factory {
    Optional<RestControllerParameterChecker> create(Method method);
  }
}
