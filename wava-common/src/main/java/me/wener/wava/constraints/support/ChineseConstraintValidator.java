package me.wener.wava.constraints.support;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import me.wener.wava.constraints.Chinese;
import me.wener.wava.util.Chinesese;

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
