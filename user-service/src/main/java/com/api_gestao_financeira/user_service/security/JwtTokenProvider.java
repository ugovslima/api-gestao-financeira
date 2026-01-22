package com.api_gestao_financeira.user_service.security;

import com.api_gestao_financeira.user_service.domain.UsuarioDetailsImpl;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenProvider {

    public String generateToken(UsuarioDetailsImpl usuario){
        try {
            Algorithm algorithm = Algorithm.HMAC256("temporario");
            String token = JWT.create()
                    .withIssuer("api-gestao-financeira")
                    .withSubject(usuario.getUsername())
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException e){
            throw new RuntimeException(e);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("temporario");
            return JWT.require(algorithm)
                    .withIssuer("api-gestao-financeira")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception){
            return "invalide token";
        }
    }
}
