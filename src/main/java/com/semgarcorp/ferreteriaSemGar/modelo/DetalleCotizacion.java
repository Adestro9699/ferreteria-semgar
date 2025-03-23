package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class DetalleCotizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDetalleCotizacion;

    @ManyToOne
    @JoinColumn(name = "idCotizacion", nullable = false)
    private Cotizacion cotizacion;  // Relación con la entidad Cotizacion

    @ManyToOne
    @JoinColumn(name = "idProducto", nullable = false)
    private Producto producto;  // Relación con la entidad Producto

    @Column(precision = 10, scale = 2)
    private BigDecimal cantidad;

    @Column(precision = 10, scale = 2)
    private BigDecimal precioUnitario;  // Precio en el momento de la cotización

    @Column(precision = 10, scale = 2)
    private BigDecimal descuento;

    @Column(precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(precision = 10, scale = 2)
    private BigDecimal subtotalSinIGV;

    @Column(precision = 10, scale = 2)
    private BigDecimal igvAplicado;

    public DetalleCotizacion() {
    }

    public DetalleCotizacion(Integer idDetalleCotizacion, Cotizacion cotizacion, Producto producto,
                             BigDecimal cantidad, BigDecimal precioUnitario, BigDecimal descuento,
                             BigDecimal subtotal, BigDecimal subtotalSinIGV, BigDecimal igvAplicado) {
        this.idDetalleCotizacion = idDetalleCotizacion;
        this.cotizacion = cotizacion;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.descuento = descuento;
        this.subtotal = subtotal;
        this.subtotalSinIGV = subtotalSinIGV;
        this.igvAplicado = igvAplicado;
    }

    public Integer getIdDetalleCotizacion() {
        return idDetalleCotizacion;
    }

    public void setIdDetalleCotizacion(Integer idDetalleCotizacion) {
        this.idDetalleCotizacion = idDetalleCotizacion;
    }

    public Cotizacion getCotizacion() {
        return cotizacion;
    }

    public void setCotizacion(Cotizacion cotizacion) {
        this.cotizacion = cotizacion;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getSubtotalSinIGV() {
        return subtotalSinIGV;
    }

    public void setSubtotalSinIGV(BigDecimal subtotalSinIGV) {
        this.subtotalSinIGV = subtotalSinIGV;
    }

    public BigDecimal getIgvAplicado() {
        return igvAplicado;
    }

    public void setIgvAplicado(BigDecimal igvAplicado) {
        this.igvAplicado = igvAplicado;
    }
}