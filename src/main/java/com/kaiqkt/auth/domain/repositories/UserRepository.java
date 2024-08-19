package com.kaiqkt.auth.domain.repositories;

import com.kaiqkt.auth.domain.models.Role;
import com.kaiqkt.auth.domain.models.User;
import com.kaiqkt.auth.domain.models.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Boolean existsByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.status = :status, u.updatedAt = CURRENT_TIMESTAMP WHERE u.id = :userId")
    void updateStatus(Status status, String userId);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO user_roles (user_id, role_id) VALUES (:userId, :roleId)", nativeQuery = true)
    void addRole(String userId, String roleId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM user_roles WHERE user_id = :userId AND role_id = :roleId", nativeQuery = true)
    void removeRole(String userId, String roleId);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.updatedAt = CURRENT_TIMESTAMP WHERE u.id = :userId")
    void updateUpdatedAt(String userId);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name IN :roles AND u.status = :status")
    Page<User> findByRolesAndStatus(List<String> roles, Status status, PageRequest pageable);

    @Query("SELECT u FROM User u WHERE u.status = :status")
    Page<User> findByStatus(Status status, PageRequest pageable);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name IN :roles")
    Page<User> findByRoles(List<String> roles, PageRequest pageable);
}
