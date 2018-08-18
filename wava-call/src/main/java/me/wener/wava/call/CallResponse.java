package me.wener.wava.call;

import com.google.common.collect.Maps;
import java.util.Map;
import lombok.Data;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2018/8/16
 */
@Data
public class CallResponse {
  private Map<String, String> metadata = Maps.newHashMap();
  private Long responseTime;

  private Long callId;
  private Object response;
  private Boolean success;
  private int code;
  private int status;
  private String message;
}
