package com.barbearia.fahcortes.infra.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_unidade")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UnidadeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String endereco;

    private String telefone;

    private Double latitude;

    private Double longitude;

    private String horarioAbertura;

    private String horarioFechamento;
}
