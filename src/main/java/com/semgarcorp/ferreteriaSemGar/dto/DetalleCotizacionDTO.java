package com.semgarcorp.ferreteriaSemGar.dto;

import java.math.BigDecimal;

public class DetalleCotizacionDTO {

    private Integer idDetalleCotizacion;
    private Integer idCotizacion;
    private Integer idProducto;
    private String nombreProducto;
    private String unidadMedida;
    private BigDecimal cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal descuento;
    private BigDecimal subtotal;
    private BigDecimal subtotalSinIGV;
    private BigDecimal igvAplicado;

    public DetalleCotizacionDTO() {
    }

    public DetalleCotizacionDTO(Integer idDetalleCotizacion, Integer idCotizacion, Integer idProducto,
                                String nombreProducto, String unidadMedida, BigDecimal cantidad,
                                BigDecimal precioUnitario, BigDecimal descuento, BigDecimal subtotal,
                                BigDecimal subtotalSinIGV, BigDecimal igvAplicado) {
        this.idDetalleCotizacion = idDetalleCotizacion;
        this.idCotizacion = idCotizacion;
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.unidadMedida = unidadMedida;
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

    public Integer getIdCotizacion() {
        return idCotizacion;
    }

    public void setIdCotizacion(Integer idCotizacion) {
        this.idCotizacion = idCotizacion;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
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
