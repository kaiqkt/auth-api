package com.kaiqkt.auth.application.web.controllers;

import com.kaiqkt.auth.application.web.dto.response.AuthenticationResponse;
import com.kaiqkt.auth.domain.models.Authentication;
import com.kaiqkt.auth.domain.services.AuthenticationService;
import com.kaiqkt.auth.generated.application.controllers.AuthenticationApi;
import com.kaiqkt.auth.generated.application.dto.AuthenticationRequestV1;
import com.kaiqkt.auth.generated.application.dto.AuthenticationResponseV1;
import com.kaiqkt.auth.generated.application.dto.RefreshAuthenticationV1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController implements AuthenticationApi {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public ResponseEntity<AuthenticationResponseV1> authenticate(String userAgent, String xForwardedFor, AuthenticationRequestV1 authenticationRequestV1) throws Exception {
        Authentication authentication = authenticationService.authenticate(authenticationRequestV1.getEmail(), authenticationRequestV1.getPassword(), userAgent, xForwardedFor);

        return ResponseEntity.ok(AuthenticationResponse.toResponse(authentication));
    }

    @PreAuthorize("hasRole('ROLE_API')")
    @Override
    public ResponseEntity<AuthenticationResponseV1> refresh(RefreshAuthenticationV1 refreshAuthenticationV1) throws Exception {
        Authentication authentication = authenticationService.refresh(
                refreshAuthenticationV1.getAccessToken(),
                refreshAuthenticationV1.getRefreshToken(),
                refreshAuthenticationV1.getUserAgent(),
                refreshAuthenticationV1.getForwardedFor()
        );

        return ResponseEntity.ok(AuthenticationResponse.toResponse(authentication));
    }
}
