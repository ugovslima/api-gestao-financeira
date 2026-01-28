package com.api_gestao_financeira.transaction_api.infra.gateway;

import com.api_gestao_financeira.transaction_api.application.auth.UsuarioAutenticado;
import com.api_gestao_financeira.transaction_api.application.gateway.ProvedorToken;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.stereotype.Component;

@Component
public class ProvedorTokenJwt implements ProvedorToken {

    private static final String SEGREDO = "temporario";
    private final Algorithm algoritmo;

    public ProvedorTokenJwt() {
        this.algoritmo = Algorithm.HMAC256(SEGREDO);
    }

    @Override
    public UsuarioAutenticado validarUsuario(String token) {
        try {
            DecodedJWT jwt = JWT.require(algoritmo)
                    .withIssuer("api-gestao-financeira")
                    .build()
                    .verify(token);

            Long id = jwt.getClaim("id").asLong();
            String nome = jwt.getClaim("name").asString();

            return new UsuarioAutenticado(id, nome);
        } catch (JWTVerificationException ex) {
            throw new RuntimeException("Token inválido", ex);
        }
    }
}