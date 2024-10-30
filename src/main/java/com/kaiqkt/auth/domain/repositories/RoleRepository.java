package com.kaiqkt.auth.domain.repositories;

import com.kaiqkt.auth.domain.models.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Boolean existsByName(String name);
    @Transactional
    @Modifying
    @Query("DELETE FROM Role r WHERE r.id IN :ids")
    void deleteByIds(List<String> ids);
    @Query("SELECT r FROM Role r WHERE :searchTerm IS NULL OR r.id = :searchTerm OR LOWER(r.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Role> findByIdOrNameContainingIgnoreCase(String searchTerm, Pageable pageable);
}
