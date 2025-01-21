package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private String nombreInventario; //nombre del Inventario

    @NotNull(message = "La ubicación no puede ser nula")
    @Size(min = 3, max = 255, message = "La ubicación debe tener entre 3 y 255 caracteres")
    @Column(length = 255)
    private String ubicacion; //ubicacion del inventario

    @NotNull(message = "La fecha de creación no puede ser nula")
    private LocalDate fechaCreacion; //fecha en la que se creó el inventario

    @Enumerated(EnumType.STRING)
    @NotNull(message = "El estado del inventario no puede ser nulo")
    @Column(length = 10)
    private EstadoInventario estadoInventario; //estado ACTIVO o INACTIVO del inventario

    @Column(columnDefinition = "TEXT")
    private String observaciones; //campo TEXT para observaciones

    @OneToMany(mappedBy = "inventario")
    private List<InventarioProducto> inventarioProductos = new ArrayList<>();

    // Constructor vacío
    public Inventario() {
    }

    public Inventario(Integer idInventario, String nombreInventario, String ubicacion, LocalDate fechaCreacion,
                      EstadoInventario estadoInventario, String observaciones, List<InventarioProducto> inventarioProductos) {
        this.idInventario = idInventario;
        this.nombreInventario = nombreInventario;
        this.ubicacion = ubicacion;
        this.fechaCreacion = fechaCreacion;
        this.estadoInventario = estadoInventario;
        this.observaciones = observaciones;
        this.inventarioProductos = inventarioProductos;
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

    public List<InventarioProducto> getInventarioProductos() {
        return inventarioProductos;
    }

    public void setInventarioProductos(List<InventarioProducto> inventarioProductos) {
        this.inventarioProductos = inventarioProductos;
    }
}
