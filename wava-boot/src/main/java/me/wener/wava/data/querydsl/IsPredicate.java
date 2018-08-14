package me.wener.wava.data.querydsl;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Visitor;
import javax.annotation.Nullable;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2018/5/30
 */
public interface IsPredicate extends Predicate {

  Predicate toPredicate();

  @Override
  default Predicate not() {
    return toPredicate().not();
  }

  @Override
  @Nullable
  default <R, C> R accept(Visitor<R, C> v, @Nullable C context) {
    return toPredicate().accept(v, context);
  }

  @Override
  default Class<? extends Boolean> getType() {
    return toPredicate().getType();
  }
}
