package edu.ucaldas.clima.domain;

import edu.ucaldas.clima.dto.TipoMedicionDTO;
import edu.ucaldas.clima.exceptions.RecursoNoEncontradoException;
import edu.ucaldas.clima.exceptions.ReglaNegocioException;
import edu.ucaldas.clima.model.TipoMedicion;
import edu.ucaldas.clima.persistance.TipoMedicionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TipoMedicionService {

    private final TipoMedicionRepository tipoMedicionRepository;

    public TipoMedicionService(TipoMedicionRepository tipoMedicionRepository) {
        this.tipoMedicionRepository = tipoMedicionRepository;
    }

    public List<TipoMedicion> listar() {
        return tipoMedicionRepository.findAll();
    }

    public TipoMedicion buscar(Long id) {
        return tipoMedicionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Tipo de medición no encontrado con id: " + id));
    }

    @Transactional
    public TipoMedicion crear(TipoMedicionDTO dto) {
        validarDatos(dto, null);
        TipoMedicion tipo = new TipoMedicion(dto.getNombre().trim(), dto.getUnidad().trim(), dto.getDescripcion());
        return tipoMedicionRepository.save(tipo);
    }

    @Transactional
    public TipoMedicion actualizar(Long id, TipoMedicionDTO dto) {
        TipoMedicion existente = buscar(id);
        validarDatos(dto, id);
        existente.setNombre(dto.getNombre().trim());
        existente.setUnidad(dto.getUnidad().trim());
        existente.setDescripcion(dto.getDescripcion());
        return tipoMedicionRepository.save(existente);
    }

    @Transactional
    public void eliminar(Long id) {
        TipoMedicion existente = buscar(id);
        tipoMedicionRepository.delete(existente);
    }

    private void validarDatos(TipoMedicionDTO dto, Long idActual) {
        if (dto == null || dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new ReglaNegocioException("El nombre del tipo de medición es obligatorio");
        }
        if (dto.getUnidad() == null || dto.getUnidad().isBlank()) {
            throw new ReglaNegocioException("La unidad del tipo de medición es obligatoria");
        }
        tipoMedicionRepository.findByNombreIgnoreCase(dto.getNombre().trim())
                .ifPresent(otro -> {
                    if (idActual == null || !otro.getId().equals(idActual)) {
                        throw new ReglaNegocioException("Ya existe un tipo de medición con el nombre: " + dto.getNombre());
                    }
                });
    }
}

