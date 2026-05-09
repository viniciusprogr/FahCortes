package com.barbearia.fahcortes.infra.controller.servico;


import com.barbearia.fahcortes.domain.entities.serviço.Servico;
import com.barbearia.fahcortes.domain.usecases.servico.BuscarServicoPorIdUseCase;
import com.barbearia.fahcortes.domain.usecases.servico.CadastrarServicoUseCase;
import com.barbearia.fahcortes.domain.usecases.servico.ListarTodosServicosUseCase;
import com.barbearia.fahcortes.domain.usecases.servico.RemoverServicoPorIdUseCase;
import com.barbearia.fahcortes.infra.controller.servico.dtos.ServicoRequestDto;
import com.barbearia.fahcortes.infra.controller.servico.dtos.ServicoResponseDto;
import com.barbearia.fahcortes.infra.mapper.servico.ServicoMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/servico")
public class ServicoController {

    private final ServicoMapper servicoMapper;


    private final CadastrarServicoUseCase cadastrarServicoUseCase;
    private final BuscarServicoPorIdUseCase buscarServicoPorIdUseCase;
    private final RemoverServicoPorIdUseCase removerServicoPorIdUseCase;
    private final ListarTodosServicosUseCase listarTodosServicosUseCase;



    public ServicoController(ServicoMapper servicoMapper, CadastrarServicoUseCase cadastrarServicoUseCase, BuscarServicoPorIdUseCase buscarServicoPorIdUseCase, RemoverServicoPorIdUseCase removerServicoPorIdUseCase, ListarTodosServicosUseCase listarTodosServicosUseCase) {
        this.servicoMapper = servicoMapper;
        this.cadastrarServicoUseCase = cadastrarServicoUseCase;
        this.buscarServicoPorIdUseCase = buscarServicoPorIdUseCase;
        this.removerServicoPorIdUseCase = removerServicoPorIdUseCase;
        this.listarTodosServicosUseCase = listarTodosServicosUseCase;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ServicoResponseDto> cadastrarServico(@RequestBody ServicoRequestDto servicoRequestDto) {
        Servico servico = servicoMapper.toDomain(servicoRequestDto);
        Servico servicoSalvo = cadastrarServicoUseCase.execute(servico);
        return ResponseEntity.ok().body(servicoMapper.toResponseDto(servicoSalvo));
    }


    @GetMapping("/{id}")
    public  ResponseEntity<ServicoResponseDto> buscarServicoPorId(@PathVariable Long id) {
        Servico servico = buscarServicoPorIdUseCase.execute(id);
        return ResponseEntity.ok().body(servicoMapper.toResponseDto(servico));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerServicoPorId(@PathVariable Long id) {
        removerServicoPorIdUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ServicoResponseDto>> listarTodosServicos() {
        List<Servico> servicos = listarTodosServicosUseCase.execute();

        return ResponseEntity.ok(servicoMapper.toResponseDtoToList(servicos));
    }
}
