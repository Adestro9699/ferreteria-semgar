package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class MovimientoCaja {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idMovimiento;

    @NotNull(message = "El monto no puede ser nulo")
    @Column(precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(columnDefinition = "text")
    private String observaciones;

    @NotNull(message = "La fecha no puede ser nula")
    private LocalDateTime fecha;

    @NotNull(message = "El tipo de movimiento no puede ser nulo")
    @Enumerated(EnumType.STRING)
    private TipoMovimiento tipo; // ENTRADA o SALIDA

    @ManyToOne
    @JoinColumn(name = "idCaja", nullable = false)
    private Caja caja;

    @ManyToOne
    @JoinColumn(name = "idCierreCaja", nullable = true)
    private CierreCaja cierreCaja;

    public MovimientoCaja() {
    }

    public MovimientoCaja(Integer idMovimiento, BigDecimal monto, String observaciones, LocalDateTime fecha,
                          TipoMovimiento tipo, Caja caja, CierreCaja cierreCaja) {
        this.idMovimiento = idMovimiento;
        this.monto = monto;
        this.observaciones = observaciones;
        this.fecha = fecha;
        this.tipo = tipo;
        this.caja = caja;
        this.cierreCaja = cierreCaja;
    }

    public Integer getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(Integer idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public TipoMovimiento getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimiento tipo) {
        this.tipo = tipo;
    }

    public Caja getCaja() {
        return caja;
    }

    public void setCaja(Caja caja) {
        this.caja = caja;
    }

    public CierreCaja getCierreCaja() {
        return cierreCaja;
    }

    public void setCierreCaja(CierreCaja cierreCaja) {
        this.cierreCaja = cierreCaja;
    }
}