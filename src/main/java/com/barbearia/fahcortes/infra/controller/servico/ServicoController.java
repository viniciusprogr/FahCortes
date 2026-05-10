package com.barbearia.fahcortes.infra.controller.servico;


import com.barbearia.fahcortes.domain.entities.serviço.Servico;
import com.barbearia.fahcortes.domain.usecases.servico.BuscarServicoPorIdUseCase;
import com.barbearia.fahcortes.domain.usecases.servico.CadastrarServicoUseCase;
import com.barbearia.fahcortes.infra.controller.servico.dtos.ServicoRequestDto;
import com.barbearia.fahcortes.infra.controller.servico.dtos.ServicoResponseDto;
import com.barbearia.fahcortes.infra.mapper.servico.ServicoMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/servico")
public class ServicoController {

    private final ServicoMapper servicoMapper;


    private final CadastrarServicoUseCase cadastrarServicoUseCase;
    private final BuscarServicoPorIdUseCase buscarServicoPorIdUseCase;



    public ServicoController(ServicoMapper servicoMapper, CadastrarServicoUseCase cadastrarServicoUseCase, BuscarServicoPorIdUseCase buscarServicoPorIdUseCase) {
        this.servicoMapper = servicoMapper;
        this.cadastrarServicoUseCase = cadastrarServicoUseCase;
        this.buscarServicoPorIdUseCase = buscarServicoPorIdUseCase;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ServicoResponseDto> cadastrarServico(@RequestBody ServicoRequestDto servicoRequestDto) {
        Servico servico = servicoMapper.toDomain(servicoRequestDto);
        Servico servicoSalvo = cadastrarServicoUseCase.execute(servico);
        return ResponseEntity.ok().body(servicoMapper.toDto(servicoSalvo));
    }


    @GetMapping("/{id}")
    public  ResponseEntity<ServicoResponseDto> buscarServicoPorId(@PathVariable Long id) {
        Servico servico = buscarServicoPorIdUseCase.execute(id);
        return ResponseEntity.ok().body(servicoMapper.toDto(servico));
    }
}
