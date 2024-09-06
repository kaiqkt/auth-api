package com.kaiqkt.auth.application.web.controllers;

import com.kaiqkt.auth.application.web.dto.response.RoleResponse;
import com.kaiqkt.auth.domain.services.RoleService;
import com.kaiqkt.auth.generated.application.controllers.RoleApi;
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
    public ResponseEntity<Void> create(String name) throws Exception {
        final String ROLE_PREFIX = "ROLE_";
        String processedName = (name != null && !name.startsWith(ROLE_PREFIX)) ? ROLE_PREFIX + name : name;
        roleService.create(processedName);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public ResponseEntity<Void> delete(String name) throws Exception {
        roleService.delete(name);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public ResponseEntity<List<RoleResponseV1>> findAll() throws Exception {
        return ResponseEntity.ok(roleService.findAll().stream().map(RoleResponse::toResponse).toList());
    }
}
