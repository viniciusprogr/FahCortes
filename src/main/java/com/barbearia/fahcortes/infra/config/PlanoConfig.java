package com.barbearia.fahcortes.infra.config;

import com.barbearia.fahcortes.domain.gateways.plano.PlanoGateway;
import com.barbearia.fahcortes.domain.usecases.plano.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PlanoConfig {

    @Bean
    public CadastrarPlanoUseCase cadastrarPlanoUseCase(PlanoGateway planoGateway) {
        return new CadastrarPlanoUseCase(planoGateway);
    }

    @Bean
    public BuscarPlanoPorIdUseCase buscarPlanoPorIdUseCase(PlanoGateway planoGateway) {
        return new BuscarPlanoPorIdUseCase(planoGateway);
    }

    @Bean
    public ListarTodosPlanosUseCase listarTodosPlanosUseCase(PlanoGateway planoGateway) {
        return new ListarTodosPlanosUseCase(planoGateway);
    }

    @Bean
    public AtualizarPlanoPorIdUseCase atualizarPlanoPorIdUseCase(PlanoGateway planoGateway) {
        return new AtualizarPlanoPorIdUseCase(planoGateway);
    }

    @Bean
    public DeletarPlanoPorIdUseCase deletarPlanoPorIdUseCase(PlanoGateway planoGateway) {
        return new DeletarPlanoPorIdUseCase(planoGateway);
    }
}
