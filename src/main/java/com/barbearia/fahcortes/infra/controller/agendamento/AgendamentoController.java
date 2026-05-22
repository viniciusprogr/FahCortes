package com.barbearia.fahcortes.infra.controller.agendamento;

import com.barbearia.fahcortes.domain.entities.agendamento.Agendamento;
import com.barbearia.fahcortes.domain.usecases.agendamento.*;
import com.barbearia.fahcortes.infra.controller.agendamento.dtos.AgendamentoRequestDto;
import com.barbearia.fahcortes.infra.controller.agendamento.dtos.AgendamentoResponseDto;
import com.barbearia.fahcortes.infra.mapper.agendamento.AgendamentoMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private final AgendamentoMapper agendamentoMapper;
    private final CriarAgendamentoUseCase criarAgendamentoUseCase;
    private final BuscarAgendamentoPorIdUseCase buscarAgendamentoPorIdUseCase;
    private final ListarAgendamentosUseCase listarAgendamentosUseCase;
    private final CancelarAgendamentoUseCase cancelarAgendamentoUseCase;

    public AgendamentoController(AgendamentoMapper agendamentoMapper,
                                  CriarAgendamentoUseCase criarAgendamentoUseCase,
                                  BuscarAgendamentoPorIdUseCase buscarAgendamentoPorIdUseCase,
                                  ListarAgendamentosUseCase listarAgendamentosUseCase,
                                  CancelarAgendamentoUseCase cancelarAgendamentoUseCase) {
        this.agendamentoMapper = agendamentoMapper;
        this.criarAgendamentoUseCase = criarAgendamentoUseCase;
        this.buscarAgendamentoPorIdUseCase = buscarAgendamentoPorIdUseCase;
        this.listarAgendamentosUseCase = listarAgendamentosUseCase;
        this.cancelarAgendamentoUseCase = cancelarAgendamentoUseCase;
    }

    @GetMapping
    public ResponseEntity<List<AgendamentoResponseDto>> listar(
            @RequestParam(required = false) Long clienteId) {
        List<Agendamento> agendamentos = listarAgendamentosUseCase.execute(clienteId);
        return ResponseEntity.ok(agendamentoMapper.toResponseList(agendamentos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoResponseDto> buscarPorId(@PathVariable Long id) {
        Agendamento agendamento = buscarAgendamentoPorIdUseCase.execute(id);
        return ResponseEntity.ok(agendamentoMapper.toResponse(agendamento));
    }

    @PostMapping
    public ResponseEntity<AgendamentoResponseDto> criar(@RequestBody @Valid AgendamentoRequestDto dto) {
        Agendamento agendamento = agendamentoMapper.toDomain(dto);
        Agendamento salvo = criarAgendamentoUseCase.execute(agendamento);
        return ResponseEntity.status(HttpStatus.CREATED).body(agendamentoMapper.toResponse(salvo));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<AgendamentoResponseDto> cancelar(@PathVariable Long id) {
        Agendamento agendamento = cancelarAgendamentoUseCase.execute(id);
        return ResponseEntity.ok(agendamentoMapper.toResponse(agendamento));
    }
}
