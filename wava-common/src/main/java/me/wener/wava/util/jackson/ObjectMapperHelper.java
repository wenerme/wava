package me.wener.wava.util.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import java.io.IOException;
import java.util.Map;
import javax.annotation.Nullable;

/** Helper wrapper of {@link ObjectMapper} */
public interface ObjectMapperHelper {
  /** @return Shared writer, this is immutable */
  default ObjectWriter writer() {
    return mapper().writer();
  }

  /** @return Shared reader, this is immutable */
  default ObjectReader reader() {
    return mapper().reader();
  }

  ObjectMapper mapper();

  /** @return Check is this string represent a JSON object */
  default boolean isValidObject(String json) {
    if (json == null || json.length() < 2) {
      return false;
    }
    try {
      JsonNode node = mapper().readTree(json);
      return node.isObject();
    } catch (IOException e) {
      // ignored
    }
    return false;
  }

  /** @return null for invalid json */
  @Nullable
  default JsonNodeType getJsonType(String json) {
    if (json == null || json.length() == 0) {
      return null;
    }
    try {
      JsonNode node = mapper().readTree(json);
      return node.getNodeType();
    } catch (IOException e) {
      // ignored
    }
    return null;
  }
  /** Parse the JSON to map */
  default Map<String, Object> toMap(String json) throws IOException {
    return mapper().readValue(json, Holder.TYPE_REF_MAP_STRING_OBJECT);
  }

  /** Convert the source to map */
  default Map<String, Object> toMap(Object source) throws IOException {
    return mapper().convertValue(source, Holder.TYPE_REF_MAP_STRING_OBJECT);
  }

  /** convert the source object to given type */
  default <T> T toObject(Object source, Class<T> type) throws IOException {
    return mapper().convertValue(source, type);
  }

  /** Update the target object by given source */
  default <T> T update(Object source, T target) throws IOException {
    return mapper().updateValue(target, source);
  }

  /** Update the target object by given json */
  default <T> T update(String json, T target) throws IOException {
    return mapper().readerForUpdating(target).readValue(json);
  }

  /** Serialize object to string */
  default String stringify(Object o) {
    return stringify(o, false);
  }

  /** Serialize object to bytes */
  default byte[] bytify(Object o) {
    return bytify(o, false);
  }

  default byte[] bytify(Object o, boolean pretty) {
    try {
      if (pretty) {
        return mapper().writerWithDefaultPrettyPrinter().writeValueAsBytes(o);
      } else {
        return mapper().writeValueAsBytes(o);
      }
    } catch (JsonProcessingException e) {
      throw throwException(e);
    }
  }

  default String stringify(Object o, boolean pretty) {
    try {
      if (pretty) {
        return mapper().writerWithDefaultPrettyPrinter().writeValueAsString(o);
      } else {
        return mapper().writeValueAsString(o);
      }
    } catch (JsonProcessingException e) {
      throw throwException(e);
    }
  }
  /** Deserialize the json to type */
  default <T> T parse(String json, Class<T> type) {
    try {
      return mapper().readValue(json, type);
    } catch (IOException e) {
      throw throwException(e);
    }
  }
  /** Deserialize the bytes to type */
  default <T> T parse(byte[] json, Class<T> type) {
    try {
      return mapper().readValue(json, type);
    } catch (IOException e) {
      throw throwException(e);
    }
  }

  /** Wrap the jackson exception */
  default RuntimeException throwException(Exception e) {
    if (e instanceof RuntimeException) {
      throw (RuntimeException) e;
    }
    throw new RuntimeException(e);
  }

  final class Holder {
    private static final TypeReference<Map<String, Object>> TYPE_REF_MAP_STRING_OBJECT =
        new TypeReference<Map<String, Object>>() {};
  }
}
