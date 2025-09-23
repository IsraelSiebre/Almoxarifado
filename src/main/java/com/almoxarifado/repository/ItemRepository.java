package com.almoxarifado.repository;

import com.almoxarifado.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long>, JpaSpecificationExecutor<Item> {
    Optional<Item> findByDescricao(String descricao);
    List<Item> findAllByAtivoTrue();

    @Query("SELECT i FROM Item i WHERE i.quantidade < 5")
    List<Item> findItensAbaixoDoEstoqueMinimo();

    @Query("SELECT DISTINCT i.quantidade FROM Item i WHERE i.ativo = true ORDER BY i.quantidade")
    List<Integer> findDistinctQuantidades();

    @Query("SELECT DISTINCT i.unidadeDeMedida FROM Item i WHERE i.ativo = true ORDER BY i.unidadeDeMedida")
    List<String> findDistinctUnidades();

    @Query("SELECT DISTINCT i.marca FROM Item i WHERE i.ativo = true ORDER BY i.marca")
    List<String> findDistinctMarcas();

    @Query("SELECT DISTINCT i.localArmazenado FROM Item i WHERE i.ativo = true ORDER BY i.localArmazenado")
    List<String> findDistinctLocais();

}
