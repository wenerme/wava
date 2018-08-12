package me.wener.wava.constraints;

import com.fasterxml.jackson.databind.node.JsonNodeType;
import me.wener.wava.util.JSON;
import com.google.common.collect.ImmutableSet;
import java.util.ArrayList;
import java.util.Optional;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2017/2/27
 */
public class JsonTypeConstraintValidator implements ConstraintValidator<JsonType, String> {

  private ImmutableSet<JsonNodeType> types;

  @Override
  public void initialize(JsonType constraintAnnotation) {
    ArrayList<JsonNodeType> list = new ArrayList<>(constraintAnnotation.types().length);
    for (JsonType.Type type : constraintAnnotation.types()) {
      list.add(JsonNodeType.valueOf(type.name()));
    }
    types = ImmutableSet.copyOf(list);
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }
    return Optional.ofNullable(JSON.getJsonType(value)).map(v -> types.contains(v)).orElse(false);
  }
}
