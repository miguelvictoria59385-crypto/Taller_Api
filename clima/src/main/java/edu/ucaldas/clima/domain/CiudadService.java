package edu.ucaldas.clima.domain;

import edu.ucaldas.clima.dto.CiudadDTO;
import edu.ucaldas.clima.exceptions.RecursoNoEncontradoException;
import edu.ucaldas.clima.exceptions.ReglaNegocioException;
import edu.ucaldas.clima.model.Ciudad;
import edu.ucaldas.clima.persistance.CiudadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CiudadService {

    private final CiudadRepository ciudadRepository;

    public CiudadService(CiudadRepository ciudadRepository) {
        this.ciudadRepository = ciudadRepository;
    }

    public List<Ciudad> listar() {
        return ciudadRepository.findAll();
    }

    public Ciudad buscar(Long id) {
        return ciudadRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Ciudad no encontrada con id: " + id));
    }

    @Transactional
    public Ciudad crear(CiudadDTO dto) {
        validarDatos(dto);
        Ciudad ciudad = new Ciudad(dto.getNombre().trim(), dto.getDepartamento().trim(), dto.getPoblacion());
        return ciudadRepository.save(ciudad);
    }

    @Transactional
    public Ciudad actualizar(Long id, CiudadDTO dto) {
        Ciudad existente = buscar(id);
        validarDatos(dto);
        existente.setNombre(dto.getNombre().trim());
        existente.setDepartamento(dto.getDepartamento().trim());
        existente.setPoblacion(dto.getPoblacion());
        return ciudadRepository.save(existente);
    }

    @Transactional
    public void eliminar(Long id) {
        Ciudad existente = buscar(id);
        ciudadRepository.delete(existente);
    }

    private void validarDatos(CiudadDTO dto) {
        if (dto == null || dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new ReglaNegocioException("El nombre de la ciudad es obligatorio");
        }
        if (dto.getDepartamento() == null || dto.getDepartamento().isBlank()) {
            throw new ReglaNegocioException("El departamento de la ciudad es obligatorio");
        }
        if (dto.getPoblacion() != null && dto.getPoblacion() < 0) {
            throw new ReglaNegocioException("La población no puede ser negativa");
        }
    }
}

