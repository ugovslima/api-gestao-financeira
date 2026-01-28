package com.api_gestao_financeira.transaction_api.application.gateway;

import com.api_gestao_financeira.transaction_api.application.auth.UsuarioAutenticado;

public interface ProvedorToken {
    UsuarioAutenticado validarUsuario(String token);
}
