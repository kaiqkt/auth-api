package com.kaiqkt.auth.domain.services;

import com.kaiqkt.auth.domain.exceptions.DomainException;
import com.kaiqkt.auth.domain.exceptions.ErrorType;
import com.kaiqkt.auth.domain.models.Session;
import com.kaiqkt.auth.domain.models.User;
import com.kaiqkt.auth.domain.repositories.SessionRepository;
import com.kaiqkt.auth.domain.utils.Pageable;
import com.kaiqkt.auth.domain.utils.TokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SessionService {

    @Value("${session-expiration}")
    private Integer expiration;
    @Value("${refresh-token-secret}")
    private String refreshTokenSecret;
    private final SessionRepository sessionRepository;
    private static final Logger log = LoggerFactory.getLogger(SessionService.class);

    @Autowired
    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public Session create(User user, String userAgent, String ip) throws Exception {
        Session session = new Session(user, LocalDateTime.now().plusDays(expiration), userAgent, ip);
        String refreshToken = TokenGenerator.generate(session.getId(), refreshTokenSecret);
        session.setRefreshToken(refreshToken);

        sessionRepository.save(session);

        log.info("Session {} for user {} created successfully", session.getId(), user.getId());

        return session;
    }

    public void revoke(String userId, String sessionId, String ip) {
        sessionRepository.updateByUserIdAndId(userId, sessionId, ip);
        log.info("Revoked session {} of user {} successfully", sessionId, userId);
    }

    public void revokeById(String sessionId, String ip) {
        sessionRepository.updateById(sessionId, ip);
        log.info("Revoked session {} successfully", sessionId);
    }

    public void replace(String userId, String sessionId, Session newSessionId) {
        sessionRepository.replace(userId, sessionId, newSessionId);
        log.info("Replaced session {} of user {} to session {} successfully", sessionId, userId, newSessionId);
    }

    public Page<Session> findAllByUserId(String userId, Integer page, Integer size, String sort) {
        return sessionRepository.findAllByUserId(userId, Pageable.getPageRequest(page, size, sort));
    }

    public Page<Session> findAll(String id, Integer page, Integer size, String sort) {
        if (id == null || id.isEmpty()) {
            return sessionRepository.findAll(Pageable.getPageRequest(page, size, sort));
        }

        return sessionRepository.findAllBySessionIdOrUserId(id, Pageable.getPageRequest(page, size, sort));
    }

    public Session findByIdAndRefreshToken(String sessionId, String refreshToken) throws DomainException {
        return sessionRepository.findActiveByIdAndRefreshToken(sessionId, refreshToken).orElseThrow(() -> new DomainException(ErrorType.SESSION_NOT_FOUND));
    }
}
