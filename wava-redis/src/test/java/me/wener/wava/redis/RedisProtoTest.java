package me.wener.wava.redis;

import static org.junit.Assert.assertEquals;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import me.wener.wava.redis.resp2.PacketType;
import me.wener.wava.redis.resp2.ProtoReader;
import me.wener.wava.redis.resp2.ProtoWriter;
import me.wener.wava.redis.resp2.RedisPacket;
import org.junit.Test;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2018/8/13
 */
public class RedisProtoTest {

  @Test
  public void testProtos() {
    LinkedHashMap<String, RedisPacket> tests = new LinkedHashMap<>();
    tests.put("+OK\r\n", new RedisPacket().setType(PacketType.SIMPLE_STRING).setString("OK"));
    tests.put(
        "-Error message\r\n",
        new RedisPacket().setType(PacketType.ERROR).setError("Error message"));
    tests.put(":1\r\n", new RedisPacket().setType(PacketType.INTEGER).setInteger(1));
    tests.put(
        "$6\r\nfoobar\r\n", new RedisPacket().setType(PacketType.BULK_STRING).setString("foobar"));
    tests.put("$0\r\n\r\n", new RedisPacket().setType(PacketType.BULK_STRING).setString(""));
    tests.put("$-1\r\n", new RedisPacket().setType(PacketType.NULL_BULK_STRING));
    tests.put(
        "*0\r\n", new RedisPacket().setType(PacketType.ARRAY).setArray(Collections.emptyList()));
    tests.put(
        "*2\r\n$3\r\nfoo\r\n$3\r\nbar\r\n",
        new RedisPacket()
            .setType(PacketType.ARRAY)
            .setArray(
                Arrays.asList(
                    new RedisPacket().setType(PacketType.BULK_STRING).setString("foo"),
                    new RedisPacket().setType(PacketType.BULK_STRING).setString("bar"))));
    tests.put(
        "*2\r\n*3\r\n:1\r\n:2\r\n:3\r\n*2\r\n+Foo\r\n-Bar\r\n",
        new RedisPacket()
            .setType(PacketType.ARRAY)
            .setArray(
                Arrays.asList(
                    new RedisPacket()
                        .setType(PacketType.ARRAY)
                        .setArray(
                            Arrays.asList(
                                new RedisPacket().setType(PacketType.INTEGER).setInteger(1),
                                new RedisPacket().setType(PacketType.INTEGER).setInteger(2),
                                new RedisPacket().setType(PacketType.INTEGER).setInteger(3))),
                    new RedisPacket()
                        .setType(PacketType.ARRAY)
                        .setArray(
                            Arrays.asList(
                                new RedisPacket()
                                    .setType(PacketType.SIMPLE_STRING)
                                    .setString("Foo"),
                                new RedisPacket().setType(PacketType.ERROR).setError("Bar"))))));

    PooledByteBufAllocator allocator = PooledByteBufAllocator.DEFAULT;
    ByteBuf buf = allocator.buffer();
    ProtoReader reader = new ProtoReader();
    reader.setBuf(buf);
    ByteBuf wbuf = allocator.buffer();
    ProtoWriter writer = new ProtoWriter();
    writer.setBuf(wbuf);

    for (Entry<String, RedisPacket> entry : tests.entrySet()) {
      String expected = entry.getKey();
      RedisPacket expectedProto = entry.getValue();

      System.out.printf("TEST: %s\n", expected.replace("\r\n", "\\r\\n"));

      buf.writeCharSequence(expected, StandardCharsets.UTF_8);

      RedisPacket proto = reader.read();
      assertEquals(expectedProto, proto);

      writer.write(expectedProto);
      assertEquals(expected, wbuf.readCharSequence(expected.length(), StandardCharsets.UTF_8));
    }
  }
}
