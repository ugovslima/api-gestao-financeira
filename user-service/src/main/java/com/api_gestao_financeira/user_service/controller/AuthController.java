package com.api_gestao_financeira.user_service.controller;

import com.api_gestao_financeira.user_service.security.JwtTokenProvider;
import com.api_gestao_financeira.user_service.domain.UsuarioDetailsImpl;
import com.api_gestao_financeira.user_service.dto.LoginDto;
import com.api_gestao_financeira.user_service.dto.TokenJwtDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Autenticação", description = "Endpoints responsáveis por autenticação e geração de JWT")
@RestController
@RequestMapping("/login")
public class AuthController {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuthenticationManager manager;

    @Operation(
            summary = "Autenticar usuário",
            description = "Realiza login e retorna um token JWT para autenticação nos outros serviços."
    )
    @ApiResponse(responseCode = "200", description = "Login criado com sucesso")
    @ApiResponse(content = @Content(schema = @Schema(implementation = TokenJwtDto.class)))
    @PostMapping()
    public ResponseEntity login(@RequestBody LoginDto dto) {
        var authToken = new UsernamePasswordAuthenticationToken(dto.nome(), dto.senha());
        var authentication = manager.authenticate(authToken);
        var token = tokenProvider.generateToken((UsuarioDetailsImpl) authentication.getPrincipal());
        return ResponseEntity.ok(new TokenJwtDto(token));
    }
}
