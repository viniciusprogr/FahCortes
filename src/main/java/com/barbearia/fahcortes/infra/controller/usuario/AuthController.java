package com.barbearia.fahcortes.infra.controller.usuario;


import com.barbearia.fahcortes.infra.controller.usuario.dtos.LoginRequestDto;
import com.barbearia.fahcortes.infra.security.DadosTokenJWT;
import com.barbearia.fahcortes.infra.security.TokenService;
import com.barbearia.fahcortes.infra.security.UsuarioDetails;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthController {
    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<DadosTokenJWT> efetuarLogin(@RequestBody @Valid LoginRequestDto dados) {
        try {
            var authenticationToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
            var authentication = manager.authenticate(authenticationToken);
            var usuarioDetails = (UsuarioDetails) authentication.getPrincipal();

            if (usuarioDetails == null || usuarioDetails.getUsuarioEntity() == null) {
                throw new UsernameNotFoundException("Usuário não encontrado ou detalhes indisponíveis");
            }

            var tokenJWT = tokenService.gerarToken(usuarioDetails.getUsuarioEntity());
            return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).build();
        }
    }
}
