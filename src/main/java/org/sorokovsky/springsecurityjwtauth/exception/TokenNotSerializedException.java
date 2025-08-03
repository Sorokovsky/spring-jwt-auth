package org.sorokovsky.springsecurityjwtauth.exception;

public class TokenNotSerializedException extends RuntimeException {
  public TokenNotSerializedException() {
    this("Token not serialized.");
  }

  public TokenNotSerializedException(String message) {
    super(message);
  }

  public TokenNotSerializedException(String message, Throwable cause) {
    super(message, cause);
  }

  public TokenNotSerializedException(Throwable cause) {
    super(cause);
  }

  public TokenNotSerializedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
