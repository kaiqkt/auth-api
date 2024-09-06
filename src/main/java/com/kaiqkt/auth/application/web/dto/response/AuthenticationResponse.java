package com.kaiqkt.auth.application.web.dto.response;

import com.kaiqkt.auth.domain.models.Authentication;
import com.kaiqkt.auth.generated.application.dto.AuthenticationResponseV1;

public class AuthenticationResponse {

    public static AuthenticationResponseV1 toResponse(Authentication authentication) {
        AuthenticationResponseV1 response = new AuthenticationResponseV1();

        response.setAccessToken(authentication.getAccessToken());
        response.setRefreshToken(authentication.getRefreshToken());
        response.setUserId(authentication.getUserId());
        response.setAccessTokenExpiration(authentication.getAccessTokenExpiration().toString());

        return response;
    }
}
