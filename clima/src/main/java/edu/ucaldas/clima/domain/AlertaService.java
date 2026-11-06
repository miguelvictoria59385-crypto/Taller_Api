package edu.ucaldas.clima.domain;

import edu.ucaldas.clima.dto.AlertaDTO;
import edu.ucaldas.clima.exceptions.RecursoNoEncontradoException;
import edu.ucaldas.clima.exceptions.ReglaNegocioException;
import edu.ucaldas.clima.model.Alerta;
import edu.ucaldas.clima.model.RegistroClima;
import edu.ucaldas.clima.persistance.AlertaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlertaService {

    private final AlertaRepository alertaRepository;
    private final RegistroClimaService registroClimaService;
    private final CiudadService ciudadService;

    public AlertaService(AlertaRepository alertaRepository, RegistroClimaService registroClimaService, CiudadService ciudadService) {
        this.alertaRepository = alertaRepository;
        this.registroClimaService = registroClimaService;
        this.ciudadService = ciudadService;
    }

    public List<Alerta> listar() {
        return alertaRepository.findAll();
    }

    public Alerta buscar(Long id) {
        return alertaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Alerta no encontrada con id: " + id));
    }

    public List<Alerta> listarPorNivel(String nivel) {
        return alertaRepository.findByNivelIgnoreCase(nivel);
    }

    public List<Alerta> listarActivas() {
        return alertaRepository.findByEstadoIgnoreCase("ACTIVA");
    }

    public List<Alerta> listarActivasPorCiudad(Long ciudadId) {
        ciudadService.buscar(ciudadId);
        return alertaRepository.obtenerAlertasActivasPorCiudad(ciudadId);
    }

    @Transactional
    public Alerta crear(AlertaDTO dto) {
        validarDatos(dto);
        RegistroClima registro = registroClimaService.buscar(dto.getRegistroClimaId());
        Alerta alerta = new Alerta(
                dto.getDescripcion().trim(),
                dto.getNivel().trim().toUpperCase(),
                dto.getFecha() != null ? dto.getFecha() : LocalDateTime.now(),
                dto.getEstado() != null && !dto.getEstado().isBlank() ? dto.getEstado().trim().toUpperCase() : "ACTIVA",
                registro
        );
        return alertaRepository.save(alerta);
    }

    @Transactional
    public Alerta actualizar(Long id, AlertaDTO dto) {
        Alerta existente = buscar(id);
        validarDatos(dto);
        RegistroClima registro = registroClimaService.buscar(dto.getRegistroClimaId());

        existente.setDescripcion(dto.getDescripcion().trim());
        existente.setNivel(dto.getNivel().trim().toUpperCase());
        if (dto.getFecha() != null) {
            existente.setFecha(dto.getFecha());
        }
        if (dto.getEstado() != null && !dto.getEstado().isBlank()) {
            existente.setEstado(dto.getEstado().trim().toUpperCase());
        }
        existente.setRegistroClima(registro);
        return alertaRepository.save(existente);
    }

    @Transactional
    public void eliminar(Long id) {
        Alerta existente = buscar(id);
        alertaRepository.delete(existente);
    }

    private void validarDatos(AlertaDTO dto) {
        if (dto == null || dto.getDescripcion() == null || dto.getDescripcion().isBlank()) {
            throw new ReglaNegocioException("La descripción de la alerta es obligatoria");
        }
        if (dto.getNivel() == null || dto.getNivel().isBlank()) {
            throw new ReglaNegocioException("El nivel de la alerta es obligatorio (ej. BAJO, MEDIO, ALTO, CRITICO)");
        }
        if (dto.getRegistroClimaId() == null) {
            throw new ReglaNegocioException("El registroClimaId es obligatorio para asociar la alerta");
        }
    }
}

