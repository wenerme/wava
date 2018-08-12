package me.wener.wava.constraints;

import me.wener.wava.util.Chinesese;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2017/3/21
 */
public class ChineseConstraintValidator implements ConstraintValidator<Chinese, CharSequence> {

  @Override
  public void initialize(Chinese constraintAnnotation) {}

  @Override
  public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
    return value == null || Chinesese.isChinese(value);
  }
}
