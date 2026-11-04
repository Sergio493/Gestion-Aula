package es.sergioe.gestionaula.repository;

import es.sergioe.gestionaula.model.Aula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AulaRepository extends JpaRepository<Aula, Long> {

    List<Aula> findByOrdenadores(Boolean ordenadores);
    List<Aula> findByCapacidadGreaterThan(Integer capacidad);
}