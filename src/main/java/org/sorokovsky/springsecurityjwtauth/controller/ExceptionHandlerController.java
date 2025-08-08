package org.sorokovsky.springsecurityjwtauth.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sorokovsky.springsecurityjwtauth.exception.*;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Locale;

@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandlerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerController.class);

    private final MessageSource messageSource;

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<HashMap<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        final var errors = new HashMap<String, String>();
        exception.getBindingResult().getFieldErrors().forEach((error) -> {
            final var errorName = error.getField();
            final var message = error.getDefaultMessage();
            errors.put(errorName, message);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler({InvalidCredentialsException.class})
    public ResponseEntity<String> handleInvalidCredentialsException(
            InvalidCredentialsException exception, Locale locale
    ) {
        LOGGER.info(exception.getMessage());
        return ResponseEntity
                .badRequest()
                .body(getMessage("errors.invalid-credentials", messageSource, locale));
    }

    @ExceptionHandler({TokenNotSerializedException.class, TokenNotParseException.class})
    public ResponseEntity<String> handleTokenNotSerializedException(Exception exception, Locale locale) {
        LOGGER.error(exception.getMessage(), exception);
        return ResponseEntity
                .internalServerError()
                .body(getMessage("errors.unknown-error", messageSource, locale));
    }

    @ExceptionHandler({TokenNotValidException.class})
    public ResponseEntity<String> handleTokenNotValidException(TokenNotValidException exception, Locale locale) {
        LOGGER.error(exception.getMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(getMessage("errors.token-not-valid", messageSource, locale));
    }

    @ExceptionHandler({UserAlreadyExistsException.class})
    public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException exception, Locale locale) {
        LOGGER.info(exception.getMessage());
        return ResponseEntity.badRequest().body(getMessage("errors.user-already-exists", messageSource, locale));
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException exception, Locale locale) {
        LOGGER.error(exception.getMessage(), exception);
        return ResponseEntity.internalServerError().body(getMessage("errors.unknown-error", messageSource, locale));
    }

    private String getMessage(String key, MessageSource messageSource, Locale locale, Object[] args) {
        final var defaultMessage = messageSource.getMessage(key, args, Locale.ENGLISH);
        return messageSource.getMessage(key, args, defaultMessage, locale);
    }

    private String getMessage(String key, MessageSource messageSource, Locale locale) {
        return getMessage(key, messageSource, locale, new Object[0]);
    }
}
