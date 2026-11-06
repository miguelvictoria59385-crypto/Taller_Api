package edu.ucaldas.clima.domain;

import edu.ucaldas.clima.dto.RolDTO;
import edu.ucaldas.clima.exceptions.RecursoNoEncontradoException;
import edu.ucaldas.clima.exceptions.ReglaNegocioException;
import edu.ucaldas.clima.model.Rol;
import edu.ucaldas.clima.persistance.RolRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RolService {

    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    public List<Rol> listar() {
        return rolRepository.findAll();
    }

    public Rol buscar(Long id) {
        return rolRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Rol no encontrado con id: " + id));
    }

    @Transactional
    public Rol crear(RolDTO dto) {
        validarDatos(dto, null);
        Rol rol = new Rol(dto.getNombre().trim().toUpperCase(), dto.getDescripcion());
        return rolRepository.save(rol);
    }

    @Transactional
    public Rol actualizar(Long id, RolDTO dto) {
        Rol existente = buscar(id);
        validarDatos(dto, id);
        existente.setNombre(dto.getNombre().trim().toUpperCase());
        existente.setDescripcion(dto.getDescripcion());
        return rolRepository.save(existente);
    }

    @Transactional
    public void eliminar(Long id) {
        Rol existente = buscar(id);
        rolRepository.delete(existente);
    }

    private void validarDatos(RolDTO dto, Long idActual) {
        if (dto == null || dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new ReglaNegocioException("El nombre del rol es obligatorio");
        }
        rolRepository.findByNombreIgnoreCase(dto.getNombre().trim())
                .ifPresent(otro -> {
                    if (idActual == null || !otro.getId().equals(idActual)) {
                        throw new ReglaNegocioException("Ya existe un rol con el nombre: " + dto.getNombre());
                    }
                });
    }
}

