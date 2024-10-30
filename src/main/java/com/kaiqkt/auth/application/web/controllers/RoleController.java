package com.kaiqkt.auth.application.web.controllers;

import com.kaiqkt.auth.application.web.dto.response.PageResponse;
import com.kaiqkt.auth.application.web.dto.response.RoleResponse;
import com.kaiqkt.auth.domain.services.RoleService;
import com.kaiqkt.auth.generated.application.controllers.RoleApi;
import com.kaiqkt.auth.generated.application.dto.DeleteRolesRequestV1;
import com.kaiqkt.auth.generated.application.dto.PageResponseV1;
import com.kaiqkt.auth.generated.application.dto.RoleRequestV1;
import com.kaiqkt.auth.generated.application.dto.RoleResponseV1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RoleController implements RoleApi {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public ResponseEntity<Void> create(RoleRequestV1 requestV1) throws Exception {
        roleService.create(requestV1.getName(), requestV1.getDescription());

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public ResponseEntity<Void> delete(DeleteRolesRequestV1 requestV1) throws Exception {
        roleService.delete(requestV1.getIds());
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public ResponseEntity<PageResponseV1> findAll(String param, Integer page, Integer size, String sort) throws Exception {
        return ResponseEntity.ok(PageResponse.toResponse(roleService.findAll(param, page, size, sort), RoleResponse::toResponse));
    }
}
