package com.semgarcorp.ferreteriaSemGar.dto;

import java.util.Map;

public class UsuarioCompletoDTO {
    private String nombreTrabajador;
    private String cargoTrabajador;
    private String nombreUsuario;
    private String rol;
    private Map<String, Boolean> permisos;
    private Integer idAcceso; // Nuevo campo agregado

    // Constructor vacío
    public UsuarioCompletoDTO() {
    }

    // Constructor con parámetros (actualizado)
    public UsuarioCompletoDTO(String nombreTrabajador, String cargoTrabajador, String nombreUsuario, String rol, Map<String, Boolean> permisos, Integer idAcceso) {
        this.nombreTrabajador = nombreTrabajador;
        this.cargoTrabajador = cargoTrabajador;
        this.nombreUsuario = nombreUsuario;
        this.rol = rol;
        this.permisos = permisos;
        this.idAcceso = idAcceso; // Asignar el idAcceso
    }

    // Getters y Setters
    public String getNombreTrabajador() {
        return nombreTrabajador;
    }

    public void setNombreTrabajador(String nombreTrabajador) {
        this.nombreTrabajador = nombreTrabajador;
    }

    public String getCargoTrabajador() {
        return cargoTrabajador;
    }

    public void setCargoTrabajador(String cargoTrabajador) {
        this.cargoTrabajador = cargoTrabajador;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Map<String, Boolean> getPermisos() {
        return permisos;
    }

    public void setPermisos(Map<String, Boolean> permisos) {
        this.permisos = permisos;
    }

    // Getter y Setter para idAcceso
    public Integer getIdAcceso() {
        return idAcceso;
    }

    public void setIdAcceso(Integer idAcceso) {
        this.idAcceso = idAcceso;
    }
}