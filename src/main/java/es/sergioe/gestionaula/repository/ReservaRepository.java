package es.sergioe.gestionaula.repository;

import es.sergioe.gestionaula.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    boolean existsByAulaIdAndFechaAndFranjaHorariaId(
            Long aulaId,
            LocalDate fecha,
            Long franjaHorariaId
    );

    boolean existsByAulaIdAndFechaAndFranjaHorariaIdAndIdNot(
            Long aulaId,
            LocalDate fecha,
            Long franjaHorariaId,
            Long id
    );
}