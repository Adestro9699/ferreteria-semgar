package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class TipoPago {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idTipoPago;

    private String nombreTipoPago;

    private String descripcionTipoPago;

    private Boolean estadoTipoPago;

    private Double comision;

    private LocalDate fechaCreacion;

    private LocalDate fechaModificacion;

    @Column(unique = true)
    private String codigoTipoPago;

    public TipoPago() {
    }

    public TipoPago(Long idTipoPago, String nombreTipoPago, String descripcionTipoPago, Boolean estadoTipoPago,
                    Double comision, LocalDate fechaCreacion, LocalDate fechaModificacion, String codigoTipoPago) {
        this.idTipoPago = idTipoPago;
        this.nombreTipoPago = nombreTipoPago;
        this.descripcionTipoPago = descripcionTipoPago;
        this.estadoTipoPago = estadoTipoPago;
        this.comision = comision;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
        this.codigoTipoPago = codigoTipoPago;
    }

    public Long getIdTipoPago() {
        return idTipoPago;
    }

    public void setIdTipoPago(Long idTipoPago) {
        this.idTipoPago = idTipoPago;
    }

    public String getNombreTipoPago() {
        return nombreTipoPago;
    }

    public void setNombreTipoPago(String nombreTipoPago) {
        this.nombreTipoPago = nombreTipoPago;
    }

    public String getDescripcionTipoPago() {
        return descripcionTipoPago;
    }

    public void setDescripcionTipoPago(String descripcionTipoPago) {
        this.descripcionTipoPago = descripcionTipoPago;
    }

    public Boolean getEstadoTipoPago() {
        return estadoTipoPago;
    }

    public void setEstadoTipoPago(Boolean estadoTipoPago) {
        this.estadoTipoPago = estadoTipoPago;
    }

    public Double getComision() {
        return comision;
    }

    public void setComision(Double comision) {
        this.comision = comision;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDate getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDate fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getCodigoTipoPago() {
        return codigoTipoPago;
    }

    public void setCodigoTipoPago(String codigoTipoPago) {
        this.codigoTipoPago = codigoTipoPago;
    }
}
