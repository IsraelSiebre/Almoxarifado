package com.almoxarifado.repository;

import com.almoxarifado.entity.TipoItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TipoItemRepository extends JpaRepository<TipoItem, Long> {
    Optional<TipoItem> findByNome(String nome);

}
