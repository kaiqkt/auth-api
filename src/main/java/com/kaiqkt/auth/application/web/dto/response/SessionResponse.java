package com.kaiqkt.auth.application.web.dto.response;

import com.kaiqkt.auth.domain.models.Session;
import com.kaiqkt.auth.generated.application.dto.SessionResponseV1;


public class SessionResponse {

    public static SessionResponseV1 toResponse(Session session) {
        var responseV1 = new SessionResponseV1();
        responseV1.setId(session.getId());
        responseV1.setCreatedByIp(session.getCreatedByIp());
        responseV1.setRevokedByIp(session.getRevokedByIp());

        if (session.getReplacedBy() != null) {
            responseV1.setReplacedBy(toResponse(session.getReplacedBy()));
        }

        responseV1.setUserId(session.getUser().getId());
        responseV1.setUserAgent(session.getUserAgent());
        responseV1.setExpireAt(session.getExpireAt() != null ? session.getExpireAt().toString(): null);
        responseV1.setCreatedAt(session.getCreatedAt() != null ? session.getCreatedAt().toString(): null);
        responseV1.setRevokedAt(session.getRevokedAt() != null ? session.getRevokedAt().toString(): null);

        return responseV1;
    }
}
