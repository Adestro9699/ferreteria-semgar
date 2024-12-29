package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    @NotNull(message = "La fecha no puede estar vacía")
    private LocalDateTime fecha;

    @NotNull(message = "El saldo inicial no puede estar vacío")
    private BigDecimal saldoInicial;

    @NotNull(message = "Las entradas no pueden estar vacías")
    private BigDecimal entradas;

    @NotNull(message = "Las salidas no pueden estar vacías")
    private BigDecimal salidas;

    @NotNull(message = "El saldo final no puede estar vacío")
    private BigDecimal saldoFinal;

    @Column(length = 255)  // Especifica que la longitud máxima de la columna es 255 caracteres
    private String descripcion;

    @NotNull(message = "El estado no puede estar vacío")
    @Enumerated(EnumType.STRING)
    private EstadoCaja estado;

    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

    public Caja() {
    }

    public Caja(Integer idCaja, LocalDateTime fecha, BigDecimal saldoInicial, BigDecimal entradas, BigDecimal salidas,
                BigDecimal saldoFinal, String descripcion, EstadoCaja estado, Usuario usuario) {
        this.idCaja = idCaja;
        this.fecha = fecha;
        this.saldoInicial = saldoInicial;
        this.entradas = entradas;
        this.salidas = salidas;
        this.saldoFinal = saldoFinal;
        this.descripcion = descripcion;
        this.estado = estado;
        this.usuario = usuario;
    }

    public Integer getIdCaja() {
        return idCaja;
    }

    public void setIdCaja(Integer idCaja) {
        this.idCaja = idCaja;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public BigDecimal getEntradas() {
        return entradas;
    }

    public void setEntradas(BigDecimal entradas) {
        this.entradas = entradas;
    }

    public BigDecimal getSalidas() {
        return salidas;
    }

    public void setSalidas(BigDecimal salidas) {
        this.salidas = salidas;
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
