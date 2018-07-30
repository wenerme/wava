package com.github.wenerme.wava.data.repo;

import java.io.Serializable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2017/3/26
 */
@NoRepositoryBean
public interface BaseRepository<T, PK extends Serializable> extends CrudRepository<T, PK> {}
