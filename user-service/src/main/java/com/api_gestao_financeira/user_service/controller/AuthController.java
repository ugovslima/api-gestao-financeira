package com.api_gestao_financeira.user_service.controller;

import com.api_gestao_financeira.user_service.config.security.JwtTokenProvider;
import com.api_gestao_financeira.user_service.domain.UsuarioDetailsImpl;
import com.api_gestao_financeira.user_service.dto.LoginDto;
import com.api_gestao_financeira.user_service.dto.TokenJwtDto;
import com.api_gestao_financeira.user_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuthenticationManager manager;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDto dto) {
        var authToken = new UsernamePasswordAuthenticationToken(dto.nome(), dto.senha());
        var authentication = manager.authenticate(authToken);
        var token = tokenProvider.generateToken((UsuarioDetailsImpl) authentication.getPrincipal());
        return ResponseEntity.ok(new TokenJwtDto(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody LoginDto dto) {
        service.registrar(dto);
        System.out.println(dto);
        return ResponseEntity.ok().build();
    }
}
