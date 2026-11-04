package es.sergioe.gestionaula.controller;


import es.sergioe.gestionaula.model.Reserva;
import es.sergioe.gestionaula.service.ReservaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping
    public List<Reserva> listar() {
        return reservaService.listar();
    }

    @GetMapping("/{id}")
    public Reserva obtenerPorId(@PathVariable Long id) {
        return reservaService.obtenerPorId(id);
    }

    @PostMapping
    public ResponseEntity<Reserva> insertar(
            @RequestBody Reserva reserva) {

        Reserva nueva = reservaService.insertar(reserva);

        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @PutMapping("/{id}")
    public Reserva actualizar(
            @PathVariable Long id,
            @RequestBody Reserva reserva) {

        return reservaService.actualizar(id, reserva);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        reservaService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}