package com.github.wenerme.wava.hibernate.dialect;

import com.github.wenerme.postjava.hibernate.dialect.PostgreSQLJsonDialect;
import org.hibernate.dialect.PostgreSQL95Dialect;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2018/6/12
 */
public class PostgreSQLCustomDialect extends PostgreSQL95Dialect {

  public PostgreSQLCustomDialect() {
    super();
    PostgreSQLJsonDialect.register(this);
  }
}
