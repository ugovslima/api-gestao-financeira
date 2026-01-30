package com.api_gestao_financeira.user_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Token JWT retornado após autenticação",
        example = """
    {
      "tokenJwt": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1Z28ubGltYSIsImlhdCI6MT..."
    }
    """
)
public record TokenJwtDto(String tokenJwt){
}
