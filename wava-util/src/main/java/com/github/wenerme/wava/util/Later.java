package com.github.wenerme.wava.util;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;

/**
 * Later for future
 *
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2018/4/13
 */
public interface Later {

  /**
   * when complete, complete the give value
   *
   * <pre>
   * other.whenComplete(Later.complete(value));
   * </pre>
   */
  static <T> BiConsumer<T, Throwable> complete(CompletableFuture<T> value) {
    return (v, e) -> {
      if (e != null) {
        value.completeExceptionally(e);
      } else {
        value.complete(v);
      }
    };
  }

  /** @return a completed {@link CompletableFuture} with given exception */
  static <T> CompletableFuture<T> completeExceptionally(Throwable e) {
    CompletableFuture<T> future = new CompletableFuture<>();
    future.completeExceptionally(e);
    return future;
  }

  /** treat the source as {@link CompletionStage} or convert to */
  @SuppressWarnings("unchecked")
  static <T> CompletionStage<T> asCompletionStage(T o) {
    if (o instanceof CompletionStage) {
      return (CompletionStage<T>) o;
    }
    if (o instanceof ListenableFuture) {
      return toCompletableFuture((ListenableFuture<T>) o);
    }
    return CompletableFuture.completedFuture(o);
  }

  /** convert {@link ListenableFuture} to CompletableFuture */
  static <T> CompletableFuture<T> toCompletableFuture(ListenableFuture<T> future) {
    return toCompletableFuture(future, MoreExecutors.directExecutor());
  }

  /** convert {@link ListenableFuture} to CompletableFuture */
  static <T> CompletableFuture<T> toCompletableFuture(
      ListenableFuture<T> future, Executor executor) {
    CompletableFuture<T> completableFuture = new CompletableFuture<>();
    future.addListener(
        () -> {
          try {
            completableFuture.complete(future.get());
          } catch (Throwable e) {
            completableFuture.completeExceptionally(e);
          }
        },
        executor);

    return completableFuture;
  }
}
