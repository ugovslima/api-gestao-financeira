package com.api_gestao_financeira.user_service.controller;

import com.api_gestao_financeira.user_service.dto.LoginDto;
import com.api_gestao_financeira.user_service.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/registrar")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(
            summary = "Registrar usuário",
            description = "Realiza o cadastro de um novo usuário no sistema"
    )
    @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso")
    @PostMapping
    public ResponseEntity registrar(@RequestBody LoginDto dto) {
        usuarioService.registrar(dto);
        System.out.println(dto);
        return ResponseEntity.status(201).build();
    }

    // A CONSERTAR

    @Operation(
            summary = "Registrar usuários em lote",
            description = "Realiza o cadastro de múltiplos usuários via arquivo CSV"
    )
    @ApiResponse(responseCode = "201", description = "Usuários registrados com sucesso")
    @PostMapping(
            value = "/lote",
            consumes = "multipart/form-data"
    )
    public ResponseEntity<Void> registrarEmLote(
            @Parameter(
                    description = "Arquivo CSV contendo usuários",
                    required = true,
                    schema = @Schema(type = "string", format = "binary")
            )
            @RequestParam("file") MultipartFile file
    ) {
        usuarioService.registrarEmLoteCsv(file);
        return ResponseEntity.status(201).build();
    }

}
