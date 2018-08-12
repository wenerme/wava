package me.wener.wava.redis.example;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import me.wener.wava.redis.proto.PacketType;
import me.wener.wava.redis.proto.ProtoReader;
import me.wener.wava.redis.proto.ProtoWriter;
import me.wener.wava.redis.proto.RedisPacket;
import me.wener.wava.redis.proto.RedisProtos;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2018/8/13
 */
public class RedisProtoHandler extends ChannelInboundHandlerAdapter { // (1)

  private static final RedisPacket ok = RedisProtos.ok();
  private static final RedisPacket res = RedisProtos.bulkString("1");
  private ProtoReader reader;
  private ProtoWriter writer;
  private Map<String, Object> store = new HashMap<>();

  @Override
  public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    reader = new ProtoReader();
    writer = new ProtoWriter();
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException { // (2)
    reader.setBuf((ByteBuf) msg);
    RedisPacket pack = reader.read();
    if (pack == null) {
      return;
    }
    String command = pack.getArray().get(0).getString().toString();
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
            pack.getArray().get(1).getString().toString(),
            pack.getArray().get(2).getString().toString());
        response = ok;
        break;
      case "GET":
        {
          Object o = store.get(pack.getArray().get(1).getString().toString());
          if (o instanceof CharSequence) {
            response =
                new RedisPacket().setType(PacketType.BULK_STRING).setString((CharSequence) o);
          } else {
            response = RedisProtos.wrongType();
          }
        }
        break;
      default:
        response = RedisProtos.error("unknown command '%s'", command);
        break;
    }

    ctx.writeAndFlush(writer.write(response, ctx.alloc().buffer()), ctx.voidPromise());
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
    cause.printStackTrace();
    ctx.close();
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    ctx.flush();
  }
}