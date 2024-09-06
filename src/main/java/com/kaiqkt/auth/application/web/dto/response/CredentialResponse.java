package com.kaiqkt.auth.application.web.dto.response;

import com.kaiqkt.auth.domain.models.Credential;
import com.kaiqkt.auth.generated.application.dto.CredentialResponseV1;

public class CredentialResponse {

    public static CredentialResponseV1 toResponse(Credential credential){
        CredentialResponseV1 response = new CredentialResponseV1();
        response.setId(credential.getId());
        response.setUserId(credential.getUser().getId());
        response.setCreatedAt(credential.getCreatedAt().toString());
        response.setUpdatedAt(credential.getUpdatedAt() != null ? credential.getUpdatedAt().toString(): null);
        return response;
    }
}
