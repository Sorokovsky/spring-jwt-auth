package org.sorokovsky.springsecurityjwtauth.exception;

public class TokenNotParsedException extends RuntimeException {
  public TokenNotParsedException() {
    this("Token not parsed");
  }

  public TokenNotParsedException(String message) {
    super(message);
  }

  public TokenNotParsedException(String message, Throwable cause) {
    super(message, cause);
  }

  public TokenNotParsedException(Throwable cause) {
    super(cause);
  }

  public TokenNotParsedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
