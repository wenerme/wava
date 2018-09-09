package me.wener.wava.redis.resp2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import java.util.List;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2018/8/14
 */
public class RedisPacketCodec extends ByteToMessageCodec<RedisPacket> {

  private ProtoReader reader = new ProtoReader();
  private ProtoWriter writer = new ProtoWriter();

  public RedisPacketCodec() {
    super(true);
  }

  @Override
  protected void encode(ChannelHandlerContext ctx, RedisPacket msg, ByteBuf out) throws Exception {
    writer.write(msg, out);
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    while (true) {
      RedisPacket packet = reader.read(in);
      if (packet == null) {
        return;
      }
      out.add(packet);
    }
  }

  @Override
  public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
    super.handlerRemoved(ctx);
    reader.reset();
    writer.reset();
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
    ctx.close();
  }
}
