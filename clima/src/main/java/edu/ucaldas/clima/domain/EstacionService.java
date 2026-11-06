package edu.ucaldas.clima.domain;

import edu.ucaldas.clima.dto.ConfiguracionDTO;
import edu.ucaldas.clima.dto.EstacionDTO;
import edu.ucaldas.clima.exceptions.RecursoNoEncontradoException;
import edu.ucaldas.clima.exceptions.ReglaNegocioException;
import edu.ucaldas.clima.model.Ciudad;
import edu.ucaldas.clima.model.Configuracion;
import edu.ucaldas.clima.model.Estacion;
import edu.ucaldas.clima.persistance.EstacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EstacionService {

    private final EstacionRepository estacionRepository;
    private final CiudadService ciudadService;

    public EstacionService(EstacionRepository estacionRepository, CiudadService ciudadService) {
        this.estacionRepository = estacionRepository;
        this.ciudadService = ciudadService;
    }

    public List<Estacion> listar() {
        return estacionRepository.findAll();
    }

    public Estacion buscar(Long id) {
        return estacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estación no encontrada con id: " + id));
    }

    public List<Estacion> listarPorCiudad(Long ciudadId) {
        ciudadService.buscar(ciudadId);
        return estacionRepository.findByCiudadId(ciudadId);
    }

    @Transactional
    public Estacion crear(EstacionDTO dto) {
        validarDatos(dto, null);
        Ciudad ciudad = ciudadService.buscar(dto.getCiudadId());
        Configuracion config = mapearConfiguracion(dto.getConfiguracion());
        Estacion estacion = new Estacion(
                dto.getNombre().trim(),
                dto.getCodigo().trim(),
                dto.getUbicacion(),
                dto.getEstado() != null && !dto.getEstado().isBlank() ? dto.getEstado().trim().toUpperCase() : "ACTIVA",
                ciudad,
                config
        );
        return estacionRepository.save(estacion);
    }

    @Transactional
    public Estacion actualizar(Long id, EstacionDTO dto) {
        Estacion existente = buscar(id);
        validarDatos(dto, id);
        Ciudad ciudad = ciudadService.buscar(dto.getCiudadId());

        existente.setNombre(dto.getNombre().trim());
        existente.setCodigo(dto.getCodigo().trim());
        existente.setUbicacion(dto.getUbicacion());
        if (dto.getEstado() != null && !dto.getEstado().isBlank()) {
            existente.setEstado(dto.getEstado().trim().toUpperCase());
        }
        existente.setCiudad(ciudad);

        if (dto.getConfiguracion() != null) {
            aplicarConfiguracion(existente, dto.getConfiguracion());
        }

        return estacionRepository.save(existente);
    }

    @Transactional
    public Estacion actualizarConfiguracion(Long estacionId, ConfiguracionDTO configDto) {
        Estacion estacion = buscar(estacionId);
        if (configDto == null) {
            throw new ReglaNegocioException("Los datos de configuración son obligatorios");
        }
        aplicarConfiguracion(estacion, configDto);
        return estacionRepository.save(estacion);
    }

    @Transactional
    public void eliminar(Long id) {
        Estacion existente = buscar(id);
        estacionRepository.delete(existente);
    }

    private void validarDatos(EstacionDTO dto, Long idActual) {
        if (dto == null || dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new ReglaNegocioException("El nombre de la estación es obligatorio");
        }
        if (dto.getCodigo() == null || dto.getCodigo().isBlank()) {
            throw new ReglaNegocioException("El código de la estación es obligatorio");
        }
        if (dto.getCiudadId() == null) {
            throw new ReglaNegocioException("El ciudadId es obligatorio para asociar la estación a una ciudad");
        }
        estacionRepository.findByCodigoIgnoreCase(dto.getCodigo().trim())
                .ifPresent(otra -> {
                    if (idActual == null || !otra.getId().equals(idActual)) {
                        throw new ReglaNegocioException("Ya existe una estación con el código: " + dto.getCodigo());
                    }
                });
    }

    private Configuracion mapearConfiguracion(ConfiguracionDTO dto) {
        if (dto == null) {
            return null;
        }
        return new Configuracion(
                dto.getIntervaloMedicionMinutos(),
                dto.getUnidadTemperatura(),
                dto.getAlturaMetros(),
                dto.getCalibracionAutomatica()
        );
    }

    private void aplicarConfiguracion(Estacion estacion, ConfiguracionDTO dto) {
        Configuracion config = estacion.getConfiguracion();
        if (config == null) {
            config = mapearConfiguracion(dto);
            estacion.setConfiguracion(config);
        } else {
            if (dto.getIntervaloMedicionMinutos() != null) {
                config.setIntervaloMedicionMinutos(dto.getIntervaloMedicionMinutos());
            }
            if (dto.getUnidadTemperatura() != null && !dto.getUnidadTemperatura().isBlank()) {
                config.setUnidadTemperatura(dto.getUnidadTemperatura().trim().toUpperCase());
            }
            if (dto.getAlturaMetros() != null) {
                config.setAlturaMetros(dto.getAlturaMetros());
            }
            if (dto.getCalibracionAutomatica() != null) {
                config.setCalibracionAutomatica(dto.getCalibracionAutomatica());
            }
        }
    }
}

