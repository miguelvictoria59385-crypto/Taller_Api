package edu.ucaldas.clima.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tipos_medicion")
public class TipoMedicion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 80)
    private String nombre;

    @Column(nullable = false, length = 30)
    private String unidad;

    @Column(length = 200)
    private String descripcion;

    @JsonIgnore
    @ManyToMany(mappedBy = "tiposMedicion", fetch = FetchType.LAZY)
    private Set<RegistroClima> registros = new HashSet<>();

    public TipoMedicion() {
    }

    public TipoMedicion(String nombre, String unidad, String descripcion) {
        this.nombre = nombre;
        this.unidad = unidad;
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<RegistroClima> getRegistros() {
        return registros;
    }

    public void setRegistros(Set<RegistroClima> registros) {
        this.registros = registros;
    }
}
