package com.barbearia.fahcortes.infra.controller;

import com.barbearia.fahcortes.domain.entities.usuario.Usuario;
import com.barbearia.fahcortes.domain.entities.usuario.UsuarioEnum;
import com.barbearia.fahcortes.domain.exception.EntidadeNaoEncontradaException;
import com.barbearia.fahcortes.domain.usecases.usuario.*;
import com.barbearia.fahcortes.infra.config.CorsConfig;
import com.barbearia.fahcortes.infra.config.SecurityConfig;
import com.barbearia.fahcortes.infra.controller.usuario.UsuarioController;
import com.barbearia.fahcortes.infra.controller.usuario.dtos.UsuarioRequestDto;
import com.barbearia.fahcortes.infra.controller.usuario.dtos.UsuarioResponseDto;
import com.barbearia.fahcortes.infra.mapper.usuario.UsuarioMapper;
import com.barbearia.fahcortes.infra.persistence.UsuarioRepository;
import com.barbearia.fahcortes.infra.security.TokenService;
import com.barbearia.fahcortes.infra.security.UsuarioDetailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
@Import({SecurityConfig.class, CorsConfig.class})
class UsuarioControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private UsuarioMapper usuarioMapper;
    @MockBean private CadastrarUsuarioUseCase cadastrarUsuarioUseCase;
    @MockBean private ListarTodosUsuariosUseCase listarTodosUsuariosUseCase;
    @MockBean private BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase;
    @MockBean private DeletarUsuarioPorIdUseCase deletarUsuarioPorIdUseCase;
    @MockBean private AtualizarUsuarioPorIdUseCase atualizarUsuarioPorIdUseCase;
    @MockBean private BuscarUsuarioPorEmailUseCase buscarUsuarioPorEmailUseCase;
    @MockBean private TokenService tokenService;
    @MockBean private UsuarioDetailService usuarioDetailService;
    @MockBean private UsuarioRepository usuarioRepository;

    private Usuario usuario(Long id) {
        return new Usuario(id, "João Silva", "joao@email.com", "senha123", UsuarioEnum.USER);
    }

    private UsuarioResponseDto responseDto(Long id) {
        UsuarioResponseDto dto = new UsuarioResponseDto();
        dto.setId(id);
        dto.setNome("João Silva");
        dto.setEmail("joao@email.com");
        dto.setRole(UsuarioEnum.USER);
        return dto;
    }

    @Test
    void cadastrar_semAutenticacao_deveRetornar201() throws Exception {
        String body = """
                {"nome":"João Silva","email":"joao@email.com","senha":"senha123"}
                """;
        Usuario domain = usuario(null);
        Usuario salvo = usuario(1L);
        UsuarioResponseDto resposta = responseDto(1L);

        when(usuarioMapper.toDomain(any(UsuarioRequestDto.class))).thenReturn(domain);
        when(cadastrarUsuarioUseCase.execute(domain)).thenReturn(salvo);
        when(usuarioMapper.toResponse(salvo)).thenReturn(resposta);

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("João Silva"));
    }

    @Test
    @WithMockUser
    void buscarPorId_deveRetornar200() throws Exception {
        Usuario u = usuario(1L);
        UsuarioResponseDto resposta = responseDto(1L);

        when(buscarUsuarioPorIdUseCase.execute(1L)).thenReturn(u);
        when(usuarioMapper.toResponse(u)).thenReturn(resposta);

        mockMvc.perform(get("/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Silva"));
    }

    @Test
    @WithMockUser
    void buscarPorEmail_deveRetornar200() throws Exception {
        Usuario u = usuario(1L);
        UsuarioResponseDto resposta = responseDto(1L);

        when(buscarUsuarioPorEmailUseCase.execute("joao@email.com")).thenReturn(u);
        when(usuarioMapper.toResponse(u)).thenReturn(resposta);

        mockMvc.perform(get("/usuarios/email").param("email", "joao@email.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("joao@email.com"));
    }

    @Test
    @WithMockUser
    void listarTodos_deveRetornar200() throws Exception {
        List<Usuario> lista = List.of(usuario(1L));
        List<UsuarioResponseDto> resposta = List.of(responseDto(1L));

        when(listarTodosUsuariosUseCase.execute()).thenReturn(lista);
        when(usuarioMapper.toResponseDtoList(lista)).thenReturn(resposta);

        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("João Silva"));
    }

    @Test
    @WithMockUser
    void deletar_deveRetornar204() throws Exception {
        doNothing().when(deletarUsuarioPorIdUseCase).execute(1L);

        mockMvc.perform(delete("/usuarios/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void atualizar_deveRetornar200() throws Exception {
        String body = """
                {"nome":"João Atualizado","email":"joao@email.com","senha":"novaSenha"}
                """;
        Usuario domain = usuario(null);
        Usuario atualizado = usuario(1L);
        UsuarioResponseDto resposta = responseDto(1L);

        when(usuarioMapper.toDomain(any(UsuarioRequestDto.class))).thenReturn(domain);
        when(atualizarUsuarioPorIdUseCase.execute(domain, 1L)).thenReturn(atualizado);
        when(usuarioMapper.toResponse(atualizado)).thenReturn(resposta);

        mockMvc.perform(put("/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Silva"));
    }

    @Test
    void buscarPorId_semAutenticacao_deveRetornar403() throws Exception {
        mockMvc.perform(get("/usuarios/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void buscarPorId_quandoNaoExiste_deveRetornar404() throws Exception {
        when(buscarUsuarioPorIdUseCase.execute(99L))
                .thenThrow(new EntidadeNaoEncontradaException("Usuário", 99L));

        mockMvc.perform(get("/usuarios/99"))
                .andExpect(status().isNotFound());
    }
}
