package com.semgarcorp.ferreteriaSemGar.dto;

import java.math.BigDecimal;

public class DetalleVentaDTO {

    private Integer idDetalleVenta;
    private Integer idVenta;
    private Integer idProducto;
    private BigDecimal cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal descuento;
    private BigDecimal subtotalSinIGV;
    private BigDecimal igvAplicado;

    public DetalleVentaDTO() {
    }

    public DetalleVentaDTO(Integer idDetalleVenta, Integer idVenta, Integer idProducto, BigDecimal cantidad,
                           BigDecimal precioUnitario, BigDecimal descuento, BigDecimal subtotalSinIGV, BigDecimal igvAplicado) {
        this.idDetalleVenta = idDetalleVenta;
        this.idVenta = idVenta;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.descuento = descuento;
        this.subtotalSinIGV = subtotalSinIGV;
        this.igvAplicado = igvAplicado;
    }

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