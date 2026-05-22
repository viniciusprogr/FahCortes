package com.barbearia.fahcortes.infra.config;

import com.barbearia.fahcortes.domain.gateways.barbeiro.BarbeiroGateway;
import com.barbearia.fahcortes.domain.usecases.barbeiro.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BarbeiroConfig {

    @Bean
    public CadastrarBarbeiroUseCase cadastrarBarbeiroUseCase(BarbeiroGateway barbeiroGateway) {
        return new CadastrarBarbeiroUseCase(barbeiroGateway);
    }

    @Bean
    public BuscarBarbeiroPorIdUseCase buscarBarbeiroPorIdUseCase(BarbeiroGateway barbeiroGateway) {
        return new BuscarBarbeiroPorIdUseCase(barbeiroGateway);
    }

    @Bean
    public ListarTodosBarbeirosUseCase listarTodosBarbeirosUseCase(BarbeiroGateway barbeiroGateway) {
        return new ListarTodosBarbeirosUseCase(barbeiroGateway);
    }

    @Bean
    public AtualizarBarbeiroPorIdUseCase atualizarBarbeiroPorIdUseCase(BarbeiroGateway barbeiroGateway) {
        return new AtualizarBarbeiroPorIdUseCase(barbeiroGateway);
    }

    @Bean
    public DeletarBarbeiroPorIdUseCase deletarBarbeiroPorIdUseCase(BarbeiroGateway barbeiroGateway) {
        return new DeletarBarbeiroPorIdUseCase(barbeiroGateway);
    }
}
