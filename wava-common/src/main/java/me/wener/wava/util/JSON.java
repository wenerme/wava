package me.wener.wava.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
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
    return helper().isValidObject(json);
  }

  /** @return null for invalid json */
  @Nullable
  static JsonNodeType getJsonType(String json) {
    return helper().getJsonType(json);
  }

  /** @see ObjectMapperHelper#toMap(String) */
  static Map<String, Object> toMap(String json) {
    return helper().toMap(json);
  }

  /** @see ObjectMapperHelper#toMap(Object) */
  static Map<String, Object> toMap(Object source) {
    return helper().toMap(source);
  }

  /** @see ObjectMapperHelper#toObject(Object,Class) */
  static <T> T toObject(Object source, Class<T> type) {
    return helper().toObject(source, type);
  }

  /** @see ObjectMapperHelper#update(Object,T) */
  static <T> T update(Object source, T target) {
    return helper().update(source, target);
  }

  /** @see ObjectMapperHelper#update(String,T) */
  static <T> T update(String json, T target) {
    return helper().update(json, target);
  }

  /** @see ObjectMapperHelper#stringify(Object) */
  static String stringify(Object o) {
    return helper().stringify(o, false);
  }

  /** @see ObjectMapperHelper#bytify(Object) */
  static byte[] bytify(Object o) {
    return helper().bytify(o, false);
  }

  /** @see ObjectMapperHelper#bytify(Object,boolean) */
  static byte[] bytify(Object o, boolean pretty) {
    return helper().bytify(o, pretty);
  }

  /** @see ObjectMapperHelper#stringify(Object,boolean) */
  static String stringify(Object o, boolean pretty) {
    return helper().stringify(o, pretty);
  }

  /** @see ObjectMapperHelper#parse(String, Class) */
  static <T> T parse(String json, Class<T> type) {
    return helper().parse(json, type);
  }

  /** @see ObjectMapperHelper#parse(byte[], Class) */
  static <T> T parse(byte[] json, Class<T> type) {
    return helper().parse(json, type);
  }

  /** Set to the new mapper */
  static ObjectMapper use(ObjectMapper mapper) {
    return Holder.HELPER.getAndSet(help(mapper)).mapper();
  }

  static ObjectMapperHelper use(ObjectMapperHelper helper) {
    return Holder.HELPER.getAndSet(helper);
  }

  static ObjectMapper mapper() {
    return helper().mapper();
  }

  static ObjectMapperHelper helper() {
    return Holder.HELPER.get();
  }

  static TypeReference<Map<String, Object>> getTypeReferenceOfMapStringObject() {
    return Holder.TYPE_REF_MAP_STRING_OBJECT;
  }

  static TypeReference<Map<String, String>> getTypeReferenceOfMapStringString() {
    return Holder.TYPE_REF_MAP_STRING_STRING;
  }

  final class Holder {
    private static final AtomicReference<ObjectMapperHelper> HELPER =
        new AtomicReference<>(help(build()));
    private static final TypeReference<Map<String, Object>> TYPE_REF_MAP_STRING_OBJECT =
        new TypeReference<Map<String, Object>>() {};
    private static final TypeReference<Map<String, String>> TYPE_REF_MAP_STRING_STRING =
        new TypeReference<Map<String, String>>() {};

    private static ObjectMapper build() {
      return new ObjectMapper()
          .findAndRegisterModules()
          .setSerializationInclusion(JsonInclude.Include.NON_NULL)
          .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
  }
}
