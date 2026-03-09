package com.barbearia.fahcortes.infra.gateways.usuario;

import com.barbearia.fahcortes.domain.entities.usuario.Usuario;
import com.barbearia.fahcortes.domain.gateways.usuario.UsuarioGateway;
import com.barbearia.fahcortes.infra.entities.UsuarioEntity;
import com.barbearia.fahcortes.infra.mapper.usuario.UsuarioMapper;
import com.barbearia.fahcortes.infra.persistence.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UsuarioGatewayImp implements UsuarioGateway {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    public UsuarioGatewayImp(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Usuario cadastrarUsuario(Usuario usuario) {

        if (usuarioRepository.existsByEmail(usuario.getEmail())){
            throw new IllegalArgumentException("Ja existe um usuário com o email: " + usuario.getEmail());
        }

        UsuarioEntity entity = usuarioMapper.toEntity(usuario);


        String senha = passwordEncoder.encode(usuario.getSenha());
        entity.setSenha(senha);


        usuarioRepository.save(entity);
        return usuarioMapper.toDomain(entity);
    }

    @Override
    public void removerUsuario(Long id) {
        if(!usuarioRepository.existsById(id)){
            throw new IllegalArgumentException("Não existe usuário com o id: " + id );
        }
        usuarioRepository.deleteById(id);
    }

    @Override
    public Usuario buscarPorId(Long id) {
       return usuarioRepository.findById(id).map(usuarioMapper::toDomain).orElseThrow(() -> new IllegalArgumentException("Usuário com id não encontrado"));
    }


    @Override
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email).map(usuarioMapper::toDomain).orElseThrow(() -> new IllegalArgumentException("Usuário com: " + email + "não encontrado"));
    }


    @Override
    public List<Usuario> ListarTodos() {
        return usuarioRepository.findAll().stream().map(usuarioMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Usuario atualizarUsuario(Usuario usuario, Long id) {
      UsuarioEntity usuarioEntity = usuarioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuário com id :" + id + "não encontrado"));

      usuarioEntity.setNome(usuario.getNome());
      usuarioEntity.setEmail(usuario.getEmail());
      usuarioEntity.setSenha(usuario.getSenha());
      return usuarioMapper.toDomain(usuarioRepository.save(usuarioEntity));
    }

}
