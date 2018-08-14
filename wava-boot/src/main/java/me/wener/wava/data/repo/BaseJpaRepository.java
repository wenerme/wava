package me.wener.wava.data.repo;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import me.wener.wava.error.Errors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author wener
 * @since 16/4/13
 */
@NoRepositoryBean
public interface BaseJpaRepository<T, PK extends Serializable>
    extends BaseQueryRepository<T, PK>, JpaRepository<T, PK>, JpaSpecificationExecutor<T> {

  default Optional<T> findFirst() {
    // FIXME 避免 count
    List<T> content = findAll(PageRequest.of(0, 1)).getContent();
    if (content.size() == 0) {
      return Optional.empty();
    }
    return Optional.of(content.get(0));
  }

  default T requireById(PK id) {
    return findById(id).orElseThrow(() -> Errors.notFound().asException("资源不存在"));
  }
}
