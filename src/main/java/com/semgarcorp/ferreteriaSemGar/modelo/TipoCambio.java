package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class TipoCambio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTipoCambio;

    @Column(nullable = false, unique = true)
    private LocalDate fecha;

    @Column(nullable = false, precision = 10, scale = 3)
    private BigDecimal venta;  // Tipo de cambio para facturas (USD → PEN)

    @Column(precision = 10, scale = 3)
    private BigDecimal compra; // Opcional

    public TipoCambio() {
    }

    public TipoCambio(Integer idTipoCambio, LocalDate fecha, BigDecimal venta, BigDecimal compra) {
        this.idTipoCambio = idTipoCambio;
        this.fecha = fecha;
        this.venta = venta;
        this.compra = compra;
    }

    public Integer getIdTipoCambio() {
        return idTipoCambio;
    }

    public void setIdTipoCambio(Integer idTipoCambio) {
        this.idTipoCambio = idTipoCambio;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getVenta() {
        return venta;
    }

    public void setVenta(BigDecimal venta) {
        this.venta = venta;
    }

    public BigDecimal getCompra() {
        return compra;
    }

    public void setCompra(BigDecimal compra) {
        this.compra = compra;
    }
}
