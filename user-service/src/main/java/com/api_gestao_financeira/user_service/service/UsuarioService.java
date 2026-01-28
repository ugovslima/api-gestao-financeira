package com.api_gestao_financeira.user_service.service;

import com.api_gestao_financeira.user_service.domain.Usuario;
import com.api_gestao_financeira.user_service.dto.LoginDto;
import com.api_gestao_financeira.user_service.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public void registrar(LoginDto loginDto) {
        Usuario usuario = new Usuario(loginDto.nome(), loginDto.senha());
        usuarioRepository.save(usuario);
    }

    public void registrarEmLoteCsv(MultipartFile file) {

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String linha = reader.readLine();

            while ((linha = reader.readLine()) != null) {
                String[] campos = linha.split(",");

                String nome = campos[0].trim();
                String senha = campos[1].trim();

                registrar(new LoginDto(nome, senha));
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar CSV, confira o formato do arquivo.", e);
        }
    }

}
