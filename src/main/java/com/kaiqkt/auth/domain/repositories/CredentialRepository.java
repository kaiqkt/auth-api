package com.kaiqkt.auth.domain.repositories;

import com.kaiqkt.auth.domain.models.Credential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredentialRepository extends JpaRepository<Credential, String> {
    Optional<Credential> findByUserEmail(String email);
    Optional<Credential> findByUserId(String userId);
    @Modifying
    @Query("UPDATE Credential c SET c.hash = :hash, c.updatedAt = CURRENT_TIMESTAMP WHERE c.user.id = :userId")
    void updateHash(String userId, String hash);

}
