package com.barbearia.fahcortes.infra.controller.servico;


import com.barbearia.fahcortes.domain.entities.serviço.Servico;
import com.barbearia.fahcortes.domain.usecases.servico.CadastrarServicoUseCase;
import com.barbearia.fahcortes.infra.controller.servico.dtos.ServicoRequestDto;
import com.barbearia.fahcortes.infra.controller.servico.dtos.ServicoResponseDto;
import com.barbearia.fahcortes.infra.entities.ServicoEntity;
import com.barbearia.fahcortes.infra.mapper.servico.ServicoMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/servico")
public class ServicoController {

    private final ServicoMapper servicoMapper;


    private final CadastrarServicoUseCase cadastrarServicoUseCase;


    public ServicoController(ServicoMapper servicoMapper, CadastrarServicoUseCase cadastrarServicoUseCase) {
        this.servicoMapper = servicoMapper;
        this.cadastrarServicoUseCase = cadastrarServicoUseCase;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ServicoResponseDto> cadastrarServico(@RequestBody ServicoRequestDto servicoRequestDto) {
        Servico servico = servicoMapper.toDomain(servicoRequestDto);
        Servico servicoSalvo = cadastrarServicoUseCase.execute(servico);
        return ResponseEntity.ok().body(servicoMapper.toDto(servicoSalvo));
    }
}
