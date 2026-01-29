package com.api_gestao_financeira.transaction_api.infra.exception;

import java.time.LocalDateTime;

public record ErrorResponse(

        LocalDateTime timestamp,
        int status,
        String error,
        String message
) {}
