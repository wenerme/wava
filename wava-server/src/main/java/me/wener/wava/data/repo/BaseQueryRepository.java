package me.wener.wava.data.repo;

import me.wener.wava.data.querydsl.IsPredicate;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import java.io.Serializable;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2017/3/26
 */
@NoRepositoryBean
public interface BaseQueryRepository<T, PK extends Serializable>
    extends BaseRepository<T, PK>,
        PagingAndSortingRepository<T, PK>,
        QuerydslPredicateExecutor<T>,
        QueryByExampleExecutor<T> {

  // 底层是返回的 List
  // region QueryDSL List

  List<T> findAll(Predicate predicate);

  List<T> findAll(Predicate predicate, Sort sort);

  List<T> findAll(Predicate predicate, OrderSpecifier<?>... orders);

  List<T> findAll(OrderSpecifier<?>... orders);

  // endregion

  default List<T> listAll(Predicate predicate, Pageable pageable) {
    // FIXME 避免 count
    return findAll(predicate, pageable).getContent();
  }

  // region IsPredicate
  default List<T> findAll(IsPredicate predicate) {
    return findAll(predicate.toPredicate());
  }

  default Page<T> findAll(IsPredicate predicate, Pageable pageable) {
    return findAll(predicate.toPredicate(), pageable);
  }
  // endregion
}
