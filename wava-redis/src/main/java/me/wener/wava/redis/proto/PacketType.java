package me.wener.wava.redis.proto;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2018/8/11
 */
public enum PacketType {
  // UNKNOWN,
  SIMPLE_STRING,
  ERROR,
  INTEGER,
  BULK_STRING,
  NULL_BULK_STRING,
  ARRAY,
}
