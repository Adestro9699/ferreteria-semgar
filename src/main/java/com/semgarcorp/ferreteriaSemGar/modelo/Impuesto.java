package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Set;

@Entity
public class Impuesto {

    @Id
    @Column
    private Integer idImpuesto;

    @Column(length = 255)
    private String nombreImpuesto;

    @Column(length = 100)
    private String tipoImpuesto;

    @Column(precision = 10, scale = 2)
    private BigDecimal porcentaje;

    @Column
    private boolean activo;

    // Relación con la tabla puente (Venta_Impuesto_Cotizacion)
    @OneToMany(mappedBy = "impuesto", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VentaImpuestoCotizacion> ventaImpuestoCotizaciones;

    public Impuesto() {
    }

    public Impuesto(Integer idImpuesto, String nombreImpuesto, String tipoImpuesto, BigDecimal porcentaje, boolean activo,
                    Set<VentaImpuestoCotizacion> ventaImpuestoCotizaciones) {
        this.idImpuesto = idImpuesto;
        this.nombreImpuesto = nombreImpuesto;
        this.tipoImpuesto = tipoImpuesto;
        this.porcentaje = porcentaje;
        this.activo = activo;
        this.ventaImpuestoCotizaciones = ventaImpuestoCotizaciones;
    }

    public Integer getIdImpuesto() {
        return idImpuesto;
    }

    public void setIdImpuesto(Integer idImpuesto) {
        this.idImpuesto = idImpuesto;
    }

    public String getNombreImpuesto() {
        return nombreImpuesto;
    }

    public void setNombreImpuesto(String nombreImpuesto) {
        this.nombreImpuesto = nombreImpuesto;
    }

    public String getTipoImpuesto() {
        return tipoImpuesto;
    }

    public void setTipoImpuesto(String tipoImpuesto) {
        this.tipoImpuesto = tipoImpuesto;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Set<VentaImpuestoCotizacion> getVentaImpuestoCotizaciones() {
        return ventaImpuestoCotizaciones;
    }

    public void setVentaImpuestoCotizaciones(Set<VentaImpuestoCotizacion> ventaImpuestoCotizaciones) {
        this.ventaImpuestoCotizaciones = ventaImpuestoCotizaciones;
    }
}
