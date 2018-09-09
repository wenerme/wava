package me.wener.wava.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.Nullable;
import me.wener.wava.util.jackson.ObjectMapperHelper;

/**
 * JSON utils for Jackson
 *
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 16/12/17
 */
public interface JSON {

  /** Create a helper */
  static ObjectMapperHelper help(ObjectMapper mapper) {
    return () -> mapper;
  }

  /** @return Check is this string represent a JSON object */
  static boolean isValidObject(String json) {
    return Holder.helper().isValidObject(json);
  }

  /** @return null for invalid json */
  @Nullable
  static JsonNodeType getJsonType(String json) {
    return Holder.helper().getJsonType(json);
  }

  /** @see ObjectMapperHelper#toMap(String) */
  static Map<String, Object> toMap(String json) throws IOException {
    return Holder.helper().toMap(json);
  }

  /** @see ObjectMapperHelper#toMap(Object) */
  static Map<String, Object> toMap(Object source) throws IOException {
    return Holder.helper().toMap(source);
  }

  /** @see ObjectMapperHelper#toObject(Object,Class) */
  static <T> T toObject(Object source, Class<T> type) throws IOException {
    return Holder.helper().toObject(source, type);
  }

  /** @see ObjectMapperHelper#update(Object,T) */
  static <T> T update(Object source, T target) throws IOException {
    return Holder.helper().update(source, target);
  }

  /** @see ObjectMapperHelper#update(String,T) */
  static <T> T update(String json, T target) throws IOException {
    return Holder.helper().update(json, target);
  }

  /** @see ObjectMapperHelper#stringify(Object) */
  static String stringify(Object o) {
    return Holder.helper().stringify(o, false);
  }

  /** @see ObjectMapperHelper#bytify(Object) */
  static byte[] bytify(Object o) {
    return Holder.helper().bytify(o, false);
  }

  /** @see ObjectMapperHelper#bytify(Object,boolean) */
  static byte[] bytify(Object o, boolean pretty) {
    return Holder.helper().bytify(o, pretty);
  }

  /** @see ObjectMapperHelper#stringify(Object,boolean) */
  static String stringify(Object o, boolean pretty) {
    return Holder.helper().stringify(o, pretty);
  }

  /** @see ObjectMapperHelper#parse(String, Class) */
  static <T> T parse(String json, Class<T> type) {
    return Holder.helper().parse(json, type);
  }

  /** @see ObjectMapperHelper#parse(byte[], Class) */
  static <T> T parse(byte[] json, Class<T> type) {
    return Holder.helper().parse(json, type);
  }

  /** @see ObjectMapperHelper#writer() */
  static ObjectWriter writer() {
    return Holder.helper().writer();
  }

  /** @see ObjectMapperHelper#reader() () */
  static ObjectReader reader() {
    return Holder.helper().reader();
  }

  /** Set to the new mapper */
  static ObjectMapper use(ObjectMapper mapper) {
    return Holder.MAPPER_REF.getAndSet(mapper);
  }

  static ObjectMapper mapper() {
    return Holder.helper().mapper();
  }

  final class Holder {
    private static final AtomicReference<ObjectMapper> MAPPER_REF =
        new AtomicReference<>(
            new ObjectMapper()
                .findAndRegisterModules()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false));

    private static final ObjectMapperHelper HELPER = MAPPER_REF::get;

    private static ObjectMapperHelper helper() {
      return HELPER;
    }
  }
}
