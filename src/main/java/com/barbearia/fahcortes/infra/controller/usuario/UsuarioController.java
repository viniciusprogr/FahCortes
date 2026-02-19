package com.barbearia.fahcortes.infra.controller.usuario;

import com.barbearia.fahcortes.domain.entities.usuario.Usuario;
import com.barbearia.fahcortes.domain.usecases.usuario.CadastrarUsuarioUseCase;
import com.barbearia.fahcortes.domain.usecases.usuario.ListarTodosUsuariosUseCase;
import com.barbearia.fahcortes.infra.controller.usuario.dtos.UsuarioResponseDto;
import com.barbearia.fahcortes.infra.controller.usuario.dtos.UsuarioRequestDto;
import com.barbearia.fahcortes.infra.mapper.usuario.UsuarioMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioMapper usuarioMapper;
    private final CadastrarUsuarioUseCase cadastrarUsuarioUseCase;
    private final ListarTodosUsuariosUseCase listarTodosUsuariosUseCase;


    public UsuarioController(UsuarioMapper usuarioMapper, CadastrarUsuarioUseCase cadastrarUsuarioUseCase, ListarTodosUsuariosUseCase listarTodosUsuariosUseCase) {
        this.usuarioMapper = usuarioMapper;
        this.cadastrarUsuarioUseCase = cadastrarUsuarioUseCase;
        this.listarTodosUsuariosUseCase = listarTodosUsuariosUseCase;
    }

    @PostMapping
    public UsuarioResponseDto CadastrarUsuario(@RequestBody UsuarioRequestDto usuarioResquestDto){
        Usuario usuario = usuarioMapper.requestToDomain(usuarioResquestDto);
        Usuario usuarioSalvo =cadastrarUsuarioUseCase.execute(usuario);
        return usuarioMapper.DomainToResponse(usuarioSalvo);
    }



    @GetMapping
    public List<UsuarioResponseDto> listarTodos(){
        List<Usuario> listaDeUsuario = listarTodosUsuariosUseCase.execute();
        return usuarioMapper.toResponseDtoList(listaDeUsuario);
    }

}
