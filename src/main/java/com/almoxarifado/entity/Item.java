package com.almoxarifado.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "itens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private int quantidade;

    @Column(nullable = false)
    private String unidadeDeMedida;

    @Column(nullable = false)
    private String marca;

    @Column(nullable = false)
    private String localArmazenado;

    @ManyToOne
    @JoinColumn(name = "tipo_id")
    private TipoItem tipo;

    @Column
    private boolean ativo = true;

    @Column
    private String observacoes;

}
