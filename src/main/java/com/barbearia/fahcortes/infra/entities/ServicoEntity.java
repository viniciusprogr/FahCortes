package com.barbearia.fahcortes.infra.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name ="tb_serviço")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServicoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  id;

    private String descricao;

    private Long tempo;

    private BigDecimal valor;
}
