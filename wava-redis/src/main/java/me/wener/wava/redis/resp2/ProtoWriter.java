package me.wener.wava.redis.resp2;

import io.netty.buffer.ByteBuf;
import java.nio.charset.StandardCharsets;
import lombok.Setter;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2018/8/11
 */
public class ProtoWriter {

  @Setter private ByteBuf buf;

  public ByteBuf write(RedisPacket proto, ByteBuf buf) {
    this.buf = buf;
    return write(proto);
  }

  public void reset() {}

  public ByteBuf write(RedisPacket proto) {
    switch (proto.getType()) {
      case SIMPLE_STRING:
        buf.writeByte('+').writeCharSequence(proto.getString(), StandardCharsets.UTF_8);
        break;
      case ERROR:
        buf.writeByte('-').writeCharSequence(proto.getError(), StandardCharsets.UTF_8);
        break;
      case INTEGER:
        buf.writeByte(':')
            .writeCharSequence(String.valueOf(proto.getInteger()), StandardCharsets.US_ASCII);
        break;
      case BULK_STRING:
        buf.writeByte('$')
            .writeCharSequence(
                String.valueOf(proto.getString().length()), StandardCharsets.US_ASCII);
        writeMarker();
        buf.writeCharSequence(proto.getString(), StandardCharsets.UTF_8);
        break;
      case NULL_BULK_STRING:
        buf.writeCharSequence("$-1", StandardCharsets.US_ASCII);
        break;
      case ARRAY:
        buf.writeByte('*')
            .writeCharSequence(String.valueOf(proto.getArray().size()), StandardCharsets.US_ASCII);
        if (!proto.getArray().isEmpty()) {
          writeMarker();
          proto.getArray().forEach(this::write);
          // no more
          return buf;
        }
        break;
    }
    writeMarker();
    return buf;
  }

  public void writeMarker() {
    buf.writeByte(RedisProto.CR).writeByte(RedisProto.LF);
  }
}
