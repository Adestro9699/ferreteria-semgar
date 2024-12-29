package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Almacen {

    public enum EstadoAlmacen { //en POSTMAN usar una String
        ACTIVO, //0
        INACTIVO //1
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idAlmacen;

    @Column(length = 100, nullable = false)
    private String nombre;

    @Column(columnDefinition = "text")
    private String ubicacion;

    @Column(length = 15)
    private String telefono;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoAlmacen estadoAlmacen;

    @Column(precision = 10, scale = 2)
    private BigDecimal capacidadMaxima;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column
    private LocalDateTime fechaModificacion;

    //Llave foránea a Inventario
    @ManyToOne
    @JoinColumn(name = "idInventario", nullable = false)
    private Inventario inventario;

    //Llave foránea a Usuario
    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

    public Almacen(Integer idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    public Almacen(Integer idAlmacen, String nombre, String ubicacion, String telefono, EstadoAlmacen estadoAlmacen,
                   BigDecimal capacidadMaxima, LocalDateTime fechaModificacion, LocalDateTime fechaCreacion,
                   Inventario inventario, Usuario usuario) {
        this.idAlmacen = idAlmacen;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.telefono = telefono;
        this.estadoAlmacen = estadoAlmacen;
        this.capacidadMaxima = capacidadMaxima;
        this.fechaModificacion = fechaModificacion;
        this.fechaCreacion = fechaCreacion;
        this.inventario = inventario;
        this.usuario = usuario;
    }

    public Integer getIdAlmacen() {
        return idAlmacen;
    }

    public void setIdAlmacen(Integer idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public EstadoAlmacen getEstadoAlmacen() {
        return estadoAlmacen;
    }

    public void setEstadoAlmacen(EstadoAlmacen estadoAlmacen) {
        this.estadoAlmacen = estadoAlmacen;
    }

    public BigDecimal getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public void setCapacidadMaxima(BigDecimal capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Inventario getInventario() {
        return inventario;
    }

    public void setInventario(Inventario inventario) {
        this.inventario = inventario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
