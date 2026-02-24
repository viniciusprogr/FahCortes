package com.barbearia.fahcortes.infra.controller.usuario;

import com.barbearia.fahcortes.domain.entities.usuario.Usuario;
import com.barbearia.fahcortes.domain.usecases.usuario.*;
import com.barbearia.fahcortes.infra.controller.usuario.dtos.UsuarioResponseDto;
import com.barbearia.fahcortes.infra.controller.usuario.dtos.UsuarioRequestDto;
import com.barbearia.fahcortes.infra.mapper.usuario.UsuarioMapper;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioMapper usuarioMapper;
    private final CadastrarUsuarioUseCase cadastrarUsuarioUseCase;
    private final ListarTodosUsuariosUseCase listarTodosUsuariosUseCase;
    private final BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase;
    private final DeletarUsuarioPorIdUseCase deletarUsuarioPorIdUseCase;
    private final AtualizarUsuarioPorIdUseCase atualizarUsuarioPeloIdUseCase;


    public UsuarioController(UsuarioMapper usuarioMapper, CadastrarUsuarioUseCase cadastrarUsuarioUseCase, ListarTodosUsuariosUseCase listarTodosUsuariosUseCase, BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase, DeletarUsuarioPorIdUseCase deletarUsuarioPorIdUseCase, AtualizarUsuarioPorIdUseCase atualizarUsuarioPeloIdUseCase) {
        this.usuarioMapper = usuarioMapper;
        this.cadastrarUsuarioUseCase = cadastrarUsuarioUseCase;
        this.listarTodosUsuariosUseCase = listarTodosUsuariosUseCase;
        this.buscarUsuarioPorIdUseCase = buscarUsuarioPorIdUseCase;
        this.deletarUsuarioPorIdUseCase = deletarUsuarioPorIdUseCase;
        this.atualizarUsuarioPeloIdUseCase = atualizarUsuarioPeloIdUseCase;
    }

    @PostMapping
    public UsuarioResponseDto CadastrarUsuario(@RequestBody @Valid UsuarioRequestDto usuarioResquestDto){
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

    @DeleteMapping("/{id}")
    public void deletarUsuario(@PathVariable Long id){
        deletarUsuarioPorIdUseCase.execute(id);
    }

    @PutMapping("/{id}")
    public UsuarioResponseDto atualizarUsuario(@RequestBody @Valid UsuarioRequestDto usuarioRequestDto, @PathVariable Long id){
        Usuario usuario = usuarioMapper.toDomain(usuarioRequestDto);
        Usuario usuarioAtualizado = atualizarUsuarioPeloIdUseCase.execute(usuario, id);
        return usuarioMapper.toResponse(usuarioAtualizado);
    }
}
