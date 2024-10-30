package com.kaiqkt.auth.application.web.controllers;

import com.kaiqkt.auth.application.web.dto.request.UserRequest;
import com.kaiqkt.auth.application.web.dto.response.UserResponse;
import com.kaiqkt.auth.application.web.dto.response.PageResponse;
import com.kaiqkt.auth.domain.models.User;
import com.kaiqkt.auth.domain.models.enums.Status;
import com.kaiqkt.auth.domain.services.UserService;
import com.kaiqkt.auth.domain.utils.Constants;
import com.kaiqkt.auth.generated.application.controllers.UserApi;
import com.kaiqkt.auth.generated.application.dto.PageResponseV1;
import com.kaiqkt.auth.generated.application.dto.UserRequestV1;
import com.kaiqkt.auth.generated.application.dto.UserResponseV1;
import com.kaiqkt.springtools.security.utils.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController implements UserApi {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAnyRole('ROLE_API', 'ROLE_ADMIN')")
    @Override
    public ResponseEntity<UserResponseV1> create(UserRequestV1 userRequestV1) throws Exception {
        User user = UserRequest.toDomain(userRequestV1);
        userService.create(user, userRequestV1.getPassword());

        return ResponseEntity.ok(UserResponse.toResponse(user));
    }

    @PreAuthorize("hasAnyRole('ROLE_API', 'ROLE_ADMIN')")
    @Override
    public ResponseEntity<UserResponseV1> findById(String userId) throws Exception {
        User user = userService.findById(userId);
        return ResponseEntity.ok(UserResponse.toResponse(user));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @Override
    public ResponseEntity<UserResponseV1> findByToken() throws Exception {
        String userId = Context.getValue(Constants.USER_ID_KEY, String.class);
        User user = userService.findById(userId);
        return ResponseEntity.ok(UserResponse.toResponse(user));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public ResponseEntity<PageResponseV1> findAll(List<String> roles, String status, Integer page, Integer size, String sort) throws Exception {
        Page<User> users = userService.findAll(roles, status != null ? Status.fromString(status) : null, page, size, sort);

        return ResponseEntity.ok(PageResponse.toResponse(users, UserResponse::toResponse));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public ResponseEntity<Void> updateStatus(String userId, String status) throws Exception {
        userService.updateStatus(userId, status != null ? Status.fromString(status) : null);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public ResponseEntity<Void> updateRole(String userId, List<String> roles) throws Exception {
        userService.updateRoles(userId, roles);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public ResponseEntity<Void> removeRole(String userId, String roleId) throws Exception {
        userService.removeRole(userId, roleId);
        return ResponseEntity.noContent().build();
    }
}
