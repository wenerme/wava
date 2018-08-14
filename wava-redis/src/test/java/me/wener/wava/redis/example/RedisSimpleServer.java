package me.wener.wava.redis.example;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import me.wener.wava.redis.proto.RedisPacketCodec;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2018/8/13
 */
public class RedisSimpleServer {

  public static void main(String[] args) throws InterruptedException {
    runSimpleServer();
  }

  private static void runSimpleServer() throws InterruptedException {
    // EventLoopGroup bossGroup = new KQueueEventLoopGroup();
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    try {
      ServerBootstrap b = new ServerBootstrap();
      b.group(bossGroup, workerGroup)
          .channel(NioServerSocketChannel.class)
          // .channel(KQueueServerSocketChannel.class)
          .childHandler(
              new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                  ch.pipeline().addLast(new RedisPacketCodec(), new SimpleHandler());
                }
              })
          .option(ChannelOption.SO_BACKLOG, 128)
          .childOption(ChannelOption.SO_KEEPALIVE, true);

      ChannelFuture f = b.bind(7890).sync();
      f.channel().closeFuture().sync();
    } finally {
      workerGroup.shutdownGracefully();
      bossGroup.shutdownGracefully();
    }
  }
}
