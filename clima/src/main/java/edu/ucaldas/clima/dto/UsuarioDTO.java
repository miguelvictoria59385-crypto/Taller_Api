package edu.ucaldas.clima.dto;

import java.util.Set;

public class UsuarioDTO {
    private String nombre;
    private String correo;
    private String contrasena;
    private Set<Long> rolIds;

    public UsuarioDTO() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Set<Long> getRolIds() {
        return rolIds;
    }

    public void setRolIds(Set<Long> rolIds) {
        this.rolIds = rolIds;
    }
}
