package com.barbearia.fahcortes.infra.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_barbeiro")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BarbeiroEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String especialidade;

    private String foto;

    private Integer curtidas;

    private Boolean ativo;
}
