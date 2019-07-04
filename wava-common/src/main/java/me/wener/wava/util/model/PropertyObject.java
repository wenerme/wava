package me.wener.wava.util.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Maps;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import me.wener.wava.util.JSON;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2019-07-05
 */
@EqualsAndHashCode
@ToString
public class PropertyObject {
  @JsonIgnore @Setter private Map<String, Object> properties;

  @JsonAnyGetter
  public Map<String, Object> getProperties() {
    return properties;
  }

  @JsonAnySetter
  public <T extends PropertyObject> T set(String name, Object value) {
    if (properties == null) {
      properties = Maps.newHashMap();
    }
    properties.put(name, value);
    return (T) this;
  }

  public Object get(String name) {
    if (properties != null) {
      return properties.get(name);
    }
    return null;
  }

  public boolean has(String name) {
    if (properties != null) {
      return properties.containsKey(name);
    }
    return false;
  }

  public Map<String, Object> toMap() {
    return JSON.toMap(this);
  }

  public <R> R to(Class<R> type) {
    return JSON.toObject(this, type);
  }
}
