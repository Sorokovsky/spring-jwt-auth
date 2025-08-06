package org.sorokovsky.springsecurityjwtauth.exception;

public class TokenNotParseException extends RuntimeException {
    public TokenNotParseException() {
        this("Token not parse");
    }

    public TokenNotParseException(String message) {
        super(message);
    }

    public TokenNotParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenNotParseException(Throwable cause) {
        super(cause);
    }

    public TokenNotParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
