package me.wener.wava.web.springfox;

import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import org.joor.Reflect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2017/3/23
 */
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 2000)
class CustomModelPropertyBuilderPlugin implements ModelPropertyBuilderPlugin {

  private static final Logger log = LoggerFactory.getLogger(CustomModelPropertyBuilderPlugin.class);

  @Override
  public void apply(ModelPropertyContext context) {
    try {
      BeanPropertyDefinition prop = context.getBeanPropertyDefinition().orNull();
      if (prop != null && prop.getField() != null) {
        ApiIgnoreProperty ignore = prop.getField().getAnnotation(ApiIgnoreProperty.class);
        if (ignore != null) {
          context.getBuilder().isHidden(true);
          return;
        }
        ApiBuilder.buildDescription(prop.getField().getAnnotated())
            .map(v -> Reflect.on(context.getBuilder()).get("description") + " " + v.toString())
            .ifPresent(v -> context.getBuilder().description(v));
      }
    } catch (Exception e) {
      log.warn("Handle property failed {}", context, e);
    }
  }

  @Override
  public boolean supports(DocumentationType delimiter) {
    return true;
  }
}
