package edu.ucaldas.clima.controllers;

import edu.ucaldas.clima.domain.EstacionService;
import edu.ucaldas.clima.dto.ConfiguracionDTO;
import edu.ucaldas.clima.dto.EstacionDTO;
import edu.ucaldas.clima.model.Estacion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estaciones")
public class EstacionController {

    private final EstacionService estacionService;

    public EstacionController(EstacionService estacionService) {
        this.estacionService = estacionService;
    }

    @GetMapping
    public List<Estacion> listar() {
        return estacionService.listar();
    }

    @GetMapping("/{id}")
    public Estacion buscar(@PathVariable Long id) {
        return estacionService.buscar(id);
    }

    @GetMapping("/ciudad/{id}")
    public List<Estacion> listarPorCiudad(@PathVariable Long id) {
        return estacionService.listarPorCiudad(id);
    }

    @PostMapping
    public ResponseEntity<Estacion> crear(@RequestBody EstacionDTO dto) {
        Estacion creada = estacionService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    public Estacion actualizar(@PathVariable Long id, @RequestBody EstacionDTO dto) {
        return estacionService.actualizar(id, dto);
    }

    @PutMapping("/{id}/configuracion")
    public Estacion actualizarConfiguracion(@PathVariable Long id, @RequestBody ConfiguracionDTO dto) {
        return estacionService.actualizarConfiguracion(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        estacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

