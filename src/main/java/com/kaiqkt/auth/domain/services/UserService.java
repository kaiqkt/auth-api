package com.kaiqkt.auth.domain.services;

import com.kaiqkt.auth.domain.exceptions.DomainException;
import com.kaiqkt.auth.domain.exceptions.ErrorType;
import com.kaiqkt.auth.domain.models.Role;
import com.kaiqkt.auth.domain.models.User;
import com.kaiqkt.auth.domain.models.enums.Status;
import com.kaiqkt.auth.domain.repositories.UserRepository;
import com.kaiqkt.auth.domain.utils.Constants;
import com.kaiqkt.auth.domain.utils.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CredentialService credentialService;
    private final RoleService roleService;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository, CredentialService credentialService, RoleService roleService) {
        this.userRepository = userRepository;
        this.credentialService = credentialService;
        this.roleService = roleService;
    }

    public void create(User user, String password) throws Exception {
        Role role = roleService.findById(Constants.USER_ROLE_ID);
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DomainException(ErrorType.EMAIL_ALREADY_IN_USE);
        }

        user.setRoles(List.of(role));

        userRepository.save(user);
        credentialService.create(user, password);

        log.info("User created: {} successfully", user.getId());
    }

    public User findById(String id) throws DomainException {
        return userRepository.findById(id).orElseThrow(() -> new DomainException(ErrorType.USER_NOT_FOUND));
    }

    public Page<User> findAll(
            List<String> roles,
            Status status,
            Integer page,
            Integer size,
            String sort
    ) {
        PageRequest pageRequest = Pageable.getPageRequest(page, size, sort);
        if (roles != null && !roles.isEmpty()) {
            return status != null ? userRepository.findByRolesAndStatus(roles, status, pageRequest) : userRepository.findByRoles(roles, pageRequest);
        }
        return status != null ? userRepository.findByStatus(status, pageRequest) : userRepository.findAll(pageRequest);
    }

    public void updateStatus(String id, Status status) throws DomainException {
        userRepository.updateStatus(status, id);
        log.info("User {} status updated to {} successfully", id, status);
    }

    public void updateRole(String id, String roleId) throws DomainException {
        Role role = roleService.findById(roleId);
        User user = findById(id);

        userRepository.addRole(user.getId(), role.getId());
        userRepository.updateUpdatedAt(user.getId());
        log.info("Added role {} to user {} successfully", role.getName(), user.getId());
    }

    public void removeRole(String id, String roleId) throws DomainException {
        Role role = roleService.findById(roleId);
        User user = findById(id);

        userRepository.removeRole(user.getId(), role.getId());
        userRepository.updateUpdatedAt(user.getId());
        log.info("Removed role {} from user {} successfully", role.getName(), user.getId());
    }
}
