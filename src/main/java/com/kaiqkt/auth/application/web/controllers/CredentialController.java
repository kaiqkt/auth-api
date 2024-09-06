package com.kaiqkt.auth.application.web.controllers;

import com.kaiqkt.auth.application.web.dto.response.CredentialResponse;
import com.kaiqkt.auth.domain.models.Credential;
import com.kaiqkt.auth.domain.services.CredentialService;
import com.kaiqkt.auth.domain.utils.Constants;
import com.kaiqkt.auth.generated.application.controllers.CredentialApi;
import com.kaiqkt.auth.generated.application.dto.CredentialRequestV1;
import com.kaiqkt.auth.generated.application.dto.CredentialResponseV1;
import com.kaiqkt.springtools.security.utils.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CredentialController implements CredentialApi {

    private final CredentialService credentialService;

    @Autowired
    public CredentialController(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @Override
    public ResponseEntity<CredentialResponseV1> findByToken() throws Exception {
        String userId = Context.getValue(Constants.USER_ID_KEY, String.class);
        Credential credential = credentialService.findByUserId(userId);

        return ResponseEntity.ok(CredentialResponse.toResponse(credential));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public ResponseEntity<CredentialResponseV1> findByUserId(String userId) throws Exception {
        Credential credential = credentialService.findByUserId(userId);

        return ResponseEntity.ok(CredentialResponse.toResponse(credential));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @Override
    public ResponseEntity<Void> change(CredentialRequestV1 credentialRequestV1) throws Exception {
        String userId = Context.getValue(Constants.USER_ID_KEY, String.class);
        credentialService.change(userId, credentialRequestV1.getPassword(), credentialRequestV1.getNewPassword());
        return ResponseEntity.noContent().build();
    }
}
