package com.barbearia.fahcortes.infra.config;

import com.barbearia.fahcortes.domain.gateways.agendamento.AgendamentoGateway;
import com.barbearia.fahcortes.domain.usecases.agendamento.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AgendamentoConfig {

    @Bean
    public CriarAgendamentoUseCase criarAgendamentoUseCase(AgendamentoGateway agendamentoGateway) {
        return new CriarAgendamentoUseCase(agendamentoGateway);
    }

    @Bean
    public BuscarAgendamentoPorIdUseCase buscarAgendamentoPorIdUseCase(AgendamentoGateway agendamentoGateway) {
        return new BuscarAgendamentoPorIdUseCase(agendamentoGateway);
    }

    @Bean
    public ListarAgendamentosUseCase listarAgendamentosUseCase(AgendamentoGateway agendamentoGateway) {
        return new ListarAgendamentosUseCase(agendamentoGateway);
    }

    @Bean
    public CancelarAgendamentoUseCase cancelarAgendamentoUseCase(AgendamentoGateway agendamentoGateway) {
        return new CancelarAgendamentoUseCase(agendamentoGateway);
    }
}
