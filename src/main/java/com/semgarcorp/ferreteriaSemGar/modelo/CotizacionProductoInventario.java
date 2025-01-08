package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class CotizacionProductoInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCotizacionProductoInventario;

    private BigDecimal cantidad; // Refleja la cantidad de productos en una cotización

    @Column(precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(precision = 10, scale = 2)
    private BigDecimal descuento;

    @Column(precision = 10, scale = 2)
    private BigDecimal total; // Valor total de una cotización

    private LocalDate fechaModificacion;

    @ManyToOne
    @JoinColumn(name = "idCotizacion", nullable = false)
    private Cotizacion cotizacion;

    @ManyToOne
    @JoinColumn(name = "idInventarioProducto", nullable = false)
    private InventarioProducto inventarioProducto;

    public CotizacionProductoInventario() {
    }

    public CotizacionProductoInventario(Integer idCotizacionProductoInventario, BigDecimal cantidad, BigDecimal subtotal,
                                        BigDecimal descuento, BigDecimal total, LocalDate fechaModificacion,
                                        Cotizacion cotizacion, InventarioProducto inventarioProducto) {
        this.idCotizacionProductoInventario = idCotizacionProductoInventario;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
        this.descuento = descuento;
        this.total = total;
        this.fechaModificacion = fechaModificacion;
        this.cotizacion = cotizacion;
        this.inventarioProducto = inventarioProducto;
    }

    public Integer getIdCotizacionProductoInventario() {
        return idCotizacionProductoInventario;
    }

    public void setIdCotizacionProductoInventario(Integer idCotizacionProductoInventario) {
        this.idCotizacionProductoInventario = idCotizacionProductoInventario;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public LocalDate getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDate fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Cotizacion getCotizacion() {
        return cotizacion;
    }

    public void setCotizacion(Cotizacion cotizacion) {
        this.cotizacion = cotizacion;
    }

    public InventarioProducto getInventarioProducto() {
        return inventarioProducto;
    }

    public void setInventarioProducto(InventarioProducto inventarioProducto) {
        this.inventarioProducto = inventarioProducto;
    }
}
