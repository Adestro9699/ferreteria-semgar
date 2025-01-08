package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class VentaImpuestoCotizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer idVentaImpuestoCotizacion;

    @Column(precision = 5, scale = 2)
    private BigDecimal porcentajeAplicado;

    @Column(precision = 10, scale = 2)
    private BigDecimal montoAplicado;

    @ManyToOne
    @JoinColumn(name = "idVenta", nullable = false)
    private Venta venta;

    @ManyToOne
    @JoinColumn(name = "idImpuesto", nullable = false)
    private Impuesto impuesto;

    @ManyToOne
    @JoinColumn(name = "idCotizacion", nullable = true) // Puede ser NULL si no se aplica cotización
    private Cotizacion cotizacion;

    public VentaImpuestoCotizacion() {
    }

    public VentaImpuestoCotizacion(Integer idVentaImpuestoCotizacion, BigDecimal porcentajeAplicado, BigDecimal montoAplicado,
                                   Venta venta, Impuesto impuesto, Cotizacion cotizacion) {
        this.idVentaImpuestoCotizacion = idVentaImpuestoCotizacion;
        this.porcentajeAplicado = porcentajeAplicado;
        this.montoAplicado = montoAplicado;
        this.venta = venta;
        this.impuesto = impuesto;
        this.cotizacion = cotizacion;
    }

    public Integer getIdVentaImpuestoCotizacion() {
        return idVentaImpuestoCotizacion;
    }

    public void setIdVentaImpuestoCotizacion(Integer idVentaImpuestoCotizacion) {
        this.idVentaImpuestoCotizacion = idVentaImpuestoCotizacion;
    }

    public BigDecimal getPorcentajeAplicado() {
        return porcentajeAplicado;
    }

    public void setPorcentajeAplicado(BigDecimal porcentajeAplicado) {
        this.porcentajeAplicado = porcentajeAplicado;
    }

    public BigDecimal getMontoAplicado() {
        return montoAplicado;
    }

    public void setMontoAplicado(BigDecimal montoAplicado) {
        this.montoAplicado = montoAplicado;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public Impuesto getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(Impuesto impuesto) {
        this.impuesto = impuesto;
    }

    public Cotizacion getCotizacion() {
        return cotizacion;
    }

    public void setCotizacion(Cotizacion cotizacion) {
        this.cotizacion = cotizacion;
    }
}
