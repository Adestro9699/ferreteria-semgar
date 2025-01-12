package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
public class Impuesto {

    // Enum para representar los estados de un impuesto
    public enum EstadoActivo {
        ACTIVO,
        INACTIVO;
    }

    @Id
    @Column
    private Integer idImpuesto;

    @Column(length = 255)
    private String nombreImpuesto;

    @Column(length = 100)
    private String tipoImpuesto;

    @Column(precision = 10, scale = 2)
    private BigDecimal porcentaje; // Si estamos tratando con IGV guardamos 18 no 18.00 ni 0.18

    // Se reemplaza el booleano 'activo' por el enum 'estado'
    @Enumerated(EnumType.STRING)  // Esto guarda el valor como un string ("ACTIVO", "INACTIVO") en la base de datos
    @Column
    private EstadoActivo estado;

    // Relación con la tabla puente (Venta_Impuesto_Cotizacion)
    @OneToMany(mappedBy = "impuesto", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VentaImpuestoCotizacion> ventaImpuestoCotizaciones;

    // Constructor vacío
    public Impuesto() {
    }

    // Constructor con parámetros
    public Impuesto(Integer idImpuesto, String nombreImpuesto, String tipoImpuesto, BigDecimal porcentaje, EstadoActivo estado,
                    Set<VentaImpuestoCotizacion> ventaImpuestoCotizaciones) {
        this.idImpuesto = idImpuesto;
        this.nombreImpuesto = nombreImpuesto;
        this.tipoImpuesto = tipoImpuesto;
        this.porcentaje = porcentaje;
        this.estado = estado;  // Establecer estado usando el enum
        this.ventaImpuestoCotizaciones = ventaImpuestoCotizaciones;
    }

    // Métodos getter y setter para el idImpuesto
    public Integer getIdImpuesto() {
        return idImpuesto;
    }

    public void setIdImpuesto(Integer idImpuesto) {
        this.idImpuesto = idImpuesto;
    }

    // Métodos getter y setter para nombreImpuesto
    public String getNombreImpuesto() {
        return nombreImpuesto;
    }

    public void setNombreImpuesto(String nombreImpuesto) {
        this.nombreImpuesto = nombreImpuesto;
    }

    // Métodos getter y setter para tipoImpuesto
    public String getTipoImpuesto() {
        return tipoImpuesto;
    }

    public void setTipoImpuesto(String tipoImpuesto) {
        this.tipoImpuesto = tipoImpuesto;
    }

    // Métodos getter y setter para porcentaje
    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }

    // Métodos getter y setter para estado (cambio importante de boolean a EstadoActivo)
    public EstadoActivo getEstado() {
        return estado;
    }

    public void setEstado(EstadoActivo estado) {
        this.estado = estado;
    }

    // Métodos getter y setter para las cotizaciones
    public Set<VentaImpuestoCotizacion> getVentaImpuestoCotizaciones() {
        return ventaImpuestoCotizaciones;
    }

    public void setVentaImpuestoCotizaciones(Set<VentaImpuestoCotizacion> ventaImpuestoCotizaciones) {
        this.ventaImpuestoCotizaciones = ventaImpuestoCotizaciones;
    }
}
