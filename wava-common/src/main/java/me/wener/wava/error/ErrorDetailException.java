package me.wener.wava.error;

/**
 * @author wener
 * @since 16/4/13
 */
public class ErrorDetailException extends RuntimeException {

  private final ErrorDetail detail;

  public ErrorDetailException(ErrorDetail detail) {
    super(detail.getCode() + ": " + detail.getMessage());
    this.detail = detail;
  }

  public ErrorDetailException(ErrorDetail detail, Throwable cause) {
    super(detail.getCode() + ": " + detail.getMessage(), cause);
    this.detail = detail;
  }

  public ErrorDetail getDetail() {
    return detail;
  }
}
