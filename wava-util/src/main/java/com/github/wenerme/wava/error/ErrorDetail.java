package com.github.wenerme.wava.error;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author wener
 * @since 16/4/24
 */
@JsonDeserialize(as = Error.class)
public interface ErrorDetail {

  /** Error code */
  int getCode();

  /** Error status - map to http status */
  int getStatus();

  /** Error messages */
  String getMessage();

  /** Extra data attached to this error */
  Object getData();

  /** Path of this error */
  String getPath();

  /** Timestamp of this error */
  long getTimestamp();

  default ErrorDetail withMessage(String format, Object... args) {
    return withMessage(String.format(format, args));
  }

  default ErrorDetail withMessage(String message) {
    return Errors.builder(this).message(message).build();
  }

  default ErrorDetail withTimestamp(long timestamp) {
    return Errors.builder(this).timestamp(timestamp).build();
  }

  default ErrorDetail withPath(String path) {
    return Errors.builder(this).path(path).build();
  }

  default ErrorDetail withCode(int code) {
    return Errors.builder(this).code(code).build();
  }

  default ErrorDetail withData(Object data) {
    return Errors.builder(this).data(data).build();
  }

  default ErrorDetailException asException() {
    return new ErrorDetailException(this.now());
  }

  default ErrorDetailException asException(Throwable cause) {
    return new ErrorDetailException(this.now(), cause);
  }

  default ErrorDetailException asException(String message) {
    return withMessage(message).asException();
  }

  default ErrorDetail check(boolean condition, String format, Object... args) {
    if (!condition) {
      throw asException(String.format(format, args));
    }
    return this;
  }

  @Nonnull
  default <T> T checkNotNull(@Nullable T v, String message) {
    if (v == null) {
      throw asException(message);
    }
    return v;
  }

  default ErrorDetail check(boolean condition, String message) {
    if (!condition) {
      throw asException(message);
    }
    return this;
  }

  default ErrorDetail check(boolean condition) {
    if (!condition) {
      throw asException();
    }
    return this;
  }

  /** Set timestamp to current */
  default ErrorDetail now() {
    return withTimestamp(System.currentTimeMillis());
  }
}
