package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class CierreCaja {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idCierreCaja;

    @NotNull(message = "La fecha de cierre no puede ser nula")
    @Column
    private LocalDate fechaCierre;

    @NotNull(message = "El saldo inicial no puede ser nulo")
    @Column(precision = 10, scale = 2)
    private BigDecimal saldoInicial;

    @NotNull(message = "El total de entradas no puede ser nulo")
    @Column(precision = 10, scale = 2)
    private BigDecimal totalEntradas;

    @NotNull(message = "El total de salidas no puede ser nulo")
    @Column(precision = 10, scale = 2)
    private BigDecimal totalSalidas;

    @NotNull(message = "El saldo final no puede ser nulo")
    @Column(precision = 10, scale = 2)
    private BigDecimal saldoFinal;

    @Column(columnDefinition = "text")
    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "idCaja")
    private Caja caja;

    // Constructor vacío
    public CierreCaja() {
    }

    // Constructor con parámetros
    public CierreCaja(Integer idCierreCaja, LocalDate fechaCierre, BigDecimal saldoInicial, BigDecimal totalEntradas,
                      BigDecimal totalSalidas, BigDecimal saldoFinal, String observaciones, Usuario usuario, Caja caja) {
        this.idCierreCaja = idCierreCaja;
        this.fechaCierre = fechaCierre;
        this.saldoInicial = saldoInicial;
        this.totalEntradas = totalEntradas;
        this.totalSalidas = totalSalidas;
        this.saldoFinal = saldoFinal;
        this.observaciones = observaciones;
        this.usuario = usuario;
        this.caja = caja;
    }

    // Getters y setters
    public Integer getIdCierreCaja() {
        return idCierreCaja;
    }

    public void setIdCierreCaja(Integer idCierreCaja) {
        this.idCierreCaja = idCierreCaja;
    }

    public LocalDate getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(LocalDate fechaCierre) {
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
