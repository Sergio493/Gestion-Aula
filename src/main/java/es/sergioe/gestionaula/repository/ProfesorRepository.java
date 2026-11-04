package es.sergioe.gestionaula.repository;

import es.sergioe.gestionaula.model.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfesorRepository extends JpaRepository<Profesor, Long> {
}