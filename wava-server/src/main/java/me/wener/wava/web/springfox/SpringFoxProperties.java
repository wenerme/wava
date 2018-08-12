package me.wener.wava.web.springfox;

import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2017/3/13
 */
@Data
@Accessors(chain = true)
@ConfigurationProperties(prefix = "springfox")
public class SpringFoxProperties {

  /** 是否启用 */
  private boolean enabled;

  private String title;
  private String description;
  private String version;
  private String termsOfServiceUrl;
  private String contactName;
  private String contactUrl;
  private String contactEmail;
  private String license;
  private String licenseUrl;
  private String host;
  private String basePath = "/";
  /** 不为这部分接口生成文档, 参数为前缀形式,例如 /a 会匹配 /a/b/c */
  private List<String> exclude;
}
