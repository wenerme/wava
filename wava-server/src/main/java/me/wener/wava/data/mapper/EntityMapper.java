package me.wener.wava.data.mapper;

import me.wener.wava.error.Errors;
import java.io.Serializable;
import javax.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.TargetType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Persistable;
/**
 * SpringData/JPA 映射, 需要依赖运行时
 *
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 28/12/2017
 */
@Mapper(uses = DataMapper.class, componentModel = "spring")
@Slf4j
public class EntityMapper {

  private EntityManager entityManager;

  @Autowired(required = false)
  // FIXME 这要求 JPA 必须在 ClassPath 中
  void init(EntityManager entityManager) {
    // 更好的处理没有 EntityManager 的情况
    this.entityManager = entityManager;
    if (entityManager == null) {
      log.warn("No EntityManager for EntityMapper");
    }
  }

  public <T extends Persistable<Long>> T integerToPersistable(
      Integer id, @TargetType Class<T> type) {
    Long l = id == null ? null : id.longValue();
    return longTopersistable(l, type);
  }

  public <T extends Persistable<Long>> T stringToPersistableLong(
      String id, @TargetType Class<T> type) {
    if (id == null) {
      return null;
    }
    long l = Long.parseLong(id);
    return longTopersistable(l, type);
  }

  public <T extends Persistable<Long>> T longTopersistable(Long id, @TargetType Class<T> type) {
    return idToPersistable(id, type);
  }

  private <T extends Persistable<ID>, ID extends Serializable> T idToPersistable(
      Object id, Class<?> type) {
    if (id == null) {
      return null;
    }
    T entity = entityManager.find((Class<T>) type, id);
    if (entity == null) {
      log.warn("{} with id {} not found", type.getSimpleName(), id);
      throw Errors.badRequest().asException("相应资源不存在或引用错误");
    }
    return entity;
  }
}
