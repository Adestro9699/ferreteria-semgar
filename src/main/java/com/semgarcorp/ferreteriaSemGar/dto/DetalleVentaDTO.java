package com.semgarcorp.ferreteriaSemGar.dto;

import java.math.BigDecimal;

public class DetalleVentaDTO {

    private Integer idDetalleVenta;
    private Integer idVenta; // Solo el ID de la venta
    private Integer idProducto; // Solo el ID del producto
    private BigDecimal cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal descuento;
    private BigDecimal subtotalSinImpuestos;
    private BigDecimal impuesto;

    // Constructor vacío
    public DetalleVentaDTO() {
    }

    // Constructor con todos los campos
    public DetalleVentaDTO(Integer idDetalleVenta, Integer idVenta, Integer idProducto, BigDecimal cantidad,
                           BigDecimal precioUnitario, BigDecimal descuento, BigDecimal subtotalSinImpuestos, BigDecimal impuesto) {
        this.idDetalleVenta = idDetalleVenta;
        this.idVenta = idVenta;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.descuento = descuento;
        this.subtotalSinImpuestos = subtotalSinImpuestos;
        this.impuesto = impuesto;
    }

    // Getters y Setters
    public Integer getIdDetalleVenta() {
        return idDetalleVenta;
    }

    public void setIdDetalleVenta(Integer idDetalleVenta) {
        this.idDetalleVenta = idDetalleVenta;
    }

    public Integer getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Integer idVenta) {
        this.idVenta = idVenta;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
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

    public BigDecimal getSubtotalSinImpuestos() {
        return subtotalSinImpuestos;
    }

    public void setSubtotalSinImpuestos(BigDecimal subtotalSinImpuestos) {
        this.subtotalSinImpuestos = subtotalSinImpuestos;
    }

    public BigDecimal getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(BigDecimal impuesto) {
        this.impuesto = impuesto;
    }
}