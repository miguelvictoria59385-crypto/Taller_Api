package edu.ucaldas.clima.controllers;

import edu.ucaldas.clima.domain.AlertaService;
import edu.ucaldas.clima.dto.AlertaDTO;
import edu.ucaldas.clima.model.Alerta;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alertas")
public class AlertaController {

    private final AlertaService alertaService;

    public AlertaController(AlertaService alertaService) {
        this.alertaService = alertaService;
    }

    @GetMapping
    public List<Alerta> listar() {
        return alertaService.listar();
    }

    @GetMapping("/{id}")
    public Alerta buscar(@PathVariable Long id) {
        return alertaService.buscar(id);
    }

    @GetMapping("/nivel/{nivel}")
    public List<Alerta> listarPorNivel(@PathVariable String nivel) {
        return alertaService.listarPorNivel(nivel);
    }

    @GetMapping("/activas")
    public List<Alerta> listarActivas() {
        return alertaService.listarActivas();
    }

    @GetMapping("/activas/ciudad/{ciudadId}")
    public List<Alerta> listarActivasPorCiudad(@PathVariable Long ciudadId) {
        return alertaService.listarActivasPorCiudad(ciudadId);
    }

    @PostMapping
    public ResponseEntity<Alerta> crear(@RequestBody AlertaDTO dto) {
        Alerta creada = alertaService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    public Alerta actualizar(@PathVariable Long id, @RequestBody AlertaDTO dto) {
        return alertaService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        alertaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

