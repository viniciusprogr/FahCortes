package com.barbearia.fahcortes.infra.controller.usuario;

import com.barbearia.fahcortes.infra.controller.usuario.dtos.LoginRequestDto;
import com.barbearia.fahcortes.infra.persistence.UsuarioRepository;
import com.barbearia.fahcortes.infra.security.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class AuthController {
    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;
    private final TokenService tokenService;

    public AuthController(UsuarioRepository repository, PasswordEncoder encoder, TokenService tokenService) {
        this.repository = repository;
        this.encoder = encoder;
        this.tokenService = tokenService;
    }

    @PostMapping
    public String login(@RequestBody LoginRequestDto loginRequestDto) {
        var usuario = repository.findByEmail(loginRequestDto.email())
                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado"));

        if (encoder.matches(loginRequestDto.senha(), usuario.getSenha())) {
            return tokenService.gerarToken(usuario);
        }
        throw new RuntimeException("Senha inválida");
    }
}