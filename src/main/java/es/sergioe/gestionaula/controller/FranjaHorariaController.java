package es.sergioe.gestionaula.controller;

import es.sergioe.gestionaula.model.FranjaHoraria;
import es.sergioe.gestionaula.repository.FranjaHorariaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/franjas-horarias")
public class FranjaHorariaController {

    private final FranjaHorariaRepository franjaRepository;

    public FranjaHorariaController(
            FranjaHorariaRepository franjaRepository) {
        this.franjaRepository = franjaRepository;
    }

    @GetMapping
    public List<FranjaHoraria> listar() {
        return franjaRepository.findAll();
    }

    @GetMapping("/{id}")
    public FranjaHoraria obtenerPorId(@PathVariable Long id) {
        return franjaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Franja horaria no encontrada"
                ));
    }

    @PostMapping
    public ResponseEntity<FranjaHoraria> insertar(
            @RequestBody FranjaHoraria franja) {

        validarHorario(franja);

        FranjaHoraria nueva = franjaRepository.save(franja);

        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @PutMapping("/{id}")
    public FranjaHoraria actualizar(
            @PathVariable Long id,
            @RequestBody FranjaHoraria datos) {

        FranjaHoraria franja = franjaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Franja horaria no encontrada"
                ));

        validarHorario(datos);

        franja.setHoraInicio(datos.getHoraInicio());
        franja.setHoraFin(datos.getHoraFin());

        return franjaRepository.save(franja);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        if (!franjaRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Franja horaria no encontrada"
            );
        }

        franjaRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    private void validarHorario(FranjaHoraria franja) {

        if (franja.getHoraInicio() == null
                || franja.getHoraFin() == null
                || !franja.getHoraInicio().isBefore(franja.getHoraFin())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La hora de inicio debe ser anterior a la hora de fin"
            );
        }
    }
}


