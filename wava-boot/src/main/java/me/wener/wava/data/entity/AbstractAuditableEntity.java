package me.wener.wava.data.entity;

import com.querydsl.core.annotations.QuerySupertype;
import java.io.Serializable;
import javax.annotation.Nullable;
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
public abstract class AbstractAuditableEntity<PK extends Serializable>
    extends AbstractTimeAuditableEntity<PK> {

  @CreatedBy private String createBy;

  @LastModifiedBy private String updateBy;

  @Nullable private String deleteBy;
}
