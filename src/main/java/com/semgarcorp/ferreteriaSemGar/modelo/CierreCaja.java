package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class CierreCaja { //entidad que resume el cierre de una caja (tomamos información de Caja y movimientoCaja)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCierreCaja;

    @NotNull(message = "La fecha de apertura no puede ser nula")
    @Column
    private LocalDateTime fechaApertura; //fecha en que se abrió la caja

    @NotNull(message = "La fecha de cierre no puede ser nula")
    @Column
    private LocalDateTime fechaCierre; //fecha en que se cerró la caja

    @NotNull(message = "El saldo inicial no puede ser nulo")
    @Column(precision = 10, scale = 2)
    private BigDecimal saldoInicial; //indicativo del saldo inicial que se ingresó

    @Column(precision = 10, scale = 2)
    private BigDecimal totalEntradas; //resumen del total de entradas

    @Column(precision = 10, scale = 2)
    private BigDecimal totalSalidas; //resumen del total de salidas

    @Column(precision = 10, scale = 2)
    private BigDecimal saldoFinal; //saldo final que quedó al cerrar la caja

    @Column(columnDefinition = "text")
    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "idUsuario")
    private Usuario usuario; //quien hizo el cierre de caja

    @ManyToOne
    @JoinColumn(name = "idCaja")
    private Caja caja; //qué caja fue la que se cerró

    @PrePersist
    @PreUpdate
    private void validarFechas() {
        if (fechaCierre != null && fechaCierre.isBefore(fechaApertura)) {
            throw new IllegalStateException("Fecha de cierre debe ser posterior a apertura");
        }
    }

    // Constructor vacío
    public CierreCaja() {
    }

    public CierreCaja(Integer idCierreCaja, LocalDateTime fechaApertura, LocalDateTime fechaCierre,
                      BigDecimal saldoInicial, BigDecimal totalEntradas, BigDecimal totalSalidas, BigDecimal saldoFinal,
                      String observaciones, Usuario usuario, Caja caja) {
        this.idCierreCaja = idCierreCaja;
        this.fechaApertura = fechaApertura;
        this.fechaCierre = fechaCierre;
        this.saldoInicial = saldoInicial;
        this.totalEntradas = totalEntradas;
        this.totalSalidas = totalSalidas;
        this.saldoFinal = saldoFinal;
        this.observaciones = observaciones;
        this.usuario = usuario;
        this.caja = caja;
    }

    public Integer getIdCierreCaja() {
        return idCierreCaja;
    }

    public void setIdCierreCaja(Integer idCierreCaja) {
        this.idCierreCaja = idCierreCaja;
    }

    public LocalDateTime getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(LocalDateTime fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public LocalDateTime getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(LocalDateTime fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public BigDecimal getTotalEntradas() {
        return totalEntradas;
    }

    public void setTotalEntradas(BigDecimal totalEntradas) {
        this.totalEntradas = totalEntradas;
    }

    public BigDecimal getTotalSalidas() {
        return totalSalidas;
    }

    public void setTotalSalidas(BigDecimal totalSalidas) {
        this.totalSalidas = totalSalidas;
    }

    public BigDecimal getSaldoFinal() {
        return saldoFinal;
    }

    public void setSaldoFinal(BigDecimal saldoFinal) {
        this.saldoFinal = saldoFinal;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Caja getCaja() {
        return caja;
    }

    public void setCaja(Caja caja) {
        this.caja = caja;
    }
}
