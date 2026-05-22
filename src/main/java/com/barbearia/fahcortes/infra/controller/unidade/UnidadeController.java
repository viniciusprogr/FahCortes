package com.barbearia.fahcortes.infra.controller.unidade;

import com.barbearia.fahcortes.domain.entities.unidade.Unidade;
import com.barbearia.fahcortes.domain.usecases.unidade.*;
import com.barbearia.fahcortes.infra.controller.unidade.dtos.UnidadeRequestDto;
import com.barbearia.fahcortes.infra.controller.unidade.dtos.UnidadeResponseDto;
import com.barbearia.fahcortes.infra.mapper.unidade.UnidadeMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/unidades")
public class UnidadeController {

    private final UnidadeMapper unidadeMapper;
    private final CadastrarUnidadeUseCase cadastrarUnidadeUseCase;
    private final BuscarUnidadePorIdUseCase buscarUnidadePorIdUseCase;
    private final ListarTodasUnidadesUseCase listarTodasUnidadesUseCase;
    private final AtualizarUnidadePorIdUseCase atualizarUnidadePorIdUseCase;
    private final DeletarUnidadePorIdUseCase deletarUnidadePorIdUseCase;

    public UnidadeController(UnidadeMapper unidadeMapper,
                             CadastrarUnidadeUseCase cadastrarUnidadeUseCase,
                             BuscarUnidadePorIdUseCase buscarUnidadePorIdUseCase,
                             ListarTodasUnidadesUseCase listarTodasUnidadesUseCase,
                             AtualizarUnidadePorIdUseCase atualizarUnidadePorIdUseCase,
                             DeletarUnidadePorIdUseCase deletarUnidadePorIdUseCase) {
        this.unidadeMapper = unidadeMapper;
        this.cadastrarUnidadeUseCase = cadastrarUnidadeUseCase;
        this.buscarUnidadePorIdUseCase = buscarUnidadePorIdUseCase;
        this.listarTodasUnidadesUseCase = listarTodasUnidadesUseCase;
        this.atualizarUnidadePorIdUseCase = atualizarUnidadePorIdUseCase;
        this.deletarUnidadePorIdUseCase = deletarUnidadePorIdUseCase;
    }

    @GetMapping
    public ResponseEntity<List<UnidadeResponseDto>> listarTodas() {
        List<Unidade> unidades = listarTodasUnidadesUseCase.execute();
        return ResponseEntity.ok(unidadeMapper.toResponseList(unidades));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UnidadeResponseDto> buscarPorId(@PathVariable Long id) {
        Unidade unidade = buscarUnidadePorIdUseCase.execute(id);
        return ResponseEntity.ok(unidadeMapper.toResponse(unidade));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UnidadeResponseDto> cadastrar(@RequestBody @Valid UnidadeRequestDto dto) {
        Unidade unidade = unidadeMapper.toDomain(dto);
        Unidade salva = cadastrarUnidadeUseCase.execute(unidade);
        return ResponseEntity.status(HttpStatus.CREATED).body(unidadeMapper.toResponse(salva));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UnidadeResponseDto> atualizar(@RequestBody @Valid UnidadeRequestDto dto,
                                                         @PathVariable Long id) {
        Unidade unidade = unidadeMapper.toDomain(dto);
        Unidade atualizada = atualizarUnidadePorIdUseCase.execute(unidade, id);
        return ResponseEntity.ok(unidadeMapper.toResponse(atualizada));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        deletarUnidadePorIdUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
