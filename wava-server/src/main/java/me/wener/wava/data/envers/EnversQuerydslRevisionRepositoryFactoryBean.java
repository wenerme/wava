package me.wener.wava.data.envers;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.envers.DefaultRevisionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.GenericTypeResolver;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.envers.repository.support.ReflectionRevisionEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.history.support.RevisionEntityInformation;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 04/01/2018
 */
@Slf4j
public class EnversQuerydslRevisionRepositoryFactoryBean<
        T extends RevisionRepository<S, ID, N>, S, ID, N extends Number & Comparable<N>>
    extends EnversRevisionRepositoryFactoryBean<T, S, ID, N> {

  @Autowired(required = false)
  @Qualifier("revisionEntityClass")
  private Class<?> revisionEntityClass;

  /**
   * Creates a new {@link EnversRevisionRepositoryFactoryBean} for the given repository interface.
   *
   * @param repositoryInterface must not be {@literal null}.
   */
  public EnversQuerydslRevisionRepositoryFactoryBean(Class repositoryInterface) {
    super(repositoryInterface);
  }

  @PostConstruct
  public void init() {
    if (revisionEntityClass != null) {
      super.setRevisionEntityClass(revisionEntityClass);
    }
  }

  @Override
  public void setRevisionEntityClass(Class<?> revisionEntityClass) {
    super.setRevisionEntityClass(revisionEntityClass);
    this.revisionEntityClass = revisionEntityClass;
  }

  @Override
  protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
    return new RevisionRepositoryFactory<T, ID, N>(entityManager, revisionEntityClass);
  }

  /**
   * Repository factory creating {@link RevisionRepository} instances.
   *
   * @author Oliver Gierke
   */
  private static class RevisionRepositoryFactory<T, ID, N extends Number & Comparable<N>>
      extends JpaRepositoryFactory {

    private final RevisionEntityInformation revisionEntityInformation;
    private final EntityManager entityManager;

    /**
     * Creates a new {@link EnversRevisionRepositoryFactoryBean.RevisionRepositoryFactory} using the
     * given {@link EntityManager} and revision entity class.
     *
     * @param entityManager must not be {@literal null}.
     * @param revisionEntityClass can be {@literal null}, will default to {@link
     *     DefaultRevisionEntity}.
     */
    public RevisionRepositoryFactory(EntityManager entityManager, Class<?> revisionEntityClass) {

      super(entityManager);
      this.entityManager = entityManager;
      revisionEntityClass =
          revisionEntityClass == null ? DefaultRevisionEntity.class : revisionEntityClass;
      this.revisionEntityInformation =
          DefaultRevisionEntity.class.equals(revisionEntityClass)
              ? new DefaultRevisionEntityInformation()
              : new ReflectionRevisionEntityInformation(revisionEntityClass);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.jpa.repository.support.JpaRepositoryFactory#getTargetRepository(org.springframework.data.repository.core.RepositoryMetadata, javax.persistence.EntityManager)
     */
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected EnverQuerydslJpaRepository getTargetRepository(RepositoryInformation information) {

      JpaEntityInformation<T, Object> entityInformation =
          (JpaEntityInformation<T, Object>) getEntityInformation(information.getDomainType());

      return new EnverQuerydslJpaRepository(
          entityInformation, entityManager, revisionEntityInformation);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.jpa.repository.support.JpaRepositoryFactory#getRepositoryBaseClass(org.springframework.data.repository.core.RepositoryMetadata)
     */
    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
      return EnverQuerydslJpaRepository.class;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.core.support.RepositoryFactorySupport#getRepository(java.lang.Class, java.lang.Object)
     */
    @Override
    @SuppressWarnings("hiding")
    public <T> T getRepository(Class<T> repositoryInterface, Object customImplementation) {

      if (RevisionRepository.class.isAssignableFrom(repositoryInterface)) {

        Class<?>[] typeArguments =
            GenericTypeResolver.resolveTypeArguments(repositoryInterface, RevisionRepository.class);
        Class<?> revisionNumberType = typeArguments[2];

        if (!revisionEntityInformation.getRevisionNumberType().equals(revisionNumberType)) {
          throw new IllegalStateException(
              String.format(
                  "Configured a revision entity type of %s with a revision type of %s "
                      + "but the repository interface is typed to a revision type of %s!",
                  repositoryInterface,
                  revisionEntityInformation.getRevisionNumberType(),
                  revisionNumberType));
        }
      }

      return super.getRepository(repositoryInterface, customImplementation);
    }
  }
}
