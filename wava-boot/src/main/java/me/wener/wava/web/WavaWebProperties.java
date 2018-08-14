package me.wener.wava.web;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2018/7/30
 */
@Data
@Accessors(chain = true)
@ConfigurationProperties(prefix = "wava.web")
public class WavaWebProperties {

  boolean requestLogging = true;
}
