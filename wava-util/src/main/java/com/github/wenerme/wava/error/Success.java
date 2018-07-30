package com.github.wenerme.wava.error;

/**
 * @author wener
 * @since 16/4/18
 */
public interface Success {

  static GeneralMessage ok(String message) {
    return new GeneralMessage().setMessage(message);
  }

  static GeneralMessage ok(boolean result) {
    return new GeneralMessage().setResult(result);
  }

  static GeneralMessage created(Object id, String message) {
    return new GeneralMessage().setId(String.valueOf(id)).setMessage(message).setStatus(201);
  }

  static GeneralMessage created(Object id) {
    return created(id, null);
  }

  static GeneralMessage deleted(Object id, String message) {
    return new GeneralMessage().setId(String.valueOf(id)).setMessage(message).setStatus(200);
  }

  static GeneralMessage deleted(Object id) {
    return deleted(id, null);
  }

  static GeneralMessage ok() {
    return new GeneralMessage();
  }
}
