package com.almoxarifado.enums;

public enum TipoEventoArmario {
    TRANCA("Tranca"),
    DESTRANCA("Destranca");

    private final String label;

    TipoEventoArmario(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
