package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "almacenes")
public class Almacen {

    public enum EstadoAlmacen {
        ACTIVO,
        INACTIVO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idAlmacen;

    @NotNull(message = "El nombre del almacén no puede ser nulo")
    @Size(min = 3, max = 255, message = "El nombre del almacén debe tener entre 3 y 255 caracteres")
    @Column(length = 255)
    private String nombre; // Nombre del almacén

    @NotNull(message = "La ubicación no puede ser nula")
    @Size(min = 3, max = 255, message = "La ubicación debe tener entre 3 y 255 caracteres")
    @Column(length = 255)
    private String ubicacion; // Ubicación física del almacén

    @Column(length = 20)
    private String telefono; // Teléfono del almacén

    @NotNull(message = "El campo esPrincipal no puede ser nulo")
    @Column(name = "es_principal", nullable = false)
    private Boolean esPrincipal = false; // true = almacén principal (recibe compras), false = almacén de sucursal (recibe transferencias)

    @Enumerated(EnumType.STRING)
    @NotNull(message = "El estado del almacén no puede ser nulo")
    @Column(length = 10)
    private EstadoAlmacen estadoAlmacen; // ACTIVO o INACTIVO

    @NotNull(message = "La fecha de creación no puede ser nula")
    private LocalDate fechaCreacion; // Fecha en la que se creó el almacén

    private LocalDate fechaModificacion; // Fecha de última modificación

    @Column(length = 500)
    private String observaciones; // Observaciones adicionales

    // Relación con Sucursal (obligatoria - todos los almacenes pertenecen a una sucursal)
    @ManyToOne
    @JoinColumn(name = "idSucursal", nullable = false)
    private Sucursal sucursal; // A qué sucursal pertenece (tanto principales como de sucursal)

    // Relación con ProductoAlmacen (tabla puente para stock)
    @OneToMany(mappedBy = "almacen")
    private List<ProductoAlmacen> productoAlmacenes = new ArrayList<>();

    public Almacen() {
    }

    public Almacen(Integer idAlmacen, String nombre, String ubicacion, String telefono,
                   Boolean esPrincipal, EstadoAlmacen estadoAlmacen,
                   LocalDate fechaCreacion, LocalDate fechaModificacion, 
                   String observaciones, Sucursal sucursal) {
        this.idAlmacen = idAlmacen;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.telefono = telefono;
        this.esPrincipal = esPrincipal;
        this.estadoAlmacen = estadoAlmacen;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
        this.observaciones = observaciones;
        this.sucursal = sucursal;
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

    public Boolean getEsPrincipal() {
        return esPrincipal;
    }

    public void setEsPrincipal(Boolean esPrincipal) {
        this.esPrincipal = esPrincipal;
    }

    public EstadoAlmacen getEstadoAlmacen() {
        return estadoAlmacen;
    }

    public void setEstadoAlmacen(EstadoAlmacen estadoAlmacen) {
        this.estadoAlmacen = estadoAlmacen;
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

    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    public List<ProductoAlmacen> getProductoAlmacenes() {
        return productoAlmacenes;
    }

    public void setProductoAlmacenes(List<ProductoAlmacen> productoAlmacenes) {
        this.productoAlmacenes = productoAlmacenes;
    }
} 