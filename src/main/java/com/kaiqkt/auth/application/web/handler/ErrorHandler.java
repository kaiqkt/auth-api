package com.kaiqkt.auth.application.web.handler;

import com.kaiqkt.auth.domain.exceptions.DomainException;
import com.kaiqkt.auth.generated.application.dto.ErrorV1;
import com.kaiqkt.springtools.security.exceptions.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
class ErrorHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ErrorHandler.class);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        Map<String, Object> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            FieldError fieldError = (FieldError) error;
            String name = fieldError.getField();
            String message = fieldError.getDefaultMessage();
            errors.put(name, message);
        });

        ErrorV1 error = new ErrorV1("INVALID_ARGUMENTS", errors);

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorV1> handleUnauthorizedException(UnauthorizedException ex, WebRequest request) {
        ErrorV1 error = new ErrorV1(ex.getType().name(), ex.getMessage());

        log(ex, request.getDescription(false));

        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorV1> handleDomainException(DomainException ex, WebRequest request) {
        ErrorV1 error = new ErrorV1(ex.getType().name(), ex.getType().getMessage());

        log(ex, request.getDescription(false));

        return new ResponseEntity<>(error, HttpStatusCode.valueOf(ex.getType().getCode()));
    }

    private void log(Exception ex, String description) {
        Logger log = LoggerFactory.getLogger(ErrorHandler.class);

        log.error("Error: {}, Message: {}, Request: {}", ex, ex.getMessage(), description);
    }
}
