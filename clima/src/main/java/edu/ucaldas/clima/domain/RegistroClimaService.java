package edu.ucaldas.clima.domain;

import edu.ucaldas.clima.dto.RegistroClimaDTO;
import edu.ucaldas.clima.exceptions.RecursoNoEncontradoException;
import edu.ucaldas.clima.exceptions.ReglaNegocioException;
import edu.ucaldas.clima.model.Ciudad;
import edu.ucaldas.clima.model.Estacion;
import edu.ucaldas.clima.model.RegistroClima;
import edu.ucaldas.clima.model.TipoMedicion;
import edu.ucaldas.clima.persistance.EstacionRepository;
import edu.ucaldas.clima.persistance.RegistroClimaRepository;
import edu.ucaldas.clima.persistance.TipoMedicionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class RegistroClimaService {

    private final RegistroClimaRepository repository;
    private final EstacionRepository estacionRepository;
    private final CiudadService ciudadService;
    private final TipoMedicionRepository tipoMedicionRepository;

    public RegistroClimaService(
            RegistroClimaRepository repository,
            EstacionRepository estacionRepository,
            CiudadService ciudadService,
            TipoMedicionRepository tipoMedicionRepository
    ) {
        this.repository = repository;
        this.estacionRepository = estacionRepository;
        this.ciudadService = ciudadService;
        this.tipoMedicionRepository = tipoMedicionRepository;
    }

    public List<RegistroClima> listar() {
        return repository.findAll();
    }

    public RegistroClima buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Registro climático no encontrado con id: " + id));
    }

    public List<RegistroClima> listarPorEstacion(Long estacionId) {
        if (!estacionRepository.existsById(estacionId)) {
            throw new RecursoNoEncontradoException("Estación no encontrada con id: " + estacionId);
        }
        return repository.findByEstacionId(estacionId);
    }

    public List<RegistroClima> listarPorCiudad(Long ciudadId) {
        ciudadService.buscar(ciudadId);
        return repository.findByEstacionCiudadId(ciudadId);
    }

    public List<RegistroClima> filtrarPorRangoFechas(LocalDateTime inicio, LocalDateTime fin, Long ciudadId) {
        if (inicio == null || fin == null) {
            throw new ReglaNegocioException("Las fechas de inicio y fin son obligatorias");
        }
        if (fin.isBefore(inicio)) {
            throw new ReglaNegocioException("La fecha fin no puede ser anterior a la fecha inicio");
        }
        if (ciudadId != null) {
            ciudadService.buscar(ciudadId);
            return repository.findByEstacionCiudadIdAndFechaBetween(ciudadId, inicio, fin);
        }
        return repository.findByFechaBetween(inicio, fin);
    }

    public List<RegistroClima> obtenerMedicionesTemperaturaSuperior(Double umbral) {
        double limite = umbral != null ? umbral : 30.0;
        return repository.obtenerMedicionesSuperioresA(limite);
    }

    public Map<String, Object> obtenerTemperaturaPromedioCiudad(Long ciudadId) {
        Ciudad ciudad = ciudadService.buscar(ciudadId);
        Double promedio = repository.obtenerTemperaturaPromedio(ciudadId);
        Map<String, Object> resultado = new LinkedHashMap<>();
        resultado.put("ciudadId", ciudad.getId());
        resultado.put("ciudad", ciudad.getNombre());
        resultado.put("temperaturaPromedio", promedio != null ? Math.round(promedio * 100.0) / 100.0 : null);
        return resultado;
    }

    @Transactional
    public RegistroClima crear(RegistroClimaDTO dto) {
        validarDatos(dto);
        Estacion estacion = resolverEstacion(dto);

        RegistroClima registroClima = new RegistroClima(
                dto.getTemperatura(),
                dto.getHumedad(),
                dto.getPresion(),
                dto.getFecha() != null ? dto.getFecha() : LocalDateTime.now(),
                dto.getDescripcion(),
                estacion
        );

        if (dto.getTipoMedicionIds() != null && !dto.getTipoMedicionIds().isEmpty()) {
            registroClima.setTiposMedicion(cargarTiposMedicion(dto.getTipoMedicionIds()));
        }

        return repository.save(registroClima);
    }

    @Transactional
    public RegistroClima actualizar(Long id, RegistroClimaDTO dto) {
        RegistroClima existente = buscar(id);
        validarDatos(dto);

        existente.setTemperatura(dto.getTemperatura());
        existente.setHumedad(dto.getHumedad());
        if (dto.getPresion() != null) {
            existente.setPresion(dto.getPresion());
        }
        if (dto.getFecha() != null) {
            existente.setFecha(dto.getFecha());
        }
        if (dto.getDescripcion() != null) {
            existente.setDescripcion(dto.getDescripcion());
        }

        Estacion estacion = resolverEstacion(dto);
        if (estacion != null) {
            existente.setEstacion(estacion);
        }

        if (dto.getTipoMedicionIds() != null) {
            existente.setTiposMedicion(cargarTiposMedicion(dto.getTipoMedicionIds()));
        }

        return repository.save(existente);
    }

    @Transactional
    public RegistroClima asociarTipoMedicion(Long registroId, Long tipoMedicionId) {
        RegistroClima registro = buscar(registroId);
        TipoMedicion tipo = tipoMedicionRepository.findById(tipoMedicionId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Tipo de medición no encontrado con id: " + tipoMedicionId));
        registro.getTiposMedicion().add(tipo);
        return repository.save(registro);
    }

    @Transactional
    public void eliminar(Long id) {
        RegistroClima existente = buscar(id);
        repository.delete(existente);
    }

    private void validarDatos(RegistroClimaDTO dto) {
        if (dto == null) {
            throw new ReglaNegocioException("Los datos del registro climático son obligatorios");
        }
        if (dto.getTemperatura() == null) {
            throw new ReglaNegocioException("La temperatura es obligatoria");
        }
        if (dto.getHumedad() == null || dto.getHumedad() < 0 || dto.getHumedad() > 100) {
            throw new ReglaNegocioException("La humedad es obligatoria y debe estar entre 0 y 100");
        }
    }

    private Estacion resolverEstacion(RegistroClimaDTO dto) {
        if (dto.getEstacionId() != null) {
            return estacionRepository.findById(dto.getEstacionId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Estación no encontrada con id: " + dto.getEstacionId()));
        }
        if (dto.getCiudadId() != null) {
            Ciudad ciudad = ciudadService.buscar(dto.getCiudadId());
            List<Estacion> estaciones = estacionRepository.findByCiudadId(ciudad.getId());
            if (!estaciones.isEmpty()) {
                return estaciones.get(0);
            }
            Estacion estacionDefault = new Estacion(
                    "Estación Principal " + ciudad.getNombre(),
                    "EST-CIU-" + ciudad.getId(),
                    "Centro Urbano",
                    "ACTIVA",
                    ciudad,
                    null
            );
            return estacionRepository.save(estacionDefault);
        }
        return null;
    }

    private Set<TipoMedicion> cargarTiposMedicion(Set<Long> ids) {
        Set<TipoMedicion> tipos = new HashSet<>();
        for (Long tipoId : ids) {
            TipoMedicion tipo = tipoMedicionRepository.findById(tipoId)
                    .orElseThrow(() -> new RecursoNoEncontradoException("Tipo de medición no encontrado con id: " + tipoId));
            tipos.add(tipo);
        }
        return tipos;
    }
}
// Crear, actualizar y eliminar completan el CRUD
