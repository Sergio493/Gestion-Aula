package es.sergioe.gestionaula.service;

import es.sergioe.gestionaula.model.Aula;
import es.sergioe.gestionaula.model.FranjaHoraria;
import es.sergioe.gestionaula.model.Profesor;
import es.sergioe.gestionaula.model.Reserva;
import es.sergioe.gestionaula.repository.AulaRepository;
import es.sergioe.gestionaula.repository.FranjaHorariaRepository;
import es.sergioe.gestionaula.repository.ProfesorRepository;
import es.sergioe.gestionaula.repository.ReservaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final AulaRepository aulaRepository;
    private final ProfesorRepository profesorRepository;
    private final FranjaHorariaRepository franjaHorariaRepository;

    public ReservaService(
            ReservaRepository reservaRepository,
            AulaRepository aulaRepository,
            ProfesorRepository profesorRepository,
            FranjaHorariaRepository franjaHorariaRepository) {

        this.reservaRepository = reservaRepository;
        this.aulaRepository = aulaRepository;
        this.profesorRepository = profesorRepository;
        this.franjaHorariaRepository = franjaHorariaRepository;
    }

    public List<Reserva> listar() {
        return reservaRepository.findAll();
    }

    public Reserva obtenerPorId(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Reserva no encontrada"
                ));
    }

    @Transactional
    public Reserva insertar(Reserva reserva) {

        validarFecha(reserva);
        comprobarReferencias(reserva);
        comprobarDisponibilidad(reserva, null);

        return reservaRepository.save(reserva);
    }

    @Transactional
    public Reserva actualizar(Long id, Reserva datos) {

        Reserva reserva = obtenerPorId(id);

        validarFecha(datos);
        comprobarReferencias(datos);
        comprobarDisponibilidad(datos, id);

        reserva.setFecha(datos.getFecha());
        reserva.setAula(datos.getAula());
        reserva.setProfesor(datos.getProfesor());
        reserva.setFranjaHoraria(datos.getFranjaHoraria());

        return reservaRepository.save(reserva);
    }

    public void eliminar(Long id) {

        Reserva reserva = obtenerPorId(id);

        reservaRepository.delete(reserva);
    }

    private void validarFecha(Reserva reserva) {

        if (reserva.getFecha() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La fecha es obligatoria"
            );
        }

        if (reserva.getFecha().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La fecha de la reserva no puede ser anterior a hoy"
            );
        }
    }

    private void comprobarReferencias(Reserva reserva) {

        if (reserva.getAula() == null
                || reserva.getAula().getId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Debes seleccionar un aula"
            );
        }

        if (reserva.getProfesor() == null
                || reserva.getProfesor().getId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Debes seleccionar un profesor"
            );
        }

        if (reserva.getFranjaHoraria() == null
                || reserva.getFranjaHoraria().getId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Debes seleccionar una franja horaria"
            );
        }

        Aula aula = aulaRepository.findById(
                reserva.getAula().getId()
        ).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "El aula no existe"
        ));

        Profesor profesor = profesorRepository.findById(
                reserva.getProfesor().getId()
        ).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "El profesor no existe"
        ));

        FranjaHoraria franja = franjaHorariaRepository.findById(
                reserva.getFranjaHoraria().getId()
        ).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "La franja horaria no existe"
        ));

        if (franja.getHoraInicio() == null
                || franja.getHoraFin() == null
                || !franja.getHoraInicio().isBefore(franja.getHoraFin())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La franja horaria no tiene un horario válido"
            );
        }

        reserva.setAula(aula);
        reserva.setProfesor(profesor);
        reserva.setFranjaHoraria(franja);
    }

    private void comprobarDisponibilidad(Reserva reserva, Long id) {

        Long aulaId = reserva.getAula().getId();
        Long franjaId = reserva.getFranjaHoraria().getId();

        boolean ocupada;

        if (id == null) {
            ocupada = reservaRepository
                    .existsByAulaIdAndFechaAndFranjaHorariaId(
                            aulaId,
                            reserva.getFecha(),
                            franjaId
                    );
        } else {
            ocupada = reservaRepository
                    .existsByAulaIdAndFechaAndFranjaHorariaIdAndIdNot(
                            aulaId,
                            reserva.getFecha(),
                            franjaId,
                            id
                    );
        }

        if (ocupada) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El aula ya está reservada para esa fecha y franja horaria"
            );
        }
    }
}
