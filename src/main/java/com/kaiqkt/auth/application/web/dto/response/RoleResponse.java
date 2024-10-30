package com.kaiqkt.auth.application.web.dto.response;

import com.kaiqkt.auth.domain.models.Role;
import com.kaiqkt.auth.generated.application.dto.RoleResponseV1;

public class RoleResponse {

    public static RoleResponseV1 toResponse(Role role) {
        return new RoleResponseV1(
                role.getId(),
                role.getName(),
                role.getDescription(),
                role.getCreatedAt().toString()
        );
    }
}
