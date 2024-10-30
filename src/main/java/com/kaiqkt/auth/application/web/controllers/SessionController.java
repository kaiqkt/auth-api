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

@RestController
public class SessionController implements SessionApi {

    private final SessionService sessionService;

    @Autowired
    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @SuppressWarnings("unchecked")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @Override
    public ResponseEntity<PageResponseV1> findAllByToken(Integer page, Integer size, String sort) throws Exception {
        Page<Session> sessions = sessionService.findAllByUserId(Context.getValue(Constants.USER_ID_KEY, String.class), page, size, sort);

        return ResponseEntity.ok(PageResponse.toResponse(sessions, SessionResponse::toResponse));
    }

    @SuppressWarnings("unchecked")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @Override
    public ResponseEntity<PageResponseV1> findAll(String id, Integer page, Integer size, String sort) throws Exception {
        Page<Session> sessions = sessionService.findAll(id, page, size, sort);

        return ResponseEntity.ok(PageResponse.toResponse(sessions, SessionResponse::toResponse));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @Override
    public ResponseEntity<Void> revoke(String xForwardedFor) {
        sessionService.revoke(Context.getValue(Constants.USER_ID_KEY, String.class), Context.getValue(Constants.SESSION_ID_KEY, String.class), xForwardedFor);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @Override
    public ResponseEntity<Void> revoke(String sessionId, String xForwardedFor) throws Exception {
        sessionService.revokeById(sessionId, xForwardedFor);

        return ResponseEntity.noContent().build();
    }
}
