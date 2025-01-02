package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Caja {

    public enum EstadoCaja {
        ABIERTA,
        CERRADA,
        BLOQUEADA,
        EN_PROCESO,
        DESACTIVADA,
        SOBRECARGADA,
        SIN_FONDOS
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idCaja;

    @NotNull(message = "La fecha de apertura no puede estar vacía")
    private LocalDate fechaApertura;

    private LocalDate fechaClausura;

    @NotNull(message = "El saldo inicial no puede estar vacío")
    @Column(precision = 10, scale = 2)
    private BigDecimal saldoInicial;

    @NotNull(message = "Las entradas no pueden estar vacías")
    @Column(precision = 10, scale = 2)
    private BigDecimal entradas;

    @NotNull(message = "Las salidas no pueden estar vacías")
    @Column(precision = 10, scale = 2)
    private BigDecimal salidas;

    @Column(precision = 10, scale = 2)
    private BigDecimal saldoFinal; // Este ya no se calculará automáticamente

    @Column(length = 255)
    private String descripcion;

    @NotNull(message = "El estado no puede estar vacío")
    @Enumerated(EnumType.STRING)
    private EstadoCaja estado;

    @ManyToOne
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;

    public Caja() {
    }

    public Caja(Integer idCaja, LocalDate fechaApertura, LocalDate fechaClausura, BigDecimal saldoInicial,
                BigDecimal entradas, BigDecimal salidas, String descripcion, EstadoCaja estado, Usuario usuario) {
        this.idCaja = idCaja;
        this.fechaApertura = fechaApertura;
        this.fechaClausura = fechaClausura;
        this.saldoInicial = saldoInicial;
        this.entradas = entradas;
        this.salidas = salidas;
        this.descripcion = descripcion;
        this.estado = estado;
        this.usuario = usuario;
        // Se elimina el cálculo automático del saldoFinal
    }

    public Integer getIdCaja() {
        return idCaja;
    }

    public void setIdCaja(Integer idCaja) {
        this.idCaja = idCaja;
    }

    public LocalDate getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(LocalDate fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public LocalDate getFechaClausura() {
        return fechaClausura;
    }

    public void setFechaClausura(LocalDate fechaClausura) {
        this.fechaClausura = fechaClausura;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
        // Ya no recalcula el saldoFinal
    }

    public BigDecimal getEntradas() {
        return entradas;
    }

    public void setEntradas(BigDecimal entradas) {
        this.entradas = entradas;
        // Ya no recalcula el saldoFinal
    }

    public BigDecimal getSalidas() {
        return salidas;
    }

    public void setSalidas(BigDecimal salidas) {
        this.salidas = salidas;
        // Ya no recalcula el saldoFinal
    }

    public BigDecimal getSaldoFinal() {
        return saldoFinal;
    }

    public void setSaldoFinal(BigDecimal saldoFinal) {
        this.saldoFinal = saldoFinal;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public EstadoCaja getEstado() {
        return estado;
    }

    public void setEstado(EstadoCaja estado) {
        this.estado = estado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
