package com.barbearia.fahcortes.infra.controller;

import com.barbearia.fahcortes.domain.entities.barbeiro.Barbeiro;
import com.barbearia.fahcortes.domain.exception.EntidadeNaoEncontradaException;
import com.barbearia.fahcortes.domain.usecases.barbeiro.*;
import com.barbearia.fahcortes.infra.config.CorsConfig;
import com.barbearia.fahcortes.infra.config.SecurityConfig;
import com.barbearia.fahcortes.infra.controller.barbeiro.BarbeiroController;
import com.barbearia.fahcortes.infra.controller.barbeiro.dtos.BarbeiroRequestDto;
import com.barbearia.fahcortes.infra.controller.barbeiro.dtos.BarbeiroResponseDto;
import com.barbearia.fahcortes.infra.mapper.barbeiro.BarbeiroMapper;
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

@WebMvcTest(BarbeiroController.class)
@Import({SecurityConfig.class, CorsConfig.class})
class BarbeiroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BarbeiroMapper barbeiroMapper;
    @MockBean
    private CadastrarBarbeiroUseCase cadastrarBarbeiroUseCase;
    @MockBean
    private BuscarBarbeiroPorIdUseCase buscarBarbeiroPorIdUseCase;
    @MockBean
    private ListarTodosBarbeirosUseCase listarTodosBarbeirosUseCase;
    @MockBean
    private AtualizarBarbeiroPorIdUseCase atualizarBarbeiroPorIdUseCase;
    @MockBean
    private DeletarBarbeiroPorIdUseCase deletarBarbeiroPorIdUseCase;
    @MockBean
    private TokenService tokenService;
    @MockBean
    private UsuarioDetailService usuarioDetailService;
    @MockBean
    private UsuarioRepository usuarioRepository;

    private BarbeiroResponseDto mockResponse(Long id, String nome) {
        BarbeiroResponseDto dto = new BarbeiroResponseDto();
        dto.setId(id);
        dto.setNome(nome);
        dto.setEspecialidade("Fade");
        dto.setCurtidas(0);
        dto.setAtivo(true);
        return dto;
    }

    @Test
    void listarTodos_deveRetornar200ComLista() throws Exception {
        List<Barbeiro> barbeiros = List.of(new Barbeiro(1L, "João", "Fade", null, 0, true));
        List<BarbeiroResponseDto> resposta = List.of(mockResponse(1L, "João"));

        when(listarTodosBarbeirosUseCase.execute()).thenReturn(barbeiros);
        when(barbeiroMapper.toResponseList(barbeiros)).thenReturn(resposta);

        mockMvc.perform(get("/barbeiros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nome").value("João"));
    }

    @Test
    void buscarPorId_deveRetornar200() throws Exception {
        Barbeiro barbeiro = new Barbeiro(1L, "João", "Fade", null, 5, true);
        BarbeiroResponseDto resposta = mockResponse(1L, "João");

        when(buscarBarbeiroPorIdUseCase.execute(1L)).thenReturn(barbeiro);
        when(barbeiroMapper.toResponse(barbeiro)).thenReturn(resposta);

        mockMvc.perform(get("/barbeiros/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("João"));
    }

    @Test
    void buscarPorId_quandoNaoExiste_deveRetornar404() throws Exception {
        when(buscarBarbeiroPorIdUseCase.execute(99L))
                .thenThrow(new EntidadeNaoEncontradaException("Barbeiro", 99L));

        mockMvc.perform(get("/barbeiros/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void cadastrar_comoAdmin_deveRetornar201() throws Exception {
        String body = """
                {"nome":"Carlos","especialidade":"Navalhado","curtidas":0,"ativo":true}
                """;
        Barbeiro domain = new Barbeiro(null, "Carlos", "Navalhado", null, 0, true);
        Barbeiro salvo = new Barbeiro(1L, "Carlos", "Navalhado", null, 0, true);
        BarbeiroResponseDto resposta = mockResponse(1L, "Carlos");

        when(barbeiroMapper.toDomain(any(BarbeiroRequestDto.class))).thenReturn(domain);
        when(cadastrarBarbeiroUseCase.execute(domain)).thenReturn(salvo);
        when(barbeiroMapper.toResponse(salvo)).thenReturn(resposta);

        mockMvc.perform(post("/barbeiros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Carlos"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void cadastrar_comoUser_deveRetornar403() throws Exception {
        String body = """
                {"nome":"Carlos","especialidade":"Navalhado"}
                """;

        mockMvc.perform(post("/barbeiros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deletar_comoAdmin_deveRetornar204() throws Exception {
        doNothing().when(deletarBarbeiroPorIdUseCase).execute(1L);

        mockMvc.perform(delete("/barbeiros/1"))
                .andExpect(status().isNoContent());
    }
}
