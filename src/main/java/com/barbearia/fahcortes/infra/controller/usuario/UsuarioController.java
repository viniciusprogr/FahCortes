package com.barbearia.fahcortes.infra.controller.usuario;

import com.barbearia.fahcortes.domain.entities.usuario.Usuario;
import com.barbearia.fahcortes.domain.usecases.usuario.CadastrarUsuarioUseCase;
import com.barbearia.fahcortes.infra.controller.usuario.dtos.UsuarioResponseDto;
import com.barbearia.fahcortes.infra.controller.usuario.dtos.UsuarioRequestDto;
import com.barbearia.fahcortes.infra.mapper.usuario.UsuarioMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioMapper usuarioMapper;
    private final CadastrarUsuarioUseCase cadastrarUsuarioUseCase;


    public UsuarioController(UsuarioMapper usuarioMapper, CadastrarUsuarioUseCase cadastrarUsuarioUseCase) {
        this.usuarioMapper = usuarioMapper;
        this.cadastrarUsuarioUseCase = cadastrarUsuarioUseCase;
    }

    @PostMapping
    public UsuarioResponseDto CadastrarUsuario(@RequestBody UsuarioRequestDto usuarioResquestDto){
        Usuario usuario = usuarioMapper.requestToDomain(usuarioResquestDto);
        Usuario usuarioSalvo =cadastrarUsuarioUseCase.execute(usuario);
        return usuarioMapper.DomainToResponse(usuarioSalvo);
    }
}
