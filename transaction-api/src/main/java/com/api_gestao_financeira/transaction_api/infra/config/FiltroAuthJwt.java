package com.api_gestao_financeira.transaction_api.infra.config;

import com.api_gestao_financeira.transaction_api.application.auth.UsuarioAutenticado;
import com.api_gestao_financeira.transaction_api.infra.gateway.ProvedorTokenJwt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class FiltroAuthJwt extends OncePerRequestFilter {

    private final ProvedorTokenJwt provedorTokenJwt;

    public FiltroAuthJwt(ProvedorTokenJwt provedorTokenJwt) {
        this.provedorTokenJwt = provedorTokenJwt;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String cabecalho = request.getHeader("Authorization");

        if (cabecalho != null && cabecalho.startsWith("Bearer ")) {
            String token = cabecalho.substring(7);

            UsuarioAutenticado usuario = provedorTokenJwt.validarUsuario(token);

            UsernamePasswordAuthenticationToken autenticacao =
                    new UsernamePasswordAuthenticationToken(
                            usuario, null, Collections.emptyList()
                    );

            SecurityContextHolder.getContext().setAuthentication(autenticacao);
        }

        filterChain.doFilter(request, response);
    }
}