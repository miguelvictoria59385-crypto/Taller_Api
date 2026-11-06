package edu.ucaldas.clima.domain;

import edu.ucaldas.clima.dto.UsuarioDTO;
import edu.ucaldas.clima.exceptions.RecursoNoEncontradoException;
import edu.ucaldas.clima.exceptions.ReglaNegocioException;
import edu.ucaldas.clima.model.Rol;
import edu.ucaldas.clima.model.Usuario;
import edu.ucaldas.clima.persistance.RolRepository;
import edu.ucaldas.clima.persistance.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
    }

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Usuario buscar(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + id));
    }

    public List<Usuario> buscarPorRol(String nombreRol) {
        return usuarioRepository.buscarPorNombreRol(nombreRol);
    }

    @Transactional
    public Usuario crear(UsuarioDTO dto) {
        validarDatos(dto, null, true);
        Usuario usuario = new Usuario(
                dto.getNombre().trim(),
                dto.getCorreo().trim().toLowerCase(),
                dto.getContrasena()
        );
        if (dto.getRolIds() != null && !dto.getRolIds().isEmpty()) {
            usuario.setRoles(cargarRoles(dto.getRolIds()));
        }
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario actualizar(Long id, UsuarioDTO dto) {
        Usuario existente = buscar(id);
        validarDatos(dto, id, false);
        existente.setNombre(dto.getNombre().trim());
        existente.setCorreo(dto.getCorreo().trim().toLowerCase());
        if (dto.getContrasena() != null && !dto.getContrasena().isBlank()) {
            existente.setContrasena(dto.getContrasena());
        }
        if (dto.getRolIds() != null) {
            existente.setRoles(cargarRoles(dto.getRolIds()));
        }
        return usuarioRepository.save(existente);
    }

    @Transactional
    public Usuario asignarRol(Long usuarioId, Long rolId) {
        Usuario usuario = buscar(usuarioId);
        Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Rol no encontrado con id: " + rolId));
        usuario.getRoles().add(rol);
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario removerRol(Long usuarioId, Long rolId) {
        Usuario usuario = buscar(usuarioId);
        Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Rol no encontrado con id: " + rolId));
        usuario.getRoles().remove(rol);
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void eliminar(Long id) {
        Usuario existente = buscar(id);
        usuarioRepository.delete(existente);
    }

    private void validarDatos(UsuarioDTO dto, Long idActual, boolean exigirContrasena) {
        if (dto == null || dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new ReglaNegocioException("El nombre del usuario es obligatorio");
        }
        if (dto.getCorreo() == null || dto.getCorreo().isBlank() || !dto.getCorreo().contains("@")) {
            throw new ReglaNegocioException("Debe proporcionar un correo electrónico válido");
        }
        if (exigirContrasena && (dto.getContrasena() == null || dto.getContrasena().isBlank())) {
            throw new ReglaNegocioException("La contraseña del usuario es obligatoria");
        }
        usuarioRepository.findByCorreoIgnoreCase(dto.getCorreo().trim())
                .ifPresent(otro -> {
                    if (idActual == null || !otro.getId().equals(idActual)) {
                        throw new ReglaNegocioException("Ya existe un usuario con el correo: " + dto.getCorreo());
                    }
                });
    }

    private Set<Rol> cargarRoles(Set<Long> rolIds) {
        Set<Rol> roles = new HashSet<>();
        for (Long rolId : rolIds) {
            Rol rol = rolRepository.findById(rolId)
                    .orElseThrow(() -> new RecursoNoEncontradoException("Rol no encontrado con id: " + rolId));
            roles.add(rol);
        }
        return roles;
    }
}

