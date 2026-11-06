package edu.ucaldas.clima.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "configuraciones_estacion")
public class Configuracion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer intervaloMedicionMinutos = 15;

    @Column(nullable = false, length = 20)
    private String unidadTemperatura = "CELSIUS";

    private Double alturaMetros;

    private Boolean calibracionAutomatica = true;

    @JsonIgnore
    @OneToOne(mappedBy = "configuracion", fetch = FetchType.LAZY)
    private Estacion estacion;

    public Configuracion() {
    }

    public Configuracion(Integer intervaloMedicionMinutos, String unidadTemperatura, Double alturaMetros, Boolean calibracionAutomatica) {
        this.intervaloMedicionMinutos = intervaloMedicionMinutos != null ? intervaloMedicionMinutos : 15;
        this.unidadTemperatura = unidadTemperatura != null ? unidadTemperatura : "CELSIUS";
        this.alturaMetros = alturaMetros;
        this.calibracionAutomatica = calibracionAutomatica != null ? calibracionAutomatica : true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIntervaloMedicionMinutos() {
        return intervaloMedicionMinutos;
    }

    public void setIntervaloMedicionMinutos(Integer intervaloMedicionMinutos) {
        this.intervaloMedicionMinutos = intervaloMedicionMinutos;
    }

    public String getUnidadTemperatura() {
        return unidadTemperatura;
    }

    public void setUnidadTemperatura(String unidadTemperatura) {
        this.unidadTemperatura = unidadTemperatura;
    }

    public Double getAlturaMetros() {
        return alturaMetros;
    }

    public void setAlturaMetros(Double alturaMetros) {
        this.alturaMetros = alturaMetros;
    }

    public Boolean getCalibracionAutomatica() {
        return calibracionAutomatica;
    }

    public void setCalibracionAutomatica(Boolean calibracionAutomatica) {
        this.calibracionAutomatica = calibracionAutomatica;
    }

    public Estacion getEstacion() {
        return estacion;
    }

    public void setEstacion(Estacion estacion) {
        this.estacion = estacion;
    }
}
