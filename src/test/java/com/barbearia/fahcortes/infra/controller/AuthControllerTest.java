package com.barbearia.fahcortes.infra.controller;

import com.barbearia.fahcortes.domain.entities.usuario.UsuarioEnum;
import com.barbearia.fahcortes.infra.config.CorsConfig;
import com.barbearia.fahcortes.infra.config.SecurityConfig;
import com.barbearia.fahcortes.infra.controller.usuario.AuthController;
import com.barbearia.fahcortes.infra.entities.UsuarioEntity;
import com.barbearia.fahcortes.infra.persistence.UsuarioRepository;
import com.barbearia.fahcortes.infra.security.TokenService;
import com.barbearia.fahcortes.infra.security.UsuarioDetailService;
import com.barbearia.fahcortes.infra.security.UsuarioDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import({SecurityConfig.class, CorsConfig.class})
class AuthControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private AuthenticationManager authenticationManager;
    @MockBean private TokenService tokenService;
    @MockBean private UsuarioDetailService usuarioDetailService;
    @MockBean private UsuarioRepository usuarioRepository;

    @Test
    void login_valido_deveRetornar200ComToken() throws Exception {
        UsuarioEntity entity = new UsuarioEntity(1L, "Admin", "admin@email.com", "encoded", UsuarioEnum.ADMIN);
        UsuarioDetails details = new UsuarioDetails(entity);

        Authentication authResult = mock(Authentication.class);
        when(authResult.getPrincipal()).thenReturn(details);
        when(authenticationManager.authenticate(any())).thenReturn(authResult);
        when(tokenService.gerarToken(entity)).thenReturn("jwt-token");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"admin@email.com\",\"senha\":\"senha123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    void login_invalido_deveRetornar401() throws Exception {
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Credenciais inválidas"));

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"wrong@email.com\",\"senha\":\"errada\"}"))
                .andExpect(status().isUnauthorized());
    }
}
