package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sucursales")
public class Sucursal {

    public enum EstadoSucursal {
        ACTIVO,
        INACTIVO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idSucursal;

    @NotNull
    @Column(length = 100)
    private String nombre;

    @Column(length = 255)
    private String direccion;

    @Column(length = 20)
    private String telefono;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private EstadoSucursal estadoSucursal;

    @NotNull
    private LocalDate fechaCreacion;

    private LocalDate fechaModificacion;

    @Column(length = 500)
    private String observaciones;

    // Relación con Almacenes (una sucursal puede tener varios almacenes)
    @OneToMany(mappedBy = "sucursal")
    private List<Almacen> almacenes = new ArrayList<>();

    public Sucursal() {
    }

    public Sucursal(Integer idSucursal, String nombre, String direccion, String telefono, 
                   EstadoSucursal estadoSucursal, LocalDate fechaCreacion, 
                   LocalDate fechaModificacion, String observaciones) {
        this.idSucursal = idSucursal;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.estadoSucursal = estadoSucursal;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
        this.observaciones = observaciones;
    }

    public Integer getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(Integer idSucursal) {
        this.idSucursal = idSucursal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public EstadoSucursal getEstadoSucursal() {
        return estadoSucursal;
    }

    public void setEstadoSucursal(EstadoSucursal estadoSucursal) {
        this.estadoSucursal = estadoSucursal;
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

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<Almacen> getAlmacenes() {
        return almacenes;
    }

    public void setAlmacenes(List<Almacen> almacenes) {
        this.almacenes = almacenes;
    }
}

