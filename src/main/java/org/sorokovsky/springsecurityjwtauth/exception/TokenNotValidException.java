package org.sorokovsky.springsecurityjwtauth.exception;

public class TokenNotValidException extends RuntimeException {
    public TokenNotValidException() {
        this("Token not valid");
    }

    public TokenNotValidException(String message) {
        super(message);
    }

    public TokenNotValidException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenNotValidException(Throwable cause) {
        super(cause);
    }

    public TokenNotValidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
