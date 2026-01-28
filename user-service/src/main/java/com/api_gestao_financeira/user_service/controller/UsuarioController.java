package com.api_gestao_financeira.user_service.controller;

import com.api_gestao_financeira.user_service.dto.LoginDto;
import com.api_gestao_financeira.user_service.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/registrar")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity registrar(@RequestBody LoginDto dto) {
        usuarioService.registrar(dto);
        System.out.println(dto);
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/lote")
    public ResponseEntity<?> registrarEmLote(@RequestParam("file") MultipartFile file) {

        usuarioService.registrarEmLoteCsv(file);
        return ResponseEntity.status(201).build();
    }

}
