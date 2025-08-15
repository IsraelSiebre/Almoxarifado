package com.almoxarifado.dto;

import com.almoxarifado.enums.TipoMovimentacao;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovimentacaoDto {

    @NotNull
    private Long itemId;

    @NotNull
    private TipoMovimentacao tipo;

    @Min(1)
    private int quantidade;

    private String observacoes;
}
