package com.barbearia.fahcortes.infra.config;

import com.barbearia.fahcortes.domain.gateways.usuario.UsuarioGateway;
import com.barbearia.fahcortes.domain.usecases.usuario.BuscarUsuarioPorIdUseCase;
import com.barbearia.fahcortes.domain.usecases.usuario.CadastrarUsuarioUseCase;
import com.barbearia.fahcortes.domain.usecases.usuario.ListarTodosUsuariosUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UsuarioConfig {


    @Bean
    public CadastrarUsuarioUseCase cadastrarUsuarioUseCase(UsuarioGateway usuarioGateway){
        return new CadastrarUsuarioUseCase(usuarioGateway);
    }

    @Bean
    public ListarTodosUsuariosUseCase listarTodosUsuariosUseCase(UsuarioGateway usuarioGateway){
        return new ListarTodosUsuariosUseCase(usuarioGateway);
    }

    @Bean
    public BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase(UsuarioGateway usuarioGateway){
        return new BuscarUsuarioPorIdUseCase(usuarioGateway);
    }
}