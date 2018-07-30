package com.github.wenerme.wava.data.entity;

import com.querydsl.core.annotations.QuerySupertype;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 15/01/2018
 */
@MappedSuperclass
@QuerySupertype
@Setter
@Getter
public class AbstractVersionedEntity<PK extends Serializable> extends AbstractEntity<PK> {

  @Version
  @Column(name = "version")
  private Long version;
}
