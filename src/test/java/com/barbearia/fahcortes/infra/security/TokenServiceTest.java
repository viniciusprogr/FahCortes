package com.barbearia.fahcortes.infra.security;

import com.barbearia.fahcortes.domain.entities.usuario.UsuarioEnum;
import com.barbearia.fahcortes.infra.entities.UsuarioEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.*;

class TokenServiceTest {

    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService();
        ReflectionTestUtils.setField(tokenService, "secret", "test-secret-key-for-unit-tests");
        ReflectionTestUtils.setField(tokenService, "expiration", 7200000L);
    }

    @Test
    void gerarToken_deveRetornarTokenNaoNulo() {
        UsuarioEntity usuario = new UsuarioEntity(1L, "Maria", "maria@email.com", "hash", UsuarioEnum.USER);

        String token = tokenService.gerarToken(usuario);

        assertThat(token).isNotNull().isNotBlank();
    }

    @Test
    void validarToken_comTokenValido_deveRetornarEmail() {
        UsuarioEntity usuario = new UsuarioEntity(1L, "Maria", "maria@email.com", "hash", UsuarioEnum.USER);
        String token = tokenService.gerarToken(usuario);

        String email = tokenService.validarToken(token);

        assertThat(email).isEqualTo("maria@email.com");
    }

    @Test
    void validarToken_comTokenInvalido_deveRetornarStringVazia() {
        String email = tokenService.validarToken("token.invalido.qualquer");

        assertThat(email).isEmpty();
    }

    @Test
    void validarToken_comTokenVazio_deveRetornarStringVazia() {
        String email = tokenService.validarToken("");

        assertThat(email).isEmpty();
    }

    @Test
    void gerarToken_doisTokensParaMesmoUsuario_devemSerDiferentes() throws InterruptedException {
        UsuarioEntity usuario = new UsuarioEntity(1L, "Maria", "maria@email.com", "hash", UsuarioEnum.USER);

        String token1 = tokenService.gerarToken(usuario);
        Thread.sleep(1100);
        String token2 = tokenService.gerarToken(usuario);

        assertThat(token1).isNotEqualTo(token2);
    }
}
