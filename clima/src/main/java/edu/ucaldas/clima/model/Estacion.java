package edu.ucaldas.clima.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "estaciones")
public class Estacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 50)
    private String codigo;

    @Column(length = 150)
    private String ubicacion;

    @Column(nullable = false, length = 30)
    private String estado = "ACTIVA";

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "ciudad_id", nullable = false)
    private Ciudad ciudad;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "configuracion_id", unique = true)
    private Configuracion configuracion;

    @JsonIgnore
    @OneToMany(mappedBy = "estacion", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RegistroClima> registros = new ArrayList<>();

    public Estacion() {
    }

    public Estacion(String nombre, String codigo, String ubicacion, String estado, Ciudad ciudad, Configuracion configuracion) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.ubicacion = ubicacion;
        this.estado = estado != null ? estado : "ACTIVA";
        this.ciudad = ciudad;
        this.configuracion = configuracion;
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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    public Configuracion getConfiguracion() {
        return configuracion;
    }

    public void setConfiguracion(Configuracion configuracion) {
        this.configuracion = configuracion;
    }

    public List<RegistroClima> getRegistros() {
        return registros;
    }

    public void setRegistros(List<RegistroClima> registros) {
        this.registros = registros;
    }
}
