package edu.ucaldas.clima.dto;

import java.time.LocalDateTime;
import java.util.Set;

public class RegistroClimaDTO {

    private Double temperatura;
    private Integer humedad;
    private Double presion;
    private LocalDateTime fecha;
    private String descripcion;
    private Long estacionId;
    private Long ciudadId;
    private Set<Long> tipoMedicionIds;

    public RegistroClimaDTO() {
    }

    public Double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Double temperatura) {
        this.temperatura = temperatura;
    }

    public Integer getHumedad() {
        return humedad;
    }

    public void setHumedad(Integer humedad) {
        this.humedad = humedad;
    }

    public Double getPresion() {
        return presion;
    }

    public void setPresion(Double presion) {
        this.presion = presion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getEstacionId() {
        return estacionId;
    }

    public void setEstacionId(Long estacionId) {
        this.estacionId = estacionId;
    }

    public Long getCiudadId() {
        return ciudadId;
    }

    public void setCiudadId(Long ciudadId) {
        this.ciudadId = ciudadId;
    }

    public Set<Long> getTipoMedicionIds() {
        return tipoMedicionIds;
    }

    public void setTipoMedicionIds(Set<Long> tipoMedicionIds) {
        this.tipoMedicionIds = tipoMedicionIds;
    }
}
