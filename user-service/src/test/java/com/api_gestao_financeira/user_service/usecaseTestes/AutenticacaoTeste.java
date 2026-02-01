package com.api_gestao_financeira.user_service.usecaseTestes;

import com.api_gestao_financeira.user_service.domain.Usuario;
import org.springframework.security.core.userdetails.UserDetails;
import com.api_gestao_financeira.user_service.domain.UsuarioDetailsImpl;
import com.api_gestao_financeira.user_service.dto.LoginDto;
import com.api_gestao_financeira.user_service.repository.UsuarioRepository;
import com.api_gestao_financeira.user_service.service.AuthService;
import com.api_gestao_financeira.user_service.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AutenticacaoTeste {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @InjectMocks
    private AuthService authService;

    @Test
    void deveRegistrarUsuarioComSucesso() {
        LoginDto dto = new LoginDto("usuario", "123456");

        usuarioService.registrar(dto);

        ArgumentCaptor<Usuario> captor =
                ArgumentCaptor.forClass(Usuario.class);

        verify(usuarioRepository).save(captor.capture());

        Usuario usuarioSalvo = captor.getValue();

        assertEquals("usuario", usuarioSalvo.getNome());
        assertNotNull(usuarioSalvo.getSenha());
        assertNotEquals("123456", usuarioSalvo.getSenha());
    }

    @Test
    void deveRetornarUsuarioAoBuscarPorNome() {
        Usuario usuario =
                new Usuario("usuarioTeste", "SENHA_CRIPTOGRAFADA");

        when(usuarioRepository.findByNome("usuarioTeste"))
                .thenReturn(Optional.of(usuario));

        UserDetails userDetails =
                authService.loadUserByUsername("usuarioTeste");

        assertNotNull(userDetails);
        assertInstanceOf(UsuarioDetailsImpl.class, userDetails);
        assertEquals("usuarioTeste", userDetails.getUsername());
        assertEquals(usuario.getSenha(), userDetails.getPassword());
    }

}