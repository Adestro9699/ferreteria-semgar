package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Acceso {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idAcceso;

    @NotNull(message = "El rol no puede ser nulo")
    @Size(min = 3, max = 150, message = "El rol debe tener entre 3 y 150 caracteres")
    @Column(length = 150)
    private String rol;

    @NotNull(message = "Los permisos no pueden ser nulos")
    @Size(min = 3, max = 150, message = "Los permisos deben tener entre 3 y 150 caracteres")
    @Column(length = 150)
    private String permisos;

    @ManyToOne
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;

    // Constructor vacío
    public Acceso() {
    }

    // Constructor con parámetros
    public Acceso(Integer idAcceso, String rol, String permisos, Usuario usuario) {
        this.idAcceso = idAcceso;
        this.rol = rol;
        this.permisos = permisos;
        this.usuario = usuario;
    }

    // Getters y setters
    public Integer getIdAcceso() {
        return idAcceso;
    }

    public void setIdAcceso(Integer idAcceso) {
        this.idAcceso = idAcceso;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getPermisos() {
        return permisos;
    }

    public void setPermisos(String permisos) {
        this.permisos = permisos;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
