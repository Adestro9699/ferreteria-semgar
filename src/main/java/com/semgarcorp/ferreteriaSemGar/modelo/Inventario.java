package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;

import java.time.LocalDateTime;

public class Inventario {

    public enum EstadoInventario { //en POSTMAN usar una String
        ACTIVO, //0
        INACTIVO //1
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idInventario;

    @Column(length = 255, nullable = false)
    private String nombreInventario;

    @Column(length = 255, nullable = false)
    private String ubicacion;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoInventario estadoInventario;

    @Column(columnDefinition = "text")
    private String observaciones;

    public Inventario(Integer idInventario) {
        this.idInventario = idInventario;
    }

    public Inventario(Integer idInventario, String nombreInventario, String ubicacion, LocalDateTime fechaCreacion,
                      EstadoInventario estadoInventario, String observaciones) {
        this.idInventario = idInventario;
        this.nombreInventario = nombreInventario;
        this.ubicacion = ubicacion;
        this.fechaCreacion = fechaCreacion;
        this.estadoInventario = estadoInventario;
        this.observaciones = observaciones;
    }

    public Integer getIdInventario() {
        return idInventario;
    }

    public void setIdInventario(Integer idInventario) {
        this.idInventario = idInventario;
    }

    public String getNombreInventario() {
        return nombreInventario;
    }

    public void setNombreInventario(String nombreInventario) {
        this.nombreInventario = nombreInventario;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public EstadoInventario getEstadoInventario() {
        return estadoInventario;
    }

    public void setEstadoInventario(EstadoInventario estadoInventario) {
        this.estadoInventario = estadoInventario;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
