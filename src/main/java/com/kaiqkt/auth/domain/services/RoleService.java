package com.kaiqkt.auth.domain.services;

import com.kaiqkt.auth.domain.exceptions.DomainException;
import com.kaiqkt.auth.domain.exceptions.ErrorType;
import com.kaiqkt.auth.domain.models.Role;
import com.kaiqkt.auth.domain.repositories.RoleRepository;
import com.kaiqkt.auth.domain.utils.Constants;
import com.kaiqkt.auth.domain.utils.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private static final Logger log = LoggerFactory.getLogger(RoleService.class);

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void create(String name, String description) throws DomainException {
        String processedName = (!name.startsWith("ROLE_")) ? "ROLE_" + name : name;

        if (roleRepository.existsByName(processedName)) {
            return;
        }

        roleRepository.save(new Role(processedName, description));

        log.info("Role {} created successfully", name);
    }

    @Transactional
    public void delete(List<String> ids) throws DomainException {
        List<Role> roles = roleRepository.findAllById(ids);

        List<String> nonProtectedRoles = roles.stream()
                .filter(role -> !Constants.DEFAULT_ROLES.contains(role.getName()))
                .map(Role::getId)
                .toList();

        roleRepository.deleteByIds(nonProtectedRoles);

        log.info("Roles {} deleted successfully", nonProtectedRoles);
    }

    public Role findById(String id) throws DomainException {
        return roleRepository.findById(id).orElseThrow(() -> new DomainException(ErrorType.ROLE_NOT_FOUND));
    }

    public List<Role> findByIds(List<String> ids) {
        return roleRepository.findAllById(ids);
    }

    public Page<Role> findAll(String param, Integer page, Integer size, String sort) {
        return roleRepository.findByIdOrNameContainingIgnoreCase(param, Pageable.getPageRequest(page, size, sort));
    }
}
