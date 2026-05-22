package com.barbearia.fahcortes.infra.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "tb_plano")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlanoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String descricao;

    private BigDecimal preco;

    @ElementCollection
    @CollectionTable(name = "tb_plano_beneficios", joinColumns = @JoinColumn(name = "plano_id"))
    @Column(name = "beneficio")
    private List<String> beneficios;

    private Integer validade;
}
