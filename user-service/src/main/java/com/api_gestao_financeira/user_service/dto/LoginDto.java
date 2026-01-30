package com.api_gestao_financeira.user_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Dados para autenticação",
        example = """
    {
      "nome": "Ugo",
      "senha": "1234"
    }
    """
)
public record LoginDto(String nome, String senha) {
}
