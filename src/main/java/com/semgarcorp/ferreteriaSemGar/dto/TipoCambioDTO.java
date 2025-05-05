package com.semgarcorp.ferreteriaSemGar.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDate;

public class TipoCambioDTO {
    @JsonProperty("fecha")
    private LocalDate fecha;
    @JsonProperty("precioCompra")
    private BigDecimal compra;
    @JsonProperty("precioVenta")
    private BigDecimal venta;

    public TipoCambioDTO() {
    }

    public TipoCambioDTO(LocalDate fecha, BigDecimal compra, BigDecimal venta) {
        this.fecha = fecha;
        this.compra = compra;
        this.venta = venta;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getCompra() {
        return compra;
    }

    public void setCompra(BigDecimal compra) {
        this.compra = compra;
    }

    public BigDecimal getVenta() {
        return venta;
    }

    public void setVenta(BigDecimal venta) {
        this.venta = venta;
    }
}