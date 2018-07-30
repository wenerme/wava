package com.github.wenerme.wava.data.entity;

import com.querydsl.core.annotations.QuerySupertype;
import java.io.Serializable;
import javax.annotation.Nullable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
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
public class AbstractEntity<PK extends Serializable> extends AbstractPersistableIdless<PK> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) // for PostgreSQL
  @Nullable
  private PK id;
}
