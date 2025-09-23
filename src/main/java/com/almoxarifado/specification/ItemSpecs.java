package com.almoxarifado.specification;

import com.almoxarifado.entity.Item;
import org.springframework.data.jpa.domain.Specification;

public class ItemSpecs {

    public static Specification<Item> ativo() {
        return (root, query, cb) -> cb.isTrue(root.get("ativo"));
    }

    public static Specification<Item> comQuantidade(Integer quantidade) {
        return (root, query, cb) -> cb.equal(root.get("quantidade"), quantidade);
    }

    public static Specification<Item> comUnidade(String unidade) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("unidadeDeMedida")), "%" + unidade.toLowerCase() + "%");
    }

    public static Specification<Item> comMarca(String marca) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("marca")), "%" + marca.toLowerCase() + "%");
    }

    public static Specification<Item> comLocal(String local) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("localArmazenado")), "%" + local.toLowerCase() + "%");
    }

    public static Specification<Item> comTipo(Long tipoId) {
        return (root, query, cb) -> cb.equal(root.get("tipo").get("id"), tipoId);
    }
}
