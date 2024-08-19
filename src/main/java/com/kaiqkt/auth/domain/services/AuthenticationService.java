package com.kaiqkt.auth.domain.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.kaiqkt.auth.domain.exceptions.DomainException;
import com.kaiqkt.auth.domain.exceptions.ErrorType;
import com.kaiqkt.auth.domain.models.Authentication;
import com.kaiqkt.auth.domain.models.Credential;
import com.kaiqkt.auth.domain.models.Role;
import com.kaiqkt.auth.domain.models.Session;
import com.kaiqkt.auth.domain.models.User;
import com.kaiqkt.auth.domain.models.enums.Status;
import com.kaiqkt.auth.domain.utils.TokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class AuthenticationService {

    @Value("${springtools.jwt-secret}")
    private String jwtSecret;
    @Value("${access-token-expiration}")
    private long accessTokenExpiration;
    @Value("${refresh-token-secret}")
    private String refreshTokenSecret;
    private final UserService userService;
    private final CredentialService credentialService;
    private final SessionService sessionService;
    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    @Autowired
    public AuthenticationService(UserService userService, CredentialService credentialService, SessionService sessionService) {
        this.userService = userService;
        this.credentialService = credentialService;
        this.sessionService = sessionService;
    }

    public Authentication authenticate(String email, String password, String userAgent, String ip) throws Exception {
        Credential credential = credentialService.findByUserEmail(email);

        if (!BCrypt.checkpw(password, credential.getHash())) {
            throw new DomainException(ErrorType.INVALID_CREDENTIAL);
        }

        if (credential.getUser().getStatus() == Status.BLOCKED) {
            throw new DomainException(ErrorType.USER_BLOCKED);
        }

        if (credential.getUser().getStatus() == Status.WAITING_EMAIL_VERIFICATION) {
            //do the email verification
            throw new DomainException(ErrorType.WAITING_EMAIL_VERIFICATION);
        }

        Session session = sessionService.create(credential.getUser(), userAgent, ip);
        String accessToken = generateToken(credential.getUser(), session.getId());

        log.info("User {} authenticated successfully", credential.getUser().getId());

        return new Authentication(accessToken, session.getRefreshToken(), credential.getUser().getId(), session.getExpireAt());
    }

    public Authentication refresh(String accessToken, String refreshToken, String userAgent, String ip) throws Exception {
        DecodedJWT jwt = getDecodedJwt(accessToken);
        Map<String, Claim> claims = jwt.getClaims();
        String sessionId = claims.get("session_id").asString();
        Session session = sessionService.findByIdAndRefreshToken(sessionId, refreshToken);

        if (jwt.getExpiresAt().after(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))) {
           return null;
        }

        if (TokenGenerator.validate(sessionId, refreshToken, refreshTokenSecret)) {
            Session newSession = sessionService.create(session.getUser(), userAgent, ip);
            sessionService.replace(session.getUser().getId(), session.getId(), newSession);
            String newAccessToken = generateToken(newSession.getUser(), newSession.getId());

            log.info("Session {} refreshed successfully", session.getId());

            return new Authentication(newAccessToken, refreshToken, session.getUser().getId(), session.getExpireAt());
        }

        throw new DomainException(ErrorType.INVALID_REFRESH_TOKEN);
    }


    private String generateToken(User user, String sessionId) {
        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .toList();

        return JWT.create()
                .withIssuer("auth-api")
                .withClaim("user_id", user.getId())
                .withClaim("session_id", sessionId)
                .withClaim("roles", roles)
                .withExpiresAt(LocalDateTime.now().plusMinutes(accessTokenExpiration).atZone(ZoneId.systemDefault()).toInstant())
                .sign(Algorithm.HMAC256(jwtSecret));
    }

    public DecodedJWT getDecodedJwt(String token) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
        JWTVerifier verifier = JWT.require(algorithm)
                .acceptExpiresAt(0) // Accept any expiration time
                .build();

        return verifier.verify(token);
    }
}
