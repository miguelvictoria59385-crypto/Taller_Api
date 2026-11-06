package edu.ucaldas.clima.controllers;

import edu.ucaldas.clima.domain.RolService;
import edu.ucaldas.clima.dto.RolDTO;
import edu.ucaldas.clima.model.Rol;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RolController {

    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @GetMapping
    public List<Rol> listar() {
        return rolService.listar();
    }

    @GetMapping("/{id}")
    public Rol buscar(@PathVariable Long id) {
        return rolService.buscar(id);
    }

    @PostMapping
    public ResponseEntity<Rol> crear(@RequestBody RolDTO dto) {
        Rol creado = rolService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public Rol actualizar(@PathVariable Long id, @RequestBody RolDTO dto) {
        return rolService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        rolService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

