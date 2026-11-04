package es.sergioe.gestionaula.controller;


import es.sergioe.gestionaula.model.Aula;
import es.sergioe.gestionaula.repository.AulaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/aulas")
public class AulaController {

    private final AulaRepository aulaRepository;

    public AulaController(AulaRepository aulaRepository) {
        this.aulaRepository = aulaRepository;
    }

    @GetMapping
    public List<Aula> listar(
            @RequestParam(required = false) Boolean ordenadores,
            @RequestParam(required = false) Integer capacidad) {

        if (ordenadores != null && capacidad != null) {
            return aulaRepository.findByOrdenadores(ordenadores)
                    .stream()
                    .filter(aula -> aula.getCapacidad() != null
                            && aula.getCapacidad() > capacidad)
                    .toList();
        }

        if (ordenadores != null) {
            return aulaRepository.findByOrdenadores(ordenadores);
        }

        if (capacidad != null) {
            return aulaRepository.findByCapacidadGreaterThan(capacidad);
        }

        return aulaRepository.findAll();
    }

    @GetMapping("/{id}")
    public Aula obtenerPorId(@PathVariable Long id) {
        return aulaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Aula no encontrada"
                ));
    }

    @PostMapping
    public ResponseEntity<Aula> insertar(@RequestBody Aula aula) {
        Aula nueva = aulaRepository.save(aula);

        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @PutMapping("/{id}")
    public Aula actualizar(
            @PathVariable Long id,
            @RequestBody Aula datos) {

        Aula aula = aulaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Aula no encontrada"
                ));

        aula.setNombre(datos.getNombre());
        aula.setCapacidad(datos.getCapacidad());
        aula.setOrdenadores(datos.getOrdenadores());

        return aulaRepository.save(aula);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        if (!aulaRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Aula no encontrada"
            );
        }

        aulaRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}