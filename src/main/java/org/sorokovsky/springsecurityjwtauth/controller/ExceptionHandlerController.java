package org.sorokovsky.springsecurityjwtauth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sorokovsky.springsecurityjwtauth.exception.TokenNotParsedException;
import org.sorokovsky.springsecurityjwtauth.exception.TokenNotSerializedException;
import org.sorokovsky.springsecurityjwtauth.exception.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerController.class);

    @ExceptionHandler({UserAlreadyExistsException.class})
    public ResponseEntity<?> handleUserAlreadyExistsException(UserAlreadyExistsException exception) {
        LOGGER.error(exception.getMessage(), exception);
        return ResponseEntity.badRequest().body("User already exists.");
    }

    @ExceptionHandler({TokenNotParsedException.class})
    public ResponseEntity<?> handleTokenNotParsedException(TokenNotParsedException exception) {
        LOGGER.error(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Failed access.");
    }

    @ExceptionHandler({TokenNotSerializedException.class})
    public ResponseEntity<?> handleTokenNotSerializedException(TokenNotSerializedException exception) {
        LOGGER.error(exception.getMessage(), exception);
        return ResponseEntity.internalServerError().body("Failed authentication, try again.");
    }
}
