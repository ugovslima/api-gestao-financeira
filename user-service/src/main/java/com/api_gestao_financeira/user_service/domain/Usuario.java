package com.api_gestao_financeira.user_service.domain;

import jakarta.persistence.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Table(name="usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nome;
    private String senha;

    public Usuario() {
    }

    public Usuario(String nome, String senha) {
        this.nome = nome;
        this.senha = new BCryptPasswordEncoder().encode(senha);
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getSenha() {
        return senha;
    }

}
