package me.wener.wava.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

/**
 * Standard immutable error info
 *
 * @author wener
 * @since 16/4/8
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Value
@AllArgsConstructor
@Builder
public class Error implements Serializable, ErrorDetail {

  @Wither private final int code;
  @Wither private final int status;
  @Wither private final String message;
  @Wither private final Object data;
  @Wither private final String path;

  /** 0 for uninitialized */
  @Wither
  @JsonInclude(JsonInclude.Include.NON_DEFAULT)
  private final long timestamp;
}
