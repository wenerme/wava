package me.wener.wava.web.support;

import lombok.extern.slf4j.Slf4j;
import me.wener.wava.error.Errors;
import me.wener.wava.web.Current;
import me.wener.wava.web.Currents;
import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver;

/**
 * 实现 Web 请求上的 {@link Current} 参数注入
 *
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2017/4/20
 */
@Slf4j
public class CurrentMethodArgumentResolver extends AbstractNamedValueMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(Current.class);
  }

  @Override
  protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
    Current ann = parameter.getParameterAnnotation(Current.class);
    Assert.notNull(ann, "No @Current");
    return new NamedValueInfo(
        parameter.getParameterType().getCanonicalName(),
        ann.required(),
        ValueConstants.DEFAULT_NONE);
  }

  @Override
  protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) {
    return Currents.resolve(parameter.nestedIfOptional().getParameterType()).orElse(null);
  }

  @Override
  protected void handleMissingValue(String name, MethodParameter parameter) {
    // AuthenticationCredentialsNotFoundException
    log.error(
        "Required current {} not found for {}.{}",
        parameter.getParameterType().getSimpleName(),
        parameter.getMethod() == null
            ? "UNKNOWN"
            : parameter.getMethod().getDeclaringClass().getSimpleName(),
        parameter.getMethod() == null ? "UNKNOWN" : parameter.getMethod().getName());
    throw Errors.unauthorized().asException("缺少必要的授权信息");
  }
}
