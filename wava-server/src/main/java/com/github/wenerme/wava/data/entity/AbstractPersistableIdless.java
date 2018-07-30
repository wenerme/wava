package com.github.wenerme.wava.data.entity;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.util.ClassUtils;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2018/5/24
 */
@MappedSuperclass
public abstract class AbstractPersistableIdless<T> implements Persistable<T> {

  /**
   * Must be {@link Transient} in order to ensure that no JPA provider complains because of a
   * missing setter.
   *
   * @see Persistable#isNew()
   */
  @Transient // DATAJPA-622
  public boolean isNew() {
    return null == getId();
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return String.format("Entity of type %s with id: %s", this.getClass().getName(), getId());
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {

    if (null == obj) {
      return false;
    }

    if (this == obj) {
      return true;
    }

    if (!getClass().equals(ClassUtils.getUserClass(obj))) {
      return false;
    }

    AbstractPersistableIdless<?> that = (AbstractPersistableIdless<?>) obj;

    return null != this.getId() && this.getId().equals(that.getId());
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {

    int hashCode = 17;

    hashCode += null == getId() ? 0 : getId().hashCode() * 31;

    return hashCode;
  }
}
