package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "producto_almacen")
public class ProductoAlmacen {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idProductoAlmacen;
    
    @Column(name = "stock", nullable = false)
    private Integer stock; // Stock operativo que cambia con operaciones
    
    @Column(name = "fecha_ultima_actualizacion")
    private LocalDate fechaUltimaActualizacion;
    
    @Column(name = "responsable_ultima_actualizacion", length = 100)
    private String responsableUltimaActualizacion;
    
    @ManyToOne
    @JoinColumn(name = "idProducto", nullable = false)
    private Producto producto;
    
    @ManyToOne
    @JoinColumn(name = "idAlmacen", nullable = false)
    private Almacen almacen;
    
    public ProductoAlmacen() {
    }
    
    public ProductoAlmacen(Integer idProductoAlmacen, Integer stock, 
                          LocalDate fechaUltimaActualizacion, String responsableUltimaActualizacion,
                          Producto producto, Almacen almacen) {
        this.idProductoAlmacen = idProductoAlmacen;
        this.stock = stock;
        this.fechaUltimaActualizacion = fechaUltimaActualizacion;
        this.responsableUltimaActualizacion = responsableUltimaActualizacion;
        this.producto = producto;
        this.almacen = almacen;
    }
    
    public Integer getIdProductoAlmacen() {
        return idProductoAlmacen;
    }
    
    public void setIdProductoAlmacen(Integer idProductoAlmacen) {
        this.idProductoAlmacen = idProductoAlmacen;
    }
    
    public Integer getStock() {
        return stock;
    }
    
    public void setStock(Integer stock) {
        this.stock = stock;
    }
    
    public LocalDate getFechaUltimaActualizacion() {
        return fechaUltimaActualizacion;
    }
    
    public void setFechaUltimaActualizacion(LocalDate fechaUltimaActualizacion) {
        this.fechaUltimaActualizacion = fechaUltimaActualizacion;
    }
    
    public String getResponsableUltimaActualizacion() {
        return responsableUltimaActualizacion;
    }
    
    public void setResponsableUltimaActualizacion(String responsableUltimaActualizacion) {
        this.responsableUltimaActualizacion = responsableUltimaActualizacion;
    }
    
    public Producto getProducto() {
        return producto;
    }
    
    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    
    public Almacen getAlmacen() {
        return almacen;
    }
    
    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
    }
} 