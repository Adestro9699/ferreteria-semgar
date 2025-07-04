package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "inventarios")
public class Inventario {

    public enum EstadoInventario {
        EN_PROCESO,    // Conteo en curso
        FINALIZADO,    // Conteo completado
        APROBADO,      // Conteo revisado y aprobado
        RECHAZADO      // Conteo con discrepancias
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idInventario;

    @NotNull(message = "La fecha de conteo no puede ser nula")
    @Column(name = "fecha_conteo", nullable = false)
    private LocalDate fechaConteo;

    @NotNull(message = "El responsable no puede ser nulo")
    @Column(name = "responsable", length = 100, nullable = false)
    private String responsable;

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "El estado del inventario no puede ser nulo")
    @Column(name = "estado", length = 20, nullable = false)
    private EstadoInventario estadoInventario;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDate fechaCreacion;

    @Column(name = "fecha_modificacion")
    private LocalDate fechaModificacion;

    // Relación con Almacen (en qué almacén se realiza el conteo)
    @ManyToOne
    @JoinColumn(name = "idAlmacen", nullable = false)
    private Almacen almacen;

    // Relación con DetalleInventario (detalles del conteo)
    @OneToMany(mappedBy = "inventario", cascade = CascadeType.ALL)
    private List<DetalleInventario> detalles = new ArrayList<>();

    public Inventario() {
    }

    public Inventario(Integer idInventario, LocalDate fechaConteo, String responsable, 
                     String observaciones, EstadoInventario estadoInventario,
                     LocalDate fechaCreacion, LocalDate fechaModificacion, 
                     Almacen almacen) {
        this.idInventario = idInventario;
        this.fechaConteo = fechaConteo;
        this.responsable = responsable;
        this.observaciones = observaciones;
        this.estadoInventario = estadoInventario;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
        this.almacen = almacen;
    }

    public Integer getIdInventario() {
        return idInventario;
    }

    public void setIdInventario(Integer idInventario) {
        this.idInventario = idInventario;
    }

    public LocalDate getFechaConteo() {
        return fechaConteo;
    }

    public void setFechaConteo(LocalDate fechaConteo) {
        this.fechaConteo = fechaConteo;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public EstadoInventario getEstadoInventario() {
        return estadoInventario;
    }

    public void setEstadoInventario(EstadoInventario estadoInventario) {
        this.estadoInventario = estadoInventario;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDate getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDate fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Almacen getAlmacen() {
        return almacen;
    }

    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
    }

    public List<DetalleInventario> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleInventario> detalles) {
        this.detalles = detalles;
    }
} 