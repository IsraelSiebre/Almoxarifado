package com.almoxarifado.repository;

import com.almoxarifado.entity.EventoArmario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArmarioRepository extends JpaRepository<EventoArmario, Long> {
}
