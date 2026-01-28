package com.api_gestao_financeira.transaction_api.infra.config;

import com.api_gestao_financeira.transaction_api.infra.gateway.ProvedorTokenJwt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final ProvedorTokenJwt provedorTokenJwt;

    public SecurityConfig(ProvedorTokenJwt provedorTokenJwt) {
        this.provedorTokenJwt = provedorTokenJwt;
    }

    @Bean
    public SecurityFilterChain filtroSeguranca(HttpSecurity http) throws Exception {
        FiltroAuthJwt filtroJwt = new FiltroAuthJwt(provedorTokenJwt);
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(autorizacoes -> autorizacoes.anyRequest().authenticated())
                .addFilterBefore(filtroJwt, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
