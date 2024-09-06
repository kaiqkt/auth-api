package com.kaiqkt.auth.domain.repositories;

import com.kaiqkt.auth.domain.models.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, String> {
    @Transactional
    @Modifying
    @Query("UPDATE Session s SET s.revokedByIp = :revokedByIp, s.revokedAt = CURRENT_TIMESTAMP WHERE s.user.id = :userId")
    void updateSessionsByUserId(String userId, String revokedByIp);

    @Transactional
    @Modifying
    @Query("UPDATE Session s SET s.revokedByIp = :revokedByIp, s.revokedAt = CURRENT_TIMESTAMP WHERE s.user.id = :userId AND s.id = :sessionId")
    void updateByUserIdAndId(String userId, String sessionId, String revokedByIp);

    @Transactional
    @Modifying
    @Query("UPDATE Session s SET s.replacedBy = :replacedBy, s.revokedAt = CURRENT_TIMESTAMP WHERE s.user.id = :userId AND s.id = :sessionId")
    void replace(String userId, String sessionId, Session replacedBy);

    @Query("SELECT s FROM Session s WHERE s.user.id =:userId AND s.revokedAt IS NULL AND s.expireAt > CURRENT_TIMESTAMP AND s.replacedBy IS NULL")
    Page<Session> findActiveSessions(String userId, PageRequest pageRequest);

    Page<Session> findAllByUserId(String userId, PageRequest pageRequest);

    @Query("SELECT s FROM Session s WHERE s.id = :id AND s.refreshToken = :refreshToken AND s.revokedAt IS NULL AND s.expireAt > CURRENT_TIMESTAMP AND s.replacedBy IS NULL")
    Optional<Session> findActiveByIdAndRefreshToken(String id, String refreshToken);

    @Query("SELECT COUNT(s) > 0 FROM Session s WHERE s.id = :id AND s.revokedAt IS NULL AND s.expireAt > CURRENT_TIMESTAMP AND s.replacedBy IS NULL")
    boolean existsActiveByIdAndRefreshToken(String id);
}