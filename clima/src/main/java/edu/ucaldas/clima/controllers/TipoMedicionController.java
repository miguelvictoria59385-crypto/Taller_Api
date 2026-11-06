package edu.ucaldas.clima.controllers;

import edu.ucaldas.clima.domain.TipoMedicionService;
import edu.ucaldas.clima.dto.TipoMedicionDTO;
import edu.ucaldas.clima.model.TipoMedicion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-medicion")
public class TipoMedicionController {

    private final TipoMedicionService tipoMedicionService;

    public TipoMedicionController(TipoMedicionService tipoMedicionService) {
        this.tipoMedicionService = tipoMedicionService;
    }

    @GetMapping
    public List<TipoMedicion> listar() {
        return tipoMedicionService.listar();
    }

    @GetMapping("/{id}")
    public TipoMedicion buscar(@PathVariable Long id) {
        return tipoMedicionService.buscar(id);
    }

    @PostMapping
    public ResponseEntity<TipoMedicion> crear(@RequestBody TipoMedicionDTO dto) {
        TipoMedicion creado = tipoMedicionService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public TipoMedicion actualizar(@PathVariable Long id, @RequestBody TipoMedicionDTO dto) {
        return tipoMedicionService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        tipoMedicionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

