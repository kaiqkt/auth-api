package com.kaiqkt.auth.domain.repositories;

import com.kaiqkt.auth.domain.models.Verification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationRepository extends JpaRepository<Verification, String> {
}
