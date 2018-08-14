package me.wener.wava.web.springfox;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import me.wener.wava.util.Chars;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/** Springfox auto configuration */
@Configuration
@EnableSwagger2
@ConditionalOnClass(Docket.class)
@ConditionalOnProperty(prefix = "springfox", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(SpringFoxProperties.class)
@Slf4j
public class SpringFoxAutoConfiguration {

  // region plugins
  @Bean
  public CustomModelPropertyBuilderPlugin customModelPropertyBuilderPlugin() {
    return new CustomModelPropertyBuilderPlugin();
  }

  @Bean
  public CustomOperationPlugin customOperationPlugin() {
    return new CustomOperationPlugin();
  }

  @Bean
  public CustomTypeNameProviderPlugin customTypeNameProviderPlugin() {
    return new CustomTypeNameProviderPlugin();
  }
  // endregion plugins

  @Bean
  public Docket swaggerSpringfoxDocket(SpringFoxProperties swagger) {
    log.debug(
        "Initializing SpringFox {}/{}",
        Strings.nullToEmpty(swagger.getHost()),
        Objects.equals(swagger.getBasePath(), "/") ? "" : swagger.getBasePath());

    StopWatch watch = new StopWatch();
    watch.start();

    Contact contact =
        new Contact(swagger.getContactName(), swagger.getContactUrl(), swagger.getContactEmail());

    ApiInfo apiInfo =
        new ApiInfo(
            swagger.getTitle(),
            swagger.getDescription(),
            swagger.getVersion(),
            swagger.getTermsOfServiceUrl(),
            contact.getName(),
            swagger.getLicense(),
            swagger.getLicenseUrl());

    Docket docket =
        new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo)
            // Security
            .securitySchemes(
                ImmutableList.of(new ApiKey("Authorization", "Authorization", "header")))
            .securityContexts(
                ImmutableList.of(
                    SecurityContext.builder()
                        // FIXME springfox better configuration
                        .forPaths(v -> Chars.startWithAny(v, "/user/login", "/user/logout") < 0)
                        .securityReferences(
                            ImmutableList.of(
                                SecurityReference.builder()
                                    .reference("Authorization")
                                    .scopes(new AuthorizationScope[] {})
                                    .build()))
                        .build()))
            // Basic
            .host(swagger.getHost())
            .pathMapping(swagger.getBasePath())
            .forCodeGeneration(true)
            // Type process
            .genericModelSubstitutes(ResponseEntity.class)
            .genericModelSubstitutes(DeferredResult.class)
            .ignoredParameterTypes(java.sql.Date.class)
            .directModelSubstitute(LocalDate.class, java.sql.Date.class)
            .directModelSubstitute(ZonedDateTime.class, Date.class)
            .directModelSubstitute(LocalDateTime.class, Date.class)
            .select()
            .paths(v -> v != null && Chars.startWithAny(v, swagger.getExclude()) < 0)
            .build();

    watch.stop();
    log.debug("Started Swagger in {} ms", watch.getTotalTimeMillis());
    return docket;
  }
}
