package me.wener.wava.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import java.io.IOException;
import java.util.Map;
import javax.annotation.Nullable;

/**
 * JSON utils
 *
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 16/12/17
 */
public interface JSON {

  /** Create a helper */
  static JSON.Helper help(ObjectMapper mapper) {
    return () -> mapper;
  }

  /** @return Check is this string represent a JSON object */
  static boolean isValidObject(String json) {
    return Holder.HELPER.isValidObject(json);
  }

  /** @return null for invalid json */
  @Nullable
  static JsonNodeType getJsonType(String json) {
    return Holder.HELPER.getJsonType(json);
  }

  static Map<String, Object> toMap(String json) throws IOException {
    return Holder.HELPER.toMap(json);
  }

  static <T> T toObject(Object source, Class<T> type) throws IOException {
    return Holder.HELPER.toObject(source, type);
  }

  static <T> T update(Object source, T target) throws IOException {
    return Holder.HELPER.update(source, target);
  }

  static <T> T update(String json, T target) throws IOException {
    return Holder.HELPER.update(json, target);
  }

  static String stringify(Object o) {
    return Holder.HELPER.stringify(o, false);
  }

  static byte[] bytify(Object o) {
    return Holder.HELPER.bytify(o, false);
  }

  static byte[] bytify(Object o, boolean pretty) {
    try {
      if (pretty) {
        return Holder.PRETTY_WRITER.writeValueAsBytes(o);
      } else {
        return Holder.MAPPER.writeValueAsBytes(o);
      }
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  static String stringify(Object o, boolean pretty) {
    try {
      if (pretty) {
        return Holder.PRETTY_WRITER.writeValueAsString(o);
      } else {
        return Holder.MAPPER.writeValueAsString(o);
      }
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  static <T> T parse(String json, Class<T> type) {
    return Holder.HELPER.parse(json, type);
  }

  static <T> T parse(byte[] json, Class<T> type) {
    return Holder.HELPER.parse(json, type);
  }

  /** @return Shared writer, this is immutable */
  static ObjectWriter writer() {
    return Holder.HELPER.writer();
  }

  /** @return Shared reader, this is immutable */
  static ObjectReader reader() {
    return Holder.HELPER.reader();
  }

  /** Helper wrapper of {@link ObjectMapper} */
  interface Helper {
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
  }

  final class Holder {

    private static final ObjectMapper MAPPER =
        new ObjectMapper()
            .findAndRegisterModules()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private static final JSON.Helper HELPER = help(MAPPER);
    private static final ObjectWriter PRETTY_WRITER = MAPPER.writerWithDefaultPrettyPrinter();
    private static final TypeReference<Map<String, Object>> TYPE_REF_MAP_STRING_OBJECT =
        new TypeReference<Map<String, Object>>() {};
  }

  /**
   * Deserialize the node to a string field, reverse of the {@link
   * com.fasterxml.jackson.annotation.JsonRawValue} operation. Usage {@code @JsonDeserialize(using =
   * JSON.JsonRawValueDeserializer.class)}
   */
  class JsonRawValueDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
      TreeNode tree = jp.getCodec().readTree(jp);
      return tree.toString();
    }
  }
}
