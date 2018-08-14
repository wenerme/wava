package me.wener.wava.data.entity;

import com.querydsl.core.annotations.QuerySupertype;
import java.io.Serializable;
import java.time.Instant;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 25/12/2017
 */
@MappedSuperclass
@QuerySupertype
@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractTimeAuditableEntity<PK extends Serializable>
    extends AbstractVersionedEntity<PK> {

  @CreatedDate
  @Column(name = "create_time", updatable = false, nullable = false)
  private Instant createTime;

  @LastModifiedDate
  @Column(name = "update_time", nullable = false)
  private Instant updateTime;

  @Nullable
  @Column(name = "delete_time")
  private Instant deleteTime;
}
