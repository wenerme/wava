package me.wener.wava.redis.resp2;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.netty.buffer.ByteBuf;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2018/8/11
 */
@Data
@Accessors(chain = true)
@ToString(exclude = "parent")
@EqualsAndHashCode(exclude = {"parent", "size"})
@JsonInclude(Include.NON_DEFAULT)
public class RedisPacket {

  @JsonIgnore private RedisPacket parent;

  private PacketType type;
  @JsonIgnore private Integer size; // FIXME ? optimize

  private CharSequence string;
  private byte[] bytes;
  private ByteBuf bulk;

  private Integer integer;
  private CharSequence error;
  private List<RedisPacket> array;

  public String asString() {
    if (string == null) {
      return null;
    }
    return string.toString();
  }
}
