package me.wener.wava.call;

import com.google.common.collect.Maps;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.Data;

/**
 * Context of one call
 *
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2018/8/17
 */
@Data
public class CallContext {
  private final CallSession session;
  private final Long callId;
  private final LocalDateTime createTime = LocalDateTime.now();
  private final Map<String, Object> attributes = Maps.newHashMap();
}
