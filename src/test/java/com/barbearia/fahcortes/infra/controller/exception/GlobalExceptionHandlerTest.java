package com.barbearia.fahcortes.infra.controller.exception;

import com.barbearia.fahcortes.domain.exception.EntidadeNaoEncontradaException;
import com.barbearia.fahcortes.domain.exception.RegraDeNegocioException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new FakeController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(new LocalValidatorFactoryBean())
                .build();
    }

    @RestController
    @RequestMapping("/test-errors")
    static class FakeController {

        record FakeRequest(@NotBlank(message = "O nome não pode ser vazio") String nome) {}

        @GetMapping("/not-found")
        public String notFound() {
            throw new EntidadeNaoEncontradaException("Recurso", 99L);
        }

        @GetMapping("/business")
        public String business() {
            throw new RegraDeNegocioException("Email já cadastrado");
        }

        @GetMapping("/generic")
        public String generic() {
            throw new RuntimeException("Erro inesperado");
        }

        @PostMapping("/validation")
        public String validation(@RequestBody @Valid FakeRequest request) {
            return "ok";
        }
    }

    @Test
    void entidadeNaoEncontrada_deveRetornar404ComCodigo() throws Exception {
        mockMvc.perform(get("/test-errors/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Recurso com id: 99 não encontrado(a)"));
    }

    @Test
    void regraDeNegocio_deveRetornar409ComCodigo() throws Exception {
        mockMvc.perform(get("/test-errors/business"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("CONFLICT"))
                .andExpect(jsonPath("$.message").value("Email já cadastrado"));
    }

    @Test
    void erroGenerico_deveRetornar500ComCodigo() throws Exception {
        mockMvc.perform(get("/test-errors/generic"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value("INTERNAL_SERVER_ERROR"))
                .andExpect(jsonPath("$.message").value("Erro interno do servidor"));
    }

    @Test
    void validacaoInvalida_deveRetornar400ComDetails() throws Exception {
        mockMvc.perform(post("/test-errors/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\": \"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Dados inválidos na requisição"))
                .andExpect(jsonPath("$.details[0].field").value("nome"))
                .andExpect(jsonPath("$.details[0].message").value("O nome não pode ser vazio"));
    }
}
