package com.kaiqkt.auth.domain.exceptions;

import org.springframework.http.HttpStatus;

public enum ErrorType {
    ROLE_NOT_FOUND("Role not found", HttpStatus.NOT_FOUND.value()),
    USER_NOT_FOUND("User not found", HttpStatus.NOT_FOUND.value()),
    INVALID_CREDENTIAL("Invalid Credential", HttpStatus.UNAUTHORIZED.value()),
    EMAIL_ALREADY_IN_USE("Email already in use", HttpStatus.BAD_REQUEST.value()),
    INVALID_OPERATION("Invalid Operation", HttpStatus.BAD_REQUEST.value()),
    USER_BLOCKED("User account blocked", HttpStatus.FORBIDDEN.value()),
    WAITING_EMAIL_VERIFICATION("Waiting email verification", HttpStatus.FORBIDDEN.value()),
    SESSION_NOT_FOUND("Valid Session not found", HttpStatus.NOT_FOUND.value()),
    INVALID_REFRESH_TOKEN("Refresh token expired or invalid", HttpStatus.UNAUTHORIZED.value()),
    INVALID_STATUS("Invalid Status", HttpStatus.BAD_REQUEST.value());

    private String message;
    private final int code;


    ErrorType(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public ErrorType setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
