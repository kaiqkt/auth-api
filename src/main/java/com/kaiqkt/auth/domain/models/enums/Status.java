package com.kaiqkt.auth.domain.models.enums;


import com.kaiqkt.auth.domain.exceptions.DomainException;
import com.kaiqkt.auth.domain.exceptions.ErrorType;

public enum Status {
    WAITING_EMAIL_VERIFICATION,
    ACTIVE,
    BLOCKED;

    public static Status fromString(String status) throws DomainException {
        return switch (status) {
            case "WAITING_EMAIL_VERIFICATION" -> WAITING_EMAIL_VERIFICATION;
            case "ACTIVE" -> ACTIVE;
            case "BLOCKED" -> BLOCKED;
            default -> throw new DomainException(ErrorType.INVALID_STATUS);
        };
    }
}
