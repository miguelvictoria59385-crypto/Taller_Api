package edu.ucaldas.clima.dto;

public class CiudadDTO {
    private String nombre;
    private String departamento;
    private Long poblacion;

    public CiudadDTO() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public Long getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(Long poblacion) {
        this.poblacion = poblacion;
    }
}
