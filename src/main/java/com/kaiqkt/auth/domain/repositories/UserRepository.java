package com.kaiqkt.auth.domain.repositories;

import com.kaiqkt.auth.domain.models.User;
import com.kaiqkt.auth.domain.models.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    @Query(
            value = "INSERT INTO user_roles (user_id, role_id) " +
                    "SELECT :userId, unnest(:roleIds) " +
                    "WHERE NOT EXISTS ( " +
                    "    SELECT 1 " +
                    "    FROM user_roles ur " +
                    "    WHERE ur.user_id = :userId AND ur.role_id IN (SELECT * FROM unnest(:roleIds)) " +
                    ")",
            nativeQuery = true
    )
    void addRoles(String userId, String[] roleIds);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.updatedAt = CURRENT_TIMESTAMP WHERE u.id = :userId")
    void updatedAt(String userId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM user_roles WHERE user_id = :userId AND role_id = :roleId", nativeQuery = true)
    void removeRole(String userId, String roleId);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN u.roles r " +
            "WHERE (:roles IS NULL OR r.name IN :roles) " +
            "AND (:status IS NULL OR u.status = :status)")
    Page<User> findByRolesAndStatus(List<String> roles, Status status, Pageable pageable);
}
