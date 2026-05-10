package com.barbearia.fahcortes.infra.entities;

import com.barbearia.fahcortes.domain.entities.usuario.UsuarioEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "tb_usuario")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message =  "nome esta em branco")
    private String nome;
    @NotBlank
    private String email;
    @NotBlank
    private String senha;
    @Enumerated(EnumType.STRING)
    private UsuarioEnum role;
}
