package edu.ucaldas.clima.controllers;

import edu.ucaldas.clima.domain.UsuarioService;
import edu.ucaldas.clima.dto.UsuarioDTO;
import edu.ucaldas.clima.model.Usuario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<Usuario> listar() {
        return usuarioService.listar();
    }

    @GetMapping("/{id}")
    public Usuario buscar(@PathVariable Long id) {
        return usuarioService.buscar(id);
    }

    @GetMapping("/rol/{nombre}")
    public List<Usuario> buscarPorRol(@PathVariable String nombre) {
        return usuarioService.buscarPorRol(nombre);
    }

    @PostMapping
    public ResponseEntity<Usuario> crear(@RequestBody UsuarioDTO dto) {
        Usuario creado = usuarioService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PostMapping("/{id}/roles/{rolId}")
    public Usuario asignarRol(@PathVariable Long id, @PathVariable Long rolId) {
        return usuarioService.asignarRol(id, rolId);
    }

    @DeleteMapping("/{id}/roles/{rolId}")
    public Usuario removerRol(@PathVariable Long id, @PathVariable Long rolId) {
        return usuarioService.removerRol(id, rolId);
    }

    @PutMapping("/{id}")
    public Usuario actualizar(@PathVariable Long id, @RequestBody UsuarioDTO dto) {
        return usuarioService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

