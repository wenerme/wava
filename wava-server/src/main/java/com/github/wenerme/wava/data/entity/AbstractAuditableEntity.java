package com.github.wenerme.wava.data.entity;

import com.querydsl.core.annotations.QuerySupertype;
import java.io.Serializable;
import javax.annotation.Nullable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 25/12/2017
 */
@MappedSuperclass
@QuerySupertype
@Setter
@Getter
public abstract class AbstractAuditableEntity<U, PK extends Serializable>
    extends AbstractTimeAuditableEntity<PK> {

  @CreatedBy
  @JoinColumn(name = "create_by", updatable = false, nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private U createBy;

  @LastModifiedBy
  @JoinColumn(name = "update_by", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private U updateBy;

  @Nullable
  @JoinColumn(name = "delete_by")
  @ManyToOne(fetch = FetchType.LAZY)
  private U deleteBy;
}
