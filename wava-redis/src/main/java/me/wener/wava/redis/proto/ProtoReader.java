package me.wener.wava.redis.proto;

import io.netty.buffer.ByteBuf;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import lombok.Setter;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2018/8/11
 */
public class ProtoReader {

  private @Setter ByteBuf buf;
  private RedisPacket current;
  private int len;
  /** expected size of array or bulk */
  private int size;

  public RedisPacket read(ByteBuf buf) {
    this.buf = buf;
    return read();
  }

  public void reset() {
    buf = null;
    current = null;
    len = 0;
    size = 0;
  }

  public RedisPacket read() {
    RedisPacket pack;
    do {
      // bulk string is not line based
      if (current != null && current.getType() == PacketType.BULK_STRING) {
        if (buf.readableBytes() < current.getSize() + RedisProto.MARKER_LENGTH) {
          // not enough
          return null;
        }
        // Read bulk to string - config-able
        current.setString(readCharSequence(current.getSize()));
        readMark();
        pack = pop();
      } else {
        len = buf.bytesBefore(RedisProto.CR);
        if (len < 0) {
          return null;
        }
        pack = readPack();
      }

    } while (pack == null);
    return pack;
  }

  private RedisPacket pop() {
    switch (current.getType()) {
      case BULK_STRING:
        if (current.getString() == null
            && current.getBulk() == null
            && current.getBytes() == null) {
          // incomplete
          return null;
        }
        break;
      case ARRAY:
        if (current.getArray().size() != current.getSize()) {
          // incomplete
          return null;
        }
        break;
    }
    // popup
    RedisPacket ret = current;
    current = ret.getParent();
    if (current != null) {
      return pop();
    }
    return ret;
  }

  private RedisPacket readPack() {

    len--;
    switch (buf.readByte()) {
      case '+':
        current = newPack().setType(PacketType.SIMPLE_STRING).setString(readCharSequence());
        break;
      case '-':
        current = newPack().setType(PacketType.ERROR).setError(readCharSequence());
        break;
      case ':':
        current = newPack().setType(PacketType.INTEGER).setInteger(readInt());
        break;
      case '$':
        {
          int i = readInt();
          if (i < 0) {
            current = newPack().setType(PacketType.NULL_BULK_STRING);
          } else {
            current = newPack().setType(PacketType.BULK_STRING).setSize(i);
          }
        }
        break;
      case '*':
        {
          int i = readInt();
          if (i == 0) {
            current =
                newPack().setType(PacketType.ARRAY).setArray(Collections.emptyList()).setSize(0);
          } else {
            current = newPack().setType(PacketType.ARRAY).setArray(new ArrayList<>(i)).setSize(i);
          }
        }
        break;
      default:
        buf.skipBytes(len);
    }
    readMark();

    return pop();
  }

  private RedisPacket newPack() {
    RedisPacket pack = new RedisPacket().setParent(current);
    if (current != null && current.getType() == PacketType.ARRAY) {
      current.getArray().add(pack);
    }
    return pack;
  }

  private int readInt() {
    return Integer.parseInt(readCharSequence().toString());
  }

  private CharSequence readCharSequence() {
    return readCharSequence(len);
  }

  private CharSequence readCharSequence(int len) {
    return buf.readCharSequence(len, StandardCharsets.UTF_8);
  }

  private void readMark() {
    byte cr = buf.readByte();
    byte lf = buf.readByte();
    if (cr == RedisProto.CR && lf == RedisProto.LF) {
      return;
    }
    throw new RuntimeException(String.format("Unexpected mark %x %x", cr, lf));
  }
}
