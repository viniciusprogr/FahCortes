package com.barbearia.fahcortes.infra.controller.usuario;

import com.barbearia.fahcortes.domain.entities.usuario.Usuario;
import com.barbearia.fahcortes.domain.usecases.usuario.*;
import com.barbearia.fahcortes.infra.controller.usuario.dtos.UsuarioResponseDto;
import com.barbearia.fahcortes.infra.controller.usuario.dtos.UsuarioRequestDto;
import com.barbearia.fahcortes.infra.mapper.usuario.UsuarioMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioMapper usuarioMapper;
    private final CadastrarUsuarioUseCase cadastrarUsuarioUseCase;
    private final ListarTodosUsuariosUseCase listarTodosUsuariosUseCase;
    private final BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase;
    private final DeletarUsuarioPorIdUseCase deletarUsuarioPorIdUseCase;
    private final AtualizarUsuarioPorIdUseCase atualizarUsuarioPeloIdUseCase;
    private final BuscarUsuarioPorEmailUseCase buscarUsuarioPorEmailUseCase;


    public UsuarioController(UsuarioMapper usuarioMapper, CadastrarUsuarioUseCase cadastrarUsuarioUseCase, ListarTodosUsuariosUseCase listarTodosUsuariosUseCase, BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase, DeletarUsuarioPorIdUseCase deletarUsuarioPorIdUseCase, AtualizarUsuarioPorIdUseCase atualizarUsuarioPeloIdUseCase, BuscarUsuarioPorEmailUseCase buscarUsuarioPorEmailUseCase) {
        this.usuarioMapper = usuarioMapper;
        this.cadastrarUsuarioUseCase = cadastrarUsuarioUseCase;
        this.listarTodosUsuariosUseCase = listarTodosUsuariosUseCase;
        this.buscarUsuarioPorIdUseCase = buscarUsuarioPorIdUseCase;
        this.deletarUsuarioPorIdUseCase = deletarUsuarioPorIdUseCase;
        this.atualizarUsuarioPeloIdUseCase = atualizarUsuarioPeloIdUseCase;
        this.buscarUsuarioPorEmailUseCase = buscarUsuarioPorEmailUseCase;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDto> CadastrarUsuario(@RequestBody @Valid UsuarioRequestDto usuarioRequestDto){
        Usuario usuario = usuarioMapper.toDomain(usuarioRequestDto);
        Usuario usuarioSalvo =cadastrarUsuarioUseCase.execute(usuario);
        return  ResponseEntity.status(HttpStatus.CREATED).body(usuarioMapper.toResponse(usuarioSalvo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> buscarPorId(@PathVariable Long id){
        Usuario usuario = buscarUsuarioPorIdUseCase.execute(id);
        return ResponseEntity.status(HttpStatus.OK).body(usuarioMapper.toResponse(usuario));
    }

    @GetMapping("/email")
    public ResponseEntity<UsuarioResponseDto> buscarPorEmail(@RequestParam String email){
        Usuario usuario = buscarUsuarioPorEmailUseCase.execute(email);
        return ResponseEntity.ok(usuarioMapper.toResponse(usuario));
    }
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> listarTodos(){
        List<Usuario> listaDeUsuario = listarTodosUsuariosUseCase.execute();
        return ResponseEntity.ok(usuarioMapper.toResponseDtoList(listaDeUsuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id){
        deletarUsuarioPorIdUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> atualizarUsuario(@RequestBody @Valid UsuarioRequestDto usuarioRequestDto, @PathVariable Long id){
        Usuario usuario = usuarioMapper.toDomain(usuarioRequestDto);
        Usuario usuarioAtualizado = atualizarUsuarioPeloIdUseCase.execute(usuario, id);
        return ResponseEntity.ok(usuarioMapper.toResponse(usuarioAtualizado));
    }
}
