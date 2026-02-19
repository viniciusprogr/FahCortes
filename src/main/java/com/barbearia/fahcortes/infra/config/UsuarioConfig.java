package com.barbearia.fahcortes.infra.config;

import com.barbearia.fahcortes.domain.gateways.usuario.UsuarioGateway;
import com.barbearia.fahcortes.domain.usecases.usuario.CadastrarUsuarioUseCase;
import com.barbearia.fahcortes.domain.usecases.usuario.RemoverUsuarioUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UsuarioConfig {


    @Bean
    public CadastrarUsuarioUseCase cadastrarUsuarioUseCase(UsuarioGateway usuarioGateway){
        return new CadastrarUsuarioUseCase(usuarioGateway);
    }

    public RemoverUsuarioUseCase removerUsuarioUseCase(UsuarioGateway usuarioGateway){
        return new RemoverUsuarioUseCase(usuarioGateway);
    }
}
