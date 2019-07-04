package me.wener.wava.constraints.support;

import com.google.common.base.Strings;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import me.wener.wava.constraints.DateFormatPattern;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2019-07-05
 */
public
class DateFormatPatternConstraintValidator
  implements ConstraintValidator<DateFormatPattern, String> {

  private SimpleDateFormat format;

  @Override
  public void initialize(DateFormatPattern constraintAnnotation) {
    format = new SimpleDateFormat(constraintAnnotation.pattern());
    format.setLenient(false);
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (!Strings.isNullOrEmpty(value)) {
      try {
        format.parse(value);
      } catch (ParseException e) {
        return false;
      }
    }
    return true;
  }
}
