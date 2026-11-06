package edu.ucaldas.clima.controllers;

import edu.ucaldas.clima.domain.CiudadService;
import edu.ucaldas.clima.domain.RegistroClimaService;
import edu.ucaldas.clima.dto.CiudadDTO;
import edu.ucaldas.clima.model.Ciudad;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ciudades")
public class CiudadController {

    private final CiudadService ciudadService;
    private final RegistroClimaService registroClimaService;

    public CiudadController(CiudadService ciudadService, RegistroClimaService registroClimaService) {
        this.ciudadService = ciudadService;
        this.registroClimaService = registroClimaService;
    }

    @GetMapping
    public List<Ciudad> listar() {
        return ciudadService.listar();
    }

    @GetMapping("/{id}")
    public Ciudad buscar(@PathVariable Long id) {
        return ciudadService.buscar(id);
    }

    @GetMapping("/{id}/temperatura-promedio")
    public Map<String, Object> obtenerTemperaturaPromedio(@PathVariable Long id) {
        return registroClimaService.obtenerTemperaturaPromedioCiudad(id);
    }

    @PostMapping
    public ResponseEntity<Ciudad> crear(@RequestBody CiudadDTO dto) {
        Ciudad creada = ciudadService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    public Ciudad actualizar(@PathVariable Long id, @RequestBody CiudadDTO dto) {
        return ciudadService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ciudadService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

