package com.barbearia.fahcortes.infra.controller.plano;

import com.barbearia.fahcortes.domain.entities.plano.Plano;
import com.barbearia.fahcortes.domain.usecases.plano.*;
import com.barbearia.fahcortes.infra.controller.plano.dtos.PlanoRequestDto;
import com.barbearia.fahcortes.infra.controller.plano.dtos.PlanoResponseDto;
import com.barbearia.fahcortes.infra.mapper.plano.PlanoMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/planos")
public class PlanoController {

    private final PlanoMapper planoMapper;
    private final CadastrarPlanoUseCase cadastrarPlanoUseCase;
    private final BuscarPlanoPorIdUseCase buscarPlanoPorIdUseCase;
    private final ListarTodosPlanosUseCase listarTodosPlanosUseCase;
    private final AtualizarPlanoPorIdUseCase atualizarPlanoPorIdUseCase;
    private final DeletarPlanoPorIdUseCase deletarPlanoPorIdUseCase;

    public PlanoController(PlanoMapper planoMapper,
                           CadastrarPlanoUseCase cadastrarPlanoUseCase,
                           BuscarPlanoPorIdUseCase buscarPlanoPorIdUseCase,
                           ListarTodosPlanosUseCase listarTodosPlanosUseCase,
                           AtualizarPlanoPorIdUseCase atualizarPlanoPorIdUseCase,
                           DeletarPlanoPorIdUseCase deletarPlanoPorIdUseCase) {
        this.planoMapper = planoMapper;
        this.cadastrarPlanoUseCase = cadastrarPlanoUseCase;
        this.buscarPlanoPorIdUseCase = buscarPlanoPorIdUseCase;
        this.listarTodosPlanosUseCase = listarTodosPlanosUseCase;
        this.atualizarPlanoPorIdUseCase = atualizarPlanoPorIdUseCase;
        this.deletarPlanoPorIdUseCase = deletarPlanoPorIdUseCase;
    }

    @GetMapping
    public ResponseEntity<List<PlanoResponseDto>> listarTodos() {
        List<Plano> planos = listarTodosPlanosUseCase.execute();
        return ResponseEntity.ok(planoMapper.toResponseList(planos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanoResponseDto> buscarPorId(@PathVariable Long id) {
        Plano plano = buscarPlanoPorIdUseCase.execute(id);
        return ResponseEntity.ok(planoMapper.toResponse(plano));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PlanoResponseDto> cadastrar(@RequestBody @Valid PlanoRequestDto dto) {
        Plano plano = planoMapper.toDomain(dto);
        Plano salvo = cadastrarPlanoUseCase.execute(plano);
        return ResponseEntity.status(HttpStatus.CREATED).body(planoMapper.toResponse(salvo));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PlanoResponseDto> atualizar(@RequestBody @Valid PlanoRequestDto dto,
                                                       @PathVariable Long id) {
        Plano plano = planoMapper.toDomain(dto);
        Plano atualizado = atualizarPlanoPorIdUseCase.execute(plano, id);
        return ResponseEntity.ok(planoMapper.toResponse(atualizado));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        deletarPlanoPorIdUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
