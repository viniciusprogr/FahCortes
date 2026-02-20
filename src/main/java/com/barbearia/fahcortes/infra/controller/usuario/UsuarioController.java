package com.barbearia.fahcortes.infra.controller.usuario;

import com.barbearia.fahcortes.domain.entities.usuario.Usuario;
import com.barbearia.fahcortes.domain.usecases.usuario.BuscarUsuarioPorIdUseCase;
import com.barbearia.fahcortes.domain.usecases.usuario.CadastrarUsuarioUseCase;
import com.barbearia.fahcortes.domain.usecases.usuario.ListarTodosUsuariosUseCase;
import com.barbearia.fahcortes.infra.controller.usuario.dtos.UsuarioResponseDto;
import com.barbearia.fahcortes.infra.controller.usuario.dtos.UsuarioRequestDto;
import com.barbearia.fahcortes.infra.mapper.usuario.UsuarioMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioMapper usuarioMapper;
    private final CadastrarUsuarioUseCase cadastrarUsuarioUseCase;
    private final ListarTodosUsuariosUseCase listarTodosUsuariosUseCase;
    private final BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase;


    public UsuarioController(UsuarioMapper usuarioMapper, CadastrarUsuarioUseCase cadastrarUsuarioUseCase, ListarTodosUsuariosUseCase listarTodosUsuariosUseCase, BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase) {
        this.usuarioMapper = usuarioMapper;
        this.cadastrarUsuarioUseCase = cadastrarUsuarioUseCase;
        this.listarTodosUsuariosUseCase = listarTodosUsuariosUseCase;
        this.buscarUsuarioPorIdUseCase = buscarUsuarioPorIdUseCase;
    }

    @PostMapping
    public UsuarioResponseDto CadastrarUsuario(@RequestBody UsuarioRequestDto usuarioResquestDto){
        Usuario usuario = usuarioMapper.toDomain(usuarioResquestDto);
        Usuario usuarioSalvo =cadastrarUsuarioUseCase.execute(usuario);
        return usuarioMapper.toResponse(usuarioSalvo);
    }

    @GetMapping("/{id}")
    public UsuarioResponseDto buscarPorId(@PathVariable Long id){
        Usuario usuario = buscarUsuarioPorIdUseCase.execute(id);
        return usuarioMapper.toResponse(usuario);
    }

    @GetMapping
    public List<UsuarioResponseDto> listarTodos(){
        List<Usuario> listaDeUsuario = listarTodosUsuariosUseCase.execute();
        return usuarioMapper.toResponseDtoList(listaDeUsuario);
    }


}
