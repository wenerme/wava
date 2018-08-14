package me.wener.wava.data;

import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.domain.Persistable;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2018/6/13
 */
public interface SpringData {

  /** 尽量在不初始化的前提下获取标识符 */
  static <T> T getLazyIdentifier(Persistable<T> persistable) {
    if (persistable instanceof HibernateProxy) {
      return (T) ((HibernateProxy) persistable).getHibernateLazyInitializer().getIdentifier();
    }
    return persistable.getId();
  }

  /** 在事务提交后执行 */
  static void committed(Runnable action) {
    if (TransactionSynchronizationManager.isSynchronizationActive()) {
      TransactionSynchronizationManager.registerSynchronization(
          new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
              action.run();
            }
          });
    } else {
      // 如果未开启事务, 则直接执行, 很少会发生
      action.run();
    }
  }
}
