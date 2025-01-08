package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class TipoPago {

    public enum EstadoTipoPago {
        ACTIVO,
        INACTIVO
    }

    public enum NombreTipoPago {
        EFECTIVO,
        DEBITO,
        CREDITO,
        YAPE,
        PLIN,
        TRANSFERENCIA,
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idTipoPago;

    @Enumerated(EnumType.STRING)
    private NombreTipoPago nombreTipoPago;

    @Column(length = 300)
    private String descripcionTipoPago;

    @Enumerated(EnumType.STRING)
    private EstadoTipoPago estadoTipoPago;

    @Column(precision = 10, scale = 2)
    private BigDecimal comision;

    private LocalDate fechaCreacion;

    private LocalDate fechaModificacion;

    @Column(unique = true, length = 20)
    private String codigoTipoPago;

    public TipoPago() {
    }

    public TipoPago(Integer idTipoPago, NombreTipoPago nombreTipoPago, String descripcionTipoPago,
                    EstadoTipoPago estadoTipoPago, BigDecimal comision, LocalDate fechaCreacion,
                    LocalDate fechaModificacion, String codigoTipoPago) {
        this.idTipoPago = idTipoPago;
        this.nombreTipoPago = nombreTipoPago;
        this.descripcionTipoPago = descripcionTipoPago;
        this.estadoTipoPago = estadoTipoPago;
        this.comision = comision;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
        this.codigoTipoPago = codigoTipoPago;
    }

    public Integer getIdTipoPago() {
        return idTipoPago;
    }

    public void setIdTipoPago(Integer idTipoPago) {
        this.idTipoPago = idTipoPago;
    }

    public NombreTipoPago getNombreTipoPago() {
        return nombreTipoPago;
    }

    public void setNombreTipoPago(NombreTipoPago nombreTipoPago) {
        this.nombreTipoPago = nombreTipoPago;
    }

    public String getDescripcionTipoPago() {
        return descripcionTipoPago;
    }

    public void setDescripcionTipoPago(String descripcionTipoPago) {
        this.descripcionTipoPago = descripcionTipoPago;
    }

    public EstadoTipoPago getEstadoTipoPago() {
        return estadoTipoPago;
    }

    public void setEstadoTipoPago(EstadoTipoPago estadoTipoPago) {
        this.estadoTipoPago = estadoTipoPago;
    }

    public BigDecimal getComision() {
        return comision;
    }

    public void setComision(BigDecimal comision) {
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
