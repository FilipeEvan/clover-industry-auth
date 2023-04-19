package br.com.jacto.cloverindustryauth.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import br.com.jacto.cloverindustryauth.model.user.User;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User user) {
        try {
            var algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("Clover Industry Authorization API")
                    .withSubject(user.getEmail())
                    .withClaim("id", String.valueOf(user.getId()))
                    .withClaim("email", user.getEmail())
                    .withExpiresAt(expirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("erro ao gerar token jwt", exception);
        }
    }

    public String getSubject(String tokenJWT) {
        try {
            var algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("Clover Industry Authorization API")
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            exception.printStackTrace();

            throw new RuntimeException("Token JWT inválido ou expirado!");
        }
    }

    private Instant expirationDate() {
        return LocalDateTime.now().plusSeconds(30).toInstant(ZoneOffset.of("-03:00"));
//        return LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.of("-03:00"));
    }

}
