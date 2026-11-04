package es.sergioe.gestionaula.controller;

import es.sergioe.gestionaula.model.Aula;
import es.sergioe.gestionaula.model.Profesor;
import es.sergioe.gestionaula.repository.ProfesorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/profesores")
public class ProfersorController {
private final ProfesorRepository profesorRepository;

    public ProfersorController(ProfesorRepository profesorRepository) {
        this.profesorRepository = profesorRepository;
    }

    @GetMapping
    public List<Profesor> Listar(){
        return profesorRepository.findAll();
    }
    @GetMapping("/{id}")
    public Profesor obtenerPorId(@PathVariable Long id) {
        return profesorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Profesor no encontrado"
                ));
    }
    @PostMapping
    public ResponseEntity<Profesor> insertar(@RequestBody Profesor profesor) {
        Profesor nuevo = profesorRepository.save(profesor);

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PutMapping("/{id}")
    public Profesor actualizar(
            @PathVariable Long id,
            @RequestBody Profesor datos) {

        Profesor profesor = profesorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Profesor no encontrado"
                ));

        profesor.setNombre(datos.getNombre());
        profesor.setApellidos(datos.getApellidos());
        profesor.setEmail(datos.getEmail());

        return profesorRepository.save(profesor);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        if (!profesorRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Profesor no encontrado"
            );
        }

        profesorRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
