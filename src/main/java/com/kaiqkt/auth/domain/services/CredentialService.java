package com.kaiqkt.auth.domain.services;

import com.kaiqkt.auth.domain.exceptions.DomainException;
import com.kaiqkt.auth.domain.exceptions.ErrorType;
import com.kaiqkt.auth.domain.models.Credential;
import com.kaiqkt.auth.domain.models.User;
import com.kaiqkt.auth.domain.repositories.CredentialRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CredentialService {

    private final CredentialRepository credentialRepository;
    private static final Logger log = LoggerFactory.getLogger(CredentialService.class);

    @Autowired
    public CredentialService(CredentialRepository credentialRepository) {
        this.credentialRepository = credentialRepository;
    }

    public void create(User user, String password) throws Exception {
        String hash = BCrypt.hashpw(password, BCrypt.gensalt());
        Credential credential = new Credential(user, hash);

        credentialRepository.save(credential);

        log.info("Credential created for user: {} successfully", user.getId());
    }

    public void change(String userId, String password, String newPassword) throws DomainException {
        Credential credential = credentialRepository.findByUserId(userId).orElseThrow(() -> new DomainException(ErrorType.USER_NOT_FOUND));

        if (!BCrypt.checkpw(password, credential.getHash())) {
            throw new DomainException(ErrorType.INVALID_CREDENTIAL);
        }

        String hash = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        credentialRepository.updateHash(userId, hash);

        log.info("Credential changed for user: {} successfully", userId);
    }

    public Credential findByUserEmail(String email) throws DomainException {
        return credentialRepository.findByUserEmail(email).orElseThrow(() -> new DomainException(ErrorType.USER_NOT_FOUND));
    }

    public Credential findByUserId(String userId) throws DomainException {
        return credentialRepository.findByUserId(userId).orElseThrow(() -> new DomainException(ErrorType.USER_NOT_FOUND));
    }
}
