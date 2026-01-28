package com.api_gestao_financeira.user_service.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> tratarViolacaoIntegridade(DataIntegrityViolationException ex) {
        String mensagem = "Dado invalido ou duplicado.";

        if (ex.getCause() != null) {
            String causaMsg = ex.getCause().getMessage();
            if (causaMsg.contains("duplicate key")) {
                Pattern pattern = Pattern.compile("\\(nome\\)=\\((.*?)\\)");
                Matcher matcher = pattern.matcher(causaMsg);
                String nomeDuplicado = matcher.find() ? matcher.group(1) : "desconhecido";

                mensagem = "Usuário com o nome '" + nomeDuplicado + "' já existe.";
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("mensagem", mensagem));
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("mensagem", mensagem));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntime(RuntimeException ex) {
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
    }
}
