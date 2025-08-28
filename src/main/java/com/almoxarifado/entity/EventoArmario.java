package com.almoxarifado.entity;

import com.almoxarifado.enums.TipoEventoArmario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "eventos_armario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventoArmario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoEventoArmario tipo;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Usuario usuario;

}
