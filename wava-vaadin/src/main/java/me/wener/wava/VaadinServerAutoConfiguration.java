package me.wener.wava;

import com.vaadin.flow.component.textfield.TextField;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2018/6/12
 */
@Configuration
@ConditionalOnClass(TextField.class)
@Slf4j
public class VaadinServerAutoConfiguration {
  @PostConstruct
  void init() {
    log.info("Configuring Vaadin Server");
  }
}
