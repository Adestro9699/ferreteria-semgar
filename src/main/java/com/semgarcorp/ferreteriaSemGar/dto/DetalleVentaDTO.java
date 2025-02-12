package com.semgarcorp.ferreteriaSemGar.dto;

import java.math.BigDecimal;

public class DetalleVentaDTO {

    private Integer idProducto;
    private String nombreProducto;
    private String unidadMedida;
    private BigDecimal precioUnitario;
    private BigDecimal cantidad;
    private BigDecimal descuento;
    private BigDecimal subtotal;

    public DetalleVentaDTO() {
    }

    public DetalleVentaDTO(Integer idProducto, String nombreProducto, String unidadMedida, BigDecimal precioUnitario,
                           BigDecimal cantidad, BigDecimal descuento, BigDecimal subtotal) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.unidadMedida = unidadMedida;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
        this.descuento = descuento;
        this.subtotal = subtotal;
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

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
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
}
