package me.wener.wava.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import me.wener.wava.util.JSON;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.mapstruct.Qualifier;
import org.mapstruct.TargetType;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2018/5/16
 */
public interface JsonMapper {
  @JsonMapping
  static <T> T fromJson(String s, @TargetType Class<T> type) {
    if (s == null) {
      return null;
    }
    return JSON.parse(s, type);
  }

  @JsonMapping
  static <T> String toJson(T s) {
    return JSON.stringify(s);
  }

  @JsonMapping
  static <T> T fromJsonNode(JsonNode s, @TargetType Class<T> type) {
    if (s == null) {
      return null;
    }
    try {
      return JSON.reader().treeToValue(s, type);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("convert json node to value failed", e);
    }
  }

  @JsonMapping
  static <T> JsonNode toJsonNode(T s) {
    if (s == null) {
      return null;
    }
    try {
      return JSON.toObject(s, JsonNode.class);
    } catch (IOException e) {
      throw new RuntimeException("convert to json node failed", e);
    }
  }

  @Qualifier
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.CLASS)
  @interface JsonMapping {}
}
