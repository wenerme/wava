package me.wener.wava.web.springfox;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.joor.Reflect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ReflectionUtils;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;

/** 移除不要的参数 */
@Order(Ordered.LOWEST_PRECEDENCE)
@RequiredArgsConstructor
class CustomOperationPlugin implements OperationBuilderPlugin {

  private final Set<Class<?>> ignoredAnnotation;

  @Override
  public void apply(OperationContext context) {
    // 由于 ParameterBuilderPlugin 处理不到这些参数,所以在这里统一处理
    List<Parameter> parameters = Reflect.on(context.operationBuilder()).get("parameters");
    for (ResolvedMethodParameter p : context.getParameters()) {
      Class<?> type = p.getParameterType().getErasedType();

      // 为 Pageable 添加参数
      if (p.getParameterType().isInstanceOf(Pageable.class)) {
        parameters.addAll(SpringFoxes.pageableParameters());

        // 将对象构建为 query 参数后, 移除原有的参数
        parameters.removeIf(
            v -> v.getType().transform(a -> Objects.equals(a.getErasedType(), type)).or(false));
        continue;
      }
      List<Annotation> annotations = Reflect.on(p).get("annotations");
      // 允许将 ApiIgnore 作为元注解来使用
      {
        boolean removed = false;
        for (Annotation anno : annotations) {
          if (ignoredAnnotation.contains(anno.getClass())) {
            removed = true;
            break;
          }
          if (anno instanceof ApiIgnore
              || AnnotationUtils.findAnnotation(anno.getClass(), ApiIgnore.class) != null) {
            removed = true;
            ignoredAnnotation.add(anno.getClass());
            break;
          }
        }
        if (removed) {
          // 由于定位不到实际参数对应的哪个参数,所以只能暂时先使用类型来判断
          parameters.removeIf(
              v -> v.getType().transform(a -> Objects.equals(a.getErasedType(), type)).or(false));
          continue;
        }
      }
      // 实现 ApiQueryParam 的处理
      {
        ApiQueryParam anno = p.findAnnotation(ApiQueryParam.class).orNull();
        if (anno == null) {
          anno = AnnotationUtils.findAnnotation(type, ApiQueryParam.class);
        }
        if (anno != null) {
          ParameterBuilder builder = new ParameterBuilder();
          builder.parameterType("query");
          ReflectionUtils.doWithFields(
              type,
              f -> {
                SpringFoxBuilder.buildParameter(f, builder).ifPresent(parameters::add);
              });
          // 将对象构建为 query 参数后, 移除原有的参数
          parameters.removeIf(
              v -> v.getType().transform(a -> Objects.equals(a.getErasedType(), type)).or(false));
        }
      }
    }

    //            log.debug("{} {} - {}", context.httpMethod(), context.requestMappingPattern(),
    // parameters
    //                .stream()
    //                .map(v -> String.format("%s %s(%s)", v.getName(), v.getParamType(),
    // v.getType().orNull()))
    //                .collect(Collectors.joining(","))
    //            );
  }

  @Override
  public boolean supports(DocumentationType delimiter) {
    return true;
  }
}
