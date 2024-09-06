package com.kaiqkt.auth.application.web.dto.request;

import com.kaiqkt.auth.domain.models.User;
import com.kaiqkt.auth.generated.application.dto.UserRequestV1;

import java.util.List;

public class UserRequest {
    public static User toDomain(UserRequestV1 userRequestV1) {
        return new User(
                userRequestV1.getFirstName(),
                userRequestV1.getLastName(),
                userRequestV1.getEmail()
        );
    }
}
