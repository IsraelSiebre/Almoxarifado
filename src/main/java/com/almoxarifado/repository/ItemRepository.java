package com.almoxarifado.repository;

import com.almoxarifado.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findByDescricao(String descricao);
    List<Item> findAllByAtivoTrue();

    @Query("SELECT i FROM Item i WHERE i.quantidade < 5")
    List<Item> findItensAbaixoDoEstoqueMinimo();
}
