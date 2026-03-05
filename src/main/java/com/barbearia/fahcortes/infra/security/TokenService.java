package com.barbearia.fahcortes.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.barbearia.fahcortes.infra.entities.UsuarioEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.*;

@Service
public class TokenService {
    @Value("${api.security.token.secret:minha-chave-secreta}")
    private String secret;

    public String gerarToken(UsuarioEntity usuario) {
        Algorithm algoritmo = Algorithm.HMAC256(secret);
        return JWT.create()
                .withIssuer("fahcortes-api")
                .withSubject(usuario.getEmail())
                .withExpiresAt(LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00")))
                .sign(algoritmo);
    }

    public String validarToken(String token) {
        try {
            Algorithm algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                    .withIssuer("fahcortes-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (Exception e) {
            return "";
        }
    }
}