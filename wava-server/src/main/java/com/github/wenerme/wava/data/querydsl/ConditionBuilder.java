package com.github.wenerme.wava.data.querydsl;

import com.google.common.base.Objects;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Visitor;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import javax.annotation.Nullable;

/**
 * {@link com.querydsl.core.BooleanBuilder} with more functions
 *
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 09/06/2017
 */
public class ConditionBuilder implements Predicate, Cloneable {

  private static final long serialVersionUID = -4129485177345542519L;

  @Nullable private Predicate predicate;

  /** Create an empty BooleanBuilder */
  public ConditionBuilder() {}

  /**
   * Create a BooleanBuilder with the given initial value
   *
   * @param initial initial value
   */
  public ConditionBuilder(Predicate initial) {
    predicate = (Predicate) ExpressionUtils.extract(initial);
  }

  public static ConditionBuilder create() {
    return new ConditionBuilder();
  }

  @Override
  public <R, C> R accept(Visitor<R, C> v, C context) {
    if (predicate != null) {
      return predicate.accept(v, context);
    } else {
      return null;
    }
  }

  /**
   * Create the intersection of this and the given predicate
   *
   * @param right right hand side of {@code and} operation
   * @return the current object
   */
  public ConditionBuilder and(@Nullable Predicate right) {
    if (right != null) {
      if (predicate == null) {
        predicate = right;
      } else {
        predicate = ExpressionUtils.and(predicate, right);
      }
    }
    return this;
  }

  /**
   * Create the intersection of this and the union of the given args {@code (this && (arg1 || arg2
   * ... || argN))}
   *
   * @param args union of predicates
   * @return the current object
   */
  public ConditionBuilder andAnyOf(Predicate... args) {
    if (args.length > 0) {
      and(ExpressionUtils.anyOf(args));
    }
    return this;
  }

  /**
   * Create the insertion of this and the negation of the given predicate
   *
   * @param right predicate to be negated
   * @return the current object
   */
  public ConditionBuilder andNot(Predicate right) {
    return and(right.not());
  }

  @Override
  public com.querydsl.core.BooleanBuilder clone() throws CloneNotSupportedException {
    return (com.querydsl.core.BooleanBuilder) super.clone();
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    } else if (o instanceof com.querydsl.core.BooleanBuilder) {
      return Objects.equal(((com.querydsl.core.BooleanBuilder) o).getValue(), predicate);
    } else {
      return false;
    }
  }

  @Nullable
  public Predicate getValue() {
    return predicate;
  }

  @Override
  public int hashCode() {
    return predicate != null ? predicate.hashCode() : 0;
  }

  /**
   * Returns true if the value is set, and false, if not
   *
   * @return true if initialized and false if not
   */
  public boolean hasValue() {
    return predicate != null;
  }

  @Override
  public ConditionBuilder not() {
    if (predicate != null) {
      predicate = predicate.not();
    }
    return this;
  }

  /**
   * Create the union of this and the given predicate
   *
   * @param right right hand side of {@code or} operation
   * @return the current object
   */
  public ConditionBuilder or(@Nullable Predicate right) {
    if (right != null) {
      if (predicate == null) {
        predicate = right;
      } else {
        predicate = ExpressionUtils.or(predicate, right);
      }
    }
    return this;
  }

  /**
   * Create the union of this and the intersection of the given args {@code (this || (arg1 && arg2
   * ... && argN))}
   *
   * @param args intersection of predicates
   * @return the current object
   */
  public ConditionBuilder orAllOf(Predicate... args) {
    if (args.length > 0) {
      or(ExpressionUtils.allOf(args));
    }
    return this;
  }

  /**
   * Create the union of this and the negation of the given predicate
   *
   * @param right predicate to be negated
   * @return the current object
   */
  public ConditionBuilder orNot(Predicate right) {
    return or(right.not());
  }

  @Override
  public Class<? extends Boolean> getType() {
    return Boolean.class;
  }

  @Override
  public String toString() {
    return predicate != null ? predicate.toString() : super.toString();
  }

  // region Extension
  public <T> ConditionBuilder and(@Nullable T val, Function<T, ? extends Predicate> exp) {
    if (val == null) {
      return this;
    }
    return and(exp.apply(val));
  }

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  public ConditionBuilder and(Optional<? extends Predicate> exp) {
    if (exp == null || !exp.isPresent()) {
      return this;
    }
    return and(exp.get());
  }

  public <T extends Collection<E>, E> ConditionBuilder andAnyOf(
      T vals, Function<E, ? extends Predicate> exp) {
    if (vals == null || vals.isEmpty()) {
      return this;
    }

    Predicate rv = null;
    for (E val : vals) {
      if (val != null) {
        Predicate b = exp.apply(val);
        rv = rv == null ? b : ExpressionUtils.or(rv, b);
      }
    }
    return and(rv);
  }

  public <T extends Collection<E>, E> ConditionBuilder orAnyOf(
      T vals, Function<E, ? extends Predicate> exp) {
    if (vals == null || vals.isEmpty()) {
      return this;
    }

    Predicate rv = null;
    for (E val : vals) {
      if (val != null) {
        Predicate b = exp.apply(val);
        rv = rv == null ? b : ExpressionUtils.or(rv, b);
      }
    }
    return or(rv);
  }
  // endregion
}
