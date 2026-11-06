package edu.ucaldas.clima.dto;

import java.time.LocalDateTime;

public class AlertaDTO {
    private String descripcion;
    private String nivel;
    private LocalDateTime fecha;
    private String estado;
    private Long registroClimaId;

    public AlertaDTO() {
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Long getRegistroClimaId() {
        return registroClimaId;
    }

    public void setRegistroClimaId(Long registroClimaId) {
        this.registroClimaId = registroClimaId;
    }
}
