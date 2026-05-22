package com.barbearia.fahcortes.infra.controller.usuario;

import com.barbearia.fahcortes.infra.controller.usuario.dtos.LoginRequestDto;
import com.barbearia.fahcortes.infra.controller.usuario.dtos.LoginResponseDto;
import com.barbearia.fahcortes.infra.security.TokenService;
import com.barbearia.fahcortes.infra.security.UsuarioDetails;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<LoginResponseDto> efetuarLogin(@RequestBody @Valid LoginRequestDto dados) {
        try {
            var token = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
            var authentication = authenticationManager.authenticate(token);
            var usuarioDetails = (UsuarioDetails) authentication.getPrincipal();
            var tokenJWT = tokenService.gerarToken(usuarioDetails.getUsuarioEntity());
            var role = usuarioDetails.getUsuarioEntity().getRole();
            return ResponseEntity.ok(new LoginResponseDto(tokenJWT, role));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).build();
        }
    }
}
