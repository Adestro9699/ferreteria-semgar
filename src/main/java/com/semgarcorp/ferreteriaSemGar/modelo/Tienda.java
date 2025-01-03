package com.semgarcorp.ferreteriaSemGar.modelo;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Tienda {

    public enum EstadoTienda {
        ACTIVO,
        INACTIVO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idTienda;

    @NotNull
    @Column(length = 100)
    private String nombre;

    @Column(length = 255)
    private String direccion;

    @Column(length = 20)
    private String telefono;

    @Enumerated(EnumType.STRING)
    @Column
    private EstadoTienda estadoTienda;

    @NotNull
    private LocalDate fechaCreacion;

    private LocalDate fechaModificacion;

    @ManyToMany(mappedBy = "tiendas") // Relación inversa
    private List<Almacen> almacenes = new ArrayList<>();

    public Tienda() {
    }

    public Tienda(Integer idTienda, String nombre, String direccion, String telefono, EstadoTienda estadoTienda,
                  LocalDate fechaCreacion, LocalDate fechaModificacion, List<Almacen> almacenes) {
        this.idTienda = idTienda;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.estadoTienda = estadoTienda;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
        this.almacenes = almacenes;
    }

    public Integer getIdTienda() {
        return idTienda;
    }

    public void setIdTienda(Integer idTienda) {
        this.idTienda = idTienda;
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

    public EstadoTienda getEstadoTienda() {
        return estadoTienda;
    }

    public void setEstadoTienda(EstadoTienda estadoTienda) {
        this.estadoTienda = estadoTienda;
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

    public List<Almacen> getAlmacenes() {
        return almacenes;
    }

    public void setAlmacenes(List<Almacen> almacenes) {
        this.almacenes = almacenes;
    }
}

