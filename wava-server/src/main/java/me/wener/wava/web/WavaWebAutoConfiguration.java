package me.wener.wava.web;

import me.wener.wava.web.filter.RequestLoggingFilter;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2018/6/13
 */
@Configuration
@Slf4j
@EnableConfigurationProperties(WavaWebProperties.class)
public class WavaWebAutoConfiguration {
  @PostConstruct
  void init() {
    log.info("WavaWebAutoConfiguration kicked in");
  }

  @ConditionalOnProperty(
      prefix = "wava.web",
      name = "request-logging",
      havingValue = "true",
      matchIfMissing = true)
  public RequestLoggingFilter requestLoggingFilter() {
    return new RequestLoggingFilter();
  }
}
