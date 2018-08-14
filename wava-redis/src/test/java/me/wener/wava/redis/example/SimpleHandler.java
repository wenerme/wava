package me.wener.wava.redis.example;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import me.wener.wava.redis.proto.PacketType;
import me.wener.wava.redis.proto.RedisPacket;
import me.wener.wava.redis.proto.RedisProtos;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2018/8/14
 */
@Slf4j
public class SimpleHandler extends ChannelInboundHandlerAdapter {
  private static final RedisPacket ok = RedisProtos.ok();
  private Map<String, Object> store = new HashMap<>();

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    if (!(msg instanceof RedisPacket)) {
      ctx.fireChannelRead(msg);
      return;
    }
    RedisPacket packet = (RedisPacket) msg;
    String command = packet.getArray().get(0).getString().toString();
    String upperCommand = command.toUpperCase();

    RedisPacket response;
    switch (upperCommand) {
      case "COMMAND":
        response =
            RedisProtos.array(
                RedisProtos.command("SET", 2, 1, 1, 1, "write"),
                RedisProtos.command("GET", 2, 1, 1, 1, "readonly"));
        break;
      case "SET":
        store.put(
            packet.getArray().get(1).getString().toString(),
            packet.getArray().get(2).getString().toString());
        response = ok;
        break;
      case "GET":
        {
          Object o = store.get(packet.getArray().get(1).getString().toString());
          if (o == null) {
            response = RedisProtos.nil();
          } else if (o instanceof CharSequence) {
            response =
                new RedisPacket().setType(PacketType.BULK_STRING).setString((CharSequence) o);
          } else {
            response = RedisProtos.wrongType();
          }
        }
        break;
      case "HSET":
        {
          Object o =
              store.computeIfAbsent(packet.getArray().get(1).asString(), (k) -> new HashMap<>());
          if (o instanceof Map) {
            String old =
                ((Map<String, String>) o)
                    .put(packet.getArray().get(2).asString(), packet.getArray().get(3).asString());
            if (old == null) {
              response = RedisProtos.integer(1);
            } else {
              response = RedisProtos.integer(0);
            }
          } else {
            response = RedisProtos.wrongType();
          }
        }
        break;
      case "HGET":
        {
          Object o = store.get(packet.getArray().get(1).asString());
          if (o == null) {
            response = RedisProtos.nil();
          } else if (o instanceof Map) {
            response = RedisProtos.bulkString((String) ((Map) o).get(packet.getArray().get(2)));
          } else {
            response = RedisProtos.wrongType();
          }
        }
        break;
      case "PING":
        response = RedisProtos.simpleString("PONG");
        break;
      default:
        response = RedisProtos.err("unknown command '%s'", command);
        log.info("UNKNOWN {}", command);
        break;
    }

    ctx.writeAndFlush(response, ctx.voidPromise());
  }
}
