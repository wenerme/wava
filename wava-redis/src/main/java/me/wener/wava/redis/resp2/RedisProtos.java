package me.wener.wava.redis.resp2;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2018/8/11
 */
public interface RedisProtos {

  static RedisPacket ok() {
    return new RedisPacket().setType(PacketType.SIMPLE_STRING).setString("OK");
  }

  static RedisPacket bulkString(String s) {
    return new RedisPacket().setType(PacketType.BULK_STRING).setString(s);
  }

  static RedisPacket simpleString(String s) {
    return new RedisPacket().setType(PacketType.SIMPLE_STRING).setString(s);
  }

  static RedisPacket integer(int v) {
    return new RedisPacket().setType(PacketType.INTEGER).setInteger(v);
  }

  static RedisPacket arrayOfSimpleString(String... v) {
    ArrayList<RedisPacket> array = new ArrayList<>(v.length);
    for (String s : v) {
      array.add(simpleString(s));
    }
    return new RedisPacket().setType(PacketType.ARRAY).setArray(array);
  }

  static RedisPacket array(RedisPacket... packets) {
    return new RedisPacket().setType(PacketType.ARRAY).setArray(Arrays.asList(packets));
  }

  static RedisPacket wrongType() {
    return new RedisPacket()
        .setType(PacketType.ERROR)
        .setError("WRONGTYPE Operation against a key holding the wrong kind of value");
  }

  static RedisPacket wrongArgument(String command) {
    return err("wrong number of arguments for '%s' command", command);
  }

  static RedisPacket err(String format, Object... args) {
    return error("ERR", format, args);
  }

  static RedisPacket error(String type, String format, Object... args) {
    return new RedisPacket()
        .setType(PacketType.ERROR)
        .setError(String.format(type + " " + format, args));
  }

  static RedisPacket command(
      String name,
      int arity,
      int firstKeyPosition,
      int lastKeyPosition,
      int repeatKeyStep,
      String... flags) {
    return new RedisPacket()
        .setType(PacketType.ARRAY)
        .setArray(
            Arrays.asList(
                bulkString(name),
                integer(arity),
                arrayOfSimpleString(flags),
                integer(firstKeyPosition),
                integer(lastKeyPosition),
                integer(repeatKeyStep)));
  }

  static RedisPacket nil() {
    return new RedisPacket().setType(PacketType.NULL_BULK_STRING);
  }
}
