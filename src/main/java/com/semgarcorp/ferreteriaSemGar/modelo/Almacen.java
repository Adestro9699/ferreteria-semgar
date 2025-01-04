package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Almacen {

    public enum EstadoAlmacen {
        ACTIVO,
        INACTIVO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idAlmacen;

    @NotNull
    @Column(length = 100)
    private String nombre; // VARCHAR(100)

    @NotNull
    @Column(length = 200)
    private String ubicacion; // VARCHAR(200)

    @Column(length = 20)
    private String telefono; // VARCHAR(20)

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private EstadoAlmacen estadoAlmacen; // Enum definido dentro de la clase

    @NotNull
    @Column(length = 100)
    private String capacidadMaxima; // VARCHAR(100)

    @NotNull
    @Column
    private LocalDate fechaCreacion;

    @Column
    private LocalDate fechaModificacion;

    // Relación ManyToOne con Inventario
    @ManyToOne
    @JoinColumn(name = "idInventario", referencedColumnName = "idInventario")
    private Inventario inventario;

    // Relación ManyToOne con Usuario
    @ManyToOne
    @JoinColumn(name = "idUsuario", referencedColumnName = "idUsuario")
    private Usuario usuario;

    // Relación ManyToMany con Tienda
    @ManyToMany
    @JoinTable(
            name = "Almacen_Tienda", // Nombre de la tabla intermedia
            joinColumns = @JoinColumn(name = "id_almacen"), // FK hacia Almacen
            inverseJoinColumns = @JoinColumn(name = "id_tienda") // FK hacia Tienda
    )
    private List<Tienda> tiendas = new ArrayList<>();

    public Almacen() {
    }

    public Almacen(Integer idAlmacen, String nombre, String ubicacion, EstadoAlmacen estadoAlmacen,
                   LocalDate fechaCreacion, Inventario inventario, Usuario usuario, List<Tienda> tiendas,
                   LocalDate fechaModificacion, String capacidadMaxima, String telefono) {
        this.idAlmacen = idAlmacen;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.estadoAlmacen = estadoAlmacen;
        this.fechaCreacion = fechaCreacion;
        this.inventario = inventario;
        this.usuario = usuario;
        this.tiendas = tiendas;
        this.fechaModificacion = fechaModificacion;
        this.capacidadMaxima = capacidadMaxima;
        this.telefono = telefono;
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Tienda> getTiendas() {
        return tiendas;
    }

    public void setTiendas(List<Tienda> tiendas) {
        this.tiendas = tiendas;
    }

    public Inventario getInventario() {
        return inventario;
    }

    public void setInventario(Inventario inventario) {
        this.inventario = inventario;
    }

    public String getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public void setCapacidadMaxima(String capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    public EstadoAlmacen getEstadoAlmacen() {
        return estadoAlmacen;
    }

    public void setEstadoAlmacen(EstadoAlmacen estadoAlmacen) {
        this.estadoAlmacen = estadoAlmacen;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
}
