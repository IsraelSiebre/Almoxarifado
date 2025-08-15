package com.almoxarifado.repository;

import com.almoxarifado.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findByDescricao(String descricao);
    List<Item> findAllByAtivoTrue();
}
