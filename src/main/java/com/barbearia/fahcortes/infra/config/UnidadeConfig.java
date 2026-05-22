package com.barbearia.fahcortes.infra.config;

import com.barbearia.fahcortes.domain.gateways.unidade.UnidadeGateway;
import com.barbearia.fahcortes.domain.usecases.unidade.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UnidadeConfig {

    @Bean
    public CadastrarUnidadeUseCase cadastrarUnidadeUseCase(UnidadeGateway unidadeGateway) {
        return new CadastrarUnidadeUseCase(unidadeGateway);
    }

    @Bean
    public BuscarUnidadePorIdUseCase buscarUnidadePorIdUseCase(UnidadeGateway unidadeGateway) {
        return new BuscarUnidadePorIdUseCase(unidadeGateway);
    }

    @Bean
    public ListarTodasUnidadesUseCase listarTodasUnidadesUseCase(UnidadeGateway unidadeGateway) {
        return new ListarTodasUnidadesUseCase(unidadeGateway);
    }

    @Bean
    public AtualizarUnidadePorIdUseCase atualizarUnidadePorIdUseCase(UnidadeGateway unidadeGateway) {
        return new AtualizarUnidadePorIdUseCase(unidadeGateway);
    }

    @Bean
    public DeletarUnidadePorIdUseCase deletarUnidadePorIdUseCase(UnidadeGateway unidadeGateway) {
        return new DeletarUnidadePorIdUseCase(unidadeGateway);
    }
}
