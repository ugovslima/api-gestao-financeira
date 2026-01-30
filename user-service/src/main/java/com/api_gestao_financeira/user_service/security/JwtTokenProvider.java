package com.api_gestao_financeira.user_service.security;

import com.api_gestao_financeira.user_service.domain.UsuarioDetailsImpl;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtTokenProvider {

    public String generateToken(UsuarioDetailsImpl usuario){
        try {
            Algorithm algorithm = Algorithm.HMAC256("temporario");

            String token = JWT.create()
                    .withIssuer("api-gestao-financeira")
                    .withSubject(usuario.getUsername())
                    .withClaim("id", usuario.getId())
                    .withClaim("name", usuario.getUsername())
                    .withIssuedAt(new Date())
                    .withExpiresAt(validadeToken())
                    .sign(algorithm);

            return token;

        } catch (JWTCreationException e){
            throw new RuntimeException("Erro ao gerar token JWT", e);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("temp");
            return JWT.require(algorithm)
                    .withIssuer("api-gestao-financeira")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception){
            return "Token invalido";
        }
    }

    private Date validadeToken() {
        return Date.from(Instant.now().plus(1, ChronoUnit.HOURS));
    }
}
