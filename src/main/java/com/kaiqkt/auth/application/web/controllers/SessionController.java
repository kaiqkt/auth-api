package com.kaiqkt.auth.application.web.controllers;

import com.kaiqkt.auth.application.web.dto.response.PageResponse;
import com.kaiqkt.auth.application.web.dto.response.SessionResponse;
import com.kaiqkt.auth.domain.models.Session;
import com.kaiqkt.auth.domain.services.SessionService;
import com.kaiqkt.auth.domain.utils.Constants;
import com.kaiqkt.auth.generated.application.controllers.SessionApi;
import com.kaiqkt.auth.generated.application.dto.PageResponseV1;
import com.kaiqkt.springtools.security.utils.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class SessionController implements SessionApi {

    private final SessionService sessionService;

    @Autowired
    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public ResponseEntity<Void> revokeAll(String userId, String xForwardedFor, String sessionId) throws Exception {
        Optional.ofNullable(sessionId)
                .ifPresentOrElse(
                        id -> sessionService.revokeById(userId, id, xForwardedFor),
                        () -> sessionService.revokeAll(userId, xForwardedFor)
                );

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public ResponseEntity<PageResponseV1> findAll(String userId, Integer page, Integer size, String sort) throws Exception {
        Page<Session> sessions = sessionService.findAll(userId, page, size, sort);

        return ResponseEntity.ok(PageResponse.toResponse(sessions, SessionResponse::toResponse));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @Override
    public ResponseEntity<PageResponseV1> findAll(Integer page, Integer size, String sort) throws Exception {
        String userId = Context.getValue(Constants.USER_ID_KEY, String.class);
        Page<Session> sessions = sessionService.findAllActive(userId, page, size, sort);

        return ResponseEntity.ok(PageResponse.toResponse(sessions, SessionResponse::toResponse));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @Override
    public ResponseEntity<Void> revokeAll(String xForwardedFor, String sessionId) throws Exception {
        String userId = Context.getValue(Constants.USER_ID_KEY, String.class);

        Optional.ofNullable(sessionId)
                .ifPresentOrElse(
                        id -> sessionService.revokeById(userId, id, xForwardedFor),
                        () -> sessionService.revokeAll(userId, xForwardedFor)
                );

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @Override
    public ResponseEntity<Void> revoke(String xForwardedFor) throws Exception {
        String userId = Context.getValue(Constants.USER_ID_KEY, String.class);
        String sessionId = Context.getValue(Constants.SESSION_ID_KEY, String.class);

        sessionService.revokeById(userId, sessionId, xForwardedFor);

        return ResponseEntity.noContent().build();
    }
}
