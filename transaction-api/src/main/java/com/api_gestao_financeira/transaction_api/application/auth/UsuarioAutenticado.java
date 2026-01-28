package com.api_gestao_financeira.transaction_api.application.auth;

public class UsuarioAutenticado {

    private final Long id;
    private final String nome;


    public UsuarioAutenticado(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}
