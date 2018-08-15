package me.wener.wava.data.repo;

import java.io.Serializable;
import java.util.Optional;
import me.wener.wava.error.Errors;
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

  Optional<T> findFirst();

  default T requireById(PK id) {
    return findById(id).orElseThrow(() -> Errors.notFound().asException("资源不存在"));
  }
}
