package edu.ucaldas.clima.controllers;

import edu.ucaldas.clima.domain.RegistroClimaService;
import edu.ucaldas.clima.dto.RegistroClimaDTO;
import edu.ucaldas.clima.model.RegistroClima;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/api/registros-clima", "/api/mediciones"})
public class RegistroClimaController {
    private final RegistroClimaService service;

    public RegistroClimaController(RegistroClimaService service) {
        this.service = service;
    }

    @GetMapping
    public List<RegistroClima> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public RegistroClima buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @GetMapping("/estacion/{id}")
    public List<RegistroClima> listarPorEstacion(@PathVariable Long id) {
        return service.listarPorEstacion(id);
    }

    @GetMapping("/ciudad/{id}")
    public List<RegistroClima> listarPorCiudad(@PathVariable Long id) {
        return service.listarPorCiudad(id);
    }

    @GetMapping("/rango-fechas")
    public List<RegistroClima> filtrarPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin,
            @RequestParam(required = false) Long ciudadId
    ) {
        return service.filtrarPorRangoFechas(inicio, fin, ciudadId);
    }

    @GetMapping("/temperatura-alta")
    public List<RegistroClima> obtenerTemperaturaAlta(@RequestParam(required = false, defaultValue = "30.0") Double min) {
        return service.obtenerMedicionesTemperaturaSuperior(min);
    }

    @GetMapping("/promedio/ciudad/{ciudadId}")
    public Map<String, Object> obtenerPromedioPorCiudad(@PathVariable Long ciudadId) {
        return service.obtenerTemperaturaPromedioCiudad(ciudadId);
    }

    @PostMapping({"", "/"})
    public ResponseEntity<RegistroClima> crear(@RequestBody RegistroClimaDTO registroClima) {
        RegistroClima creado = service.crear(registroClima);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PostMapping("/{id}/tipos-medicion/{tipoId}")
    public RegistroClima asociarTipoMedicion(@PathVariable Long id, @PathVariable Long tipoId) {
        return service.asociarTipoMedicion(id, tipoId);
    }

    @PutMapping("/{id}")
    public RegistroClima actualizar(@PathVariable Long id, @RequestBody RegistroClimaDTO registroClima) {
        return service.actualizar(id, registroClima);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
    // Agregar POST, PUT /{id} y DELETE /{id}
}
