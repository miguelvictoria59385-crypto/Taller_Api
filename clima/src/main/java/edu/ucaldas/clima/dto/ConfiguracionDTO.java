package edu.ucaldas.clima.dto;

public class ConfiguracionDTO {
    private Integer intervaloMedicionMinutos;
    private String unidadTemperatura;
    private Double alturaMetros;
    private Boolean calibracionAutomatica;

    public ConfiguracionDTO() {
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
}
