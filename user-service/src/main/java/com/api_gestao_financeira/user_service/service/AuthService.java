package com.api_gestao_financeira.user_service.service;

import com.api_gestao_financeira.user_service.domain.UsuarioDetailsImpl;
import com.api_gestao_financeira.user_service.domain.Usuario;
import com.api_gestao_financeira.user_service.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String login) {
        Usuario usuario = usuarioRepository.findByNome(login)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
        return new UsuarioDetailsImpl(usuario);
    }
}
