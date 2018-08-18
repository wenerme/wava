package me.wener.wava.call;

import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2018/8/16
 */
@Data
public class CallRequest {
  private String callerId;
  private Long callId;
  private Map<String, String> metadata;

  private Long type;
  private String service;
  private String method;
  private List<Object> parameters;
  private Long requestTime;

  // CALL
  // CLIENT_STREAM
  // SERVER_STREAM
  // BI_STREAM
}
