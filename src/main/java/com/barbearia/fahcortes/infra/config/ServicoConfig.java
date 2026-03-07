package com.barbearia.fahcortes.infra.config;

import com.barbearia.fahcortes.domain.gateways.servico.ServicoGateway;
import com.barbearia.fahcortes.domain.usecases.servico.CadastrarServicoUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServicoConfig {

    @Bean
    public CadastrarServicoUseCase cadastrarServicoUseCase(ServicoGateway servicoGateway) {
        return new CadastrarServicoUseCase(servicoGateway);
    }
}
