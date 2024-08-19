package com.kaiqkt.auth.application.web.dto.request;

import com.kaiqkt.auth.domain.models.User;
import com.kaiqkt.auth.generated.application.dto.UserResponseV1;

import java.util.Optional;

public class UserResponse {
    public static UserResponseV1 toResponse(User user) {
        return new UserResponseV1(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getStatus().name(),
                user.getRoles().stream()
                        .map(RoleResponse::toResponse)
                        .toList(),
                user.getCreatedAt().toString(),
                Optional.ofNullable(user.getUpdatedAt())
                        .map(Object::toString)
                        .orElse(null)
        );
    }
}
