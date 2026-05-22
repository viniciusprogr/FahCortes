package com.barbearia.fahcortes.infra.gateways.usuario;

import com.barbearia.fahcortes.domain.entities.usuario.Usuario;
import com.barbearia.fahcortes.domain.entities.usuario.UsuarioEnum;
import com.barbearia.fahcortes.domain.gateways.usuario.UsuarioGateway;
import com.barbearia.fahcortes.infra.controller.exception.EntidadeNaoEncontradaException;
import com.barbearia.fahcortes.infra.controller.exception.RegraDeNegocioException;
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
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RegraDeNegocioException(
                    "Já existe um usuário cadastrado com o email '" + usuario.getEmail() + "'. Utilize outro email para realizar o cadastro.");
        }

        UsuarioEntity entity = usuarioMapper.toEntity(usuario);
        entity.setRole(UsuarioEnum.valueOf("USER"));
        entity.setSenha(passwordEncoder.encode(usuario.getSenha()));

        usuarioRepository.save(entity);
        return usuarioMapper.toDomain(entity);
    }

    @Override
    public void removerUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new EntidadeNaoEncontradaException(
                    "Não foi possível remover o usuário com id " + id + ". Nenhum usuário encontrado com esse id.");
        }
        usuarioRepository.deleteById(id);
    }

    @Override
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .map(usuarioMapper::toDomain)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Usuário com id " + id + " não encontrado. Verifique se o id informado está correto."));
    }

    @Override
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .map(usuarioMapper::toDomain)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Usuário com o email '" + email + "' não encontrado. Verifique se o email informado está correto."));
    }

    @Override
    public List<Usuario> ListarTodos() {
        return usuarioRepository.findAll().stream().map(usuarioMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Usuario atualizarUsuario(Usuario usuario, Long id) {
        UsuarioEntity usuarioEntity = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Não foi possível atualizar. Usuário com id " + id + " não encontrado."));

        usuarioEntity.setNome(usuario.getNome());
        usuarioEntity.setEmail(usuario.getEmail());

        if (usuario.getNovaSenha() != null && !usuario.getNovaSenha().isBlank()) {
            usuarioEntity.setSenha(passwordEncoder.encode(usuario.getNovaSenha()));
        }

        return usuarioMapper.toDomain(usuarioRepository.save(usuarioEntity));
    }
}
