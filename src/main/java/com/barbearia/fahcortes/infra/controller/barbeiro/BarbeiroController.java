package com.barbearia.fahcortes.infra.controller.barbeiro;

import com.barbearia.fahcortes.domain.entities.barbeiro.Barbeiro;
import com.barbearia.fahcortes.domain.usecases.barbeiro.*;
import com.barbearia.fahcortes.infra.controller.barbeiro.dtos.BarbeiroRequestDto;
import com.barbearia.fahcortes.infra.controller.barbeiro.dtos.BarbeiroResponseDto;
import com.barbearia.fahcortes.infra.mapper.barbeiro.BarbeiroMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/barbeiros")
public class BarbeiroController {

    private final BarbeiroMapper barbeiroMapper;
    private final CadastrarBarbeiroUseCase cadastrarBarbeiroUseCase;
    private final BuscarBarbeiroPorIdUseCase buscarBarbeiroPorIdUseCase;
    private final ListarTodosBarbeirosUseCase listarTodosBarbeirosUseCase;
    private final AtualizarBarbeiroPorIdUseCase atualizarBarbeiroPorIdUseCase;
    private final DeletarBarbeiroPorIdUseCase deletarBarbeiroPorIdUseCase;

    public BarbeiroController(BarbeiroMapper barbeiroMapper,
                              CadastrarBarbeiroUseCase cadastrarBarbeiroUseCase,
                              BuscarBarbeiroPorIdUseCase buscarBarbeiroPorIdUseCase,
                              ListarTodosBarbeirosUseCase listarTodosBarbeirosUseCase,
                              AtualizarBarbeiroPorIdUseCase atualizarBarbeiroPorIdUseCase,
                              DeletarBarbeiroPorIdUseCase deletarBarbeiroPorIdUseCase) {
        this.barbeiroMapper = barbeiroMapper;
        this.cadastrarBarbeiroUseCase = cadastrarBarbeiroUseCase;
        this.buscarBarbeiroPorIdUseCase = buscarBarbeiroPorIdUseCase;
        this.listarTodosBarbeirosUseCase = listarTodosBarbeirosUseCase;
        this.atualizarBarbeiroPorIdUseCase = atualizarBarbeiroPorIdUseCase;
        this.deletarBarbeiroPorIdUseCase = deletarBarbeiroPorIdUseCase;
    }

    @GetMapping
    public ResponseEntity<List<BarbeiroResponseDto>> listarTodos() {
        List<Barbeiro> barbeiros = listarTodosBarbeirosUseCase.execute();
        return ResponseEntity.ok(barbeiroMapper.toResponseList(barbeiros));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BarbeiroResponseDto> buscarPorId(@PathVariable Long id) {
        Barbeiro barbeiro = buscarBarbeiroPorIdUseCase.execute(id);
        return ResponseEntity.ok(barbeiroMapper.toResponse(barbeiro));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<BarbeiroResponseDto> cadastrar(@RequestBody @Valid BarbeiroRequestDto dto) {
        Barbeiro barbeiro = barbeiroMapper.toDomain(dto);
        Barbeiro salvo = cadastrarBarbeiroUseCase.execute(barbeiro);
        return ResponseEntity.status(HttpStatus.CREATED).body(barbeiroMapper.toResponse(salvo));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<BarbeiroResponseDto> atualizar(@RequestBody @Valid BarbeiroRequestDto dto,
                                                          @PathVariable Long id) {
        Barbeiro barbeiro = barbeiroMapper.toDomain(dto);
        Barbeiro atualizado = atualizarBarbeiroPorIdUseCase.execute(barbeiro, id);
        return ResponseEntity.ok(barbeiroMapper.toResponse(atualizado));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        deletarBarbeiroPorIdUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
