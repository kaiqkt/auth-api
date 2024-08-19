package com.kaiqkt.auth.domain.services;

import com.kaiqkt.auth.domain.exceptions.DomainException;
import com.kaiqkt.auth.domain.exceptions.ErrorType;
import com.kaiqkt.auth.domain.models.Role;
import com.kaiqkt.auth.domain.repositories.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    public void create(String name) throws DomainException{
        if (roleRepository.findByName(name).isPresent()) {
            return;
        }

        Role role = new Role(name);
        roleRepository.save(role);

        log.info("Role {} created successfully", role.getName());
    }

    @Transactional
    public void delete(String name) throws DomainException{
        final String ADMIN_ROLE = "ROLE_ADMIN";
        final String USER_ROLE = "ROLE_USER";

        if (ADMIN_ROLE.equals(name) || USER_ROLE.equals(name)) {
            log.warn("Attempted to delete protected role: {}", name);
            throw new DomainException(ErrorType.INVALID_OPERATION.setMessage("Protected role cannot be deleted."));
        }

        Role role = roleRepository.findByName(name).orElseThrow(() -> new DomainException(ErrorType.ROLE_NOT_FOUND));
        roleRepository.removeAllByRoleId(role.getId());
        roleRepository.deleteById(role.getId());

        log.info("Role {} delete successfully", role.getName());
    }

    public Role findById(String id) throws DomainException {
        return roleRepository.findById(id).orElseThrow(() -> new DomainException(ErrorType.ROLE_NOT_FOUND));
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }
}
