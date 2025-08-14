package com.almoxarifado.entity;

import com.almoxarifado.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    private Role role;

    public String primeiroNomeFormatado() {
        String nomeUsuario = this.nome.trim();
        int espaco = nomeUsuario.indexOf(" ");
        if (espaco > 0) {
            nomeUsuario = nomeUsuario.substring(0, espaco);
        }

        nomeUsuario = nomeUsuario.substring(0, 1).toUpperCase() +
                nomeUsuario.substring(1).toLowerCase();

        return nomeUsuario;
    }

}
