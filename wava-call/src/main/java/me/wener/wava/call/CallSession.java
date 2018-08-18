package me.wener.wava.call;

import com.google.common.collect.Maps;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Session of one connection
 *
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2018/8/17
 */
@Getter
@Setter
@RequiredArgsConstructor
public class CallSession {
  private final String callerId;
  private final LocalDateTime createTime = LocalDateTime.now();
  private final Map<String, Object> attributes = Maps.newConcurrentMap();

  public CallContext createContext(CallRequest request) {
    return new CallContext(this, request.getCallId());
  }
}
