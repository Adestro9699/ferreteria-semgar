package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Entity
public class Inventario {

    // Enum para los estados de inventario
    public enum EstadoInventario {
        ACTIVO,
        INACTIVO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idInventario;

    @NotNull(message = "El nombre del inventario no puede ser nulo")
    @Size(min = 3, max = 255, message = "El nombre del inventario debe tener entre 3 y 255 caracteres")
    @Column(length = 255)
    private String nombreInventario;  // VARCHAR(255)

    @NotNull(message = "La ubicación no puede ser nula")
    @Size(min = 3, max = 255, message = "La ubicación debe tener entre 3 y 255 caracteres")
    @Column(length = 255)
    private String ubicacion;  // VARCHAR(255)

    @NotNull(message = "La fecha de creación no puede ser nula")
    private LocalDate fechaCreacion;  // LocalDate

    @Enumerated(EnumType.STRING)
    @NotNull(message = "El estado del inventario no puede ser nulo")
    @Column(length = 10)
    private EstadoInventario estadoInventario;  // Enum de estado

    @Column(columnDefinition = "TEXT")
    private String observaciones;  // Campo TEXT para observaciones

    // Constructor vacío
    public Inventario() {
    }

    // Constructor completo
    public Inventario(Integer idInventario, String nombreInventario, String ubicacion, LocalDate fechaCreacion,
                      EstadoInventario estadoInventario, String observaciones) {
        this.idInventario = idInventario;
        this.nombreInventario = nombreInventario;
        this.ubicacion = ubicacion;
        this.fechaCreacion = fechaCreacion;
        this.estadoInventario = estadoInventario;
        this.observaciones = observaciones;
    }

    // Getters y setters
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

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
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
