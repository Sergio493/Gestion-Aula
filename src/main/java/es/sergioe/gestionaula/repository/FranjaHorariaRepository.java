package es.sergioe.gestionaula.repository;

import es.sergioe.gestionaula.model.FranjaHoraria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FranjaHorariaRepository
        extends JpaRepository<FranjaHoraria, Long> {
}