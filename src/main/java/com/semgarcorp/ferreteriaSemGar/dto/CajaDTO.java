package com.semgarcorp.ferreteriaSemGar.dto;

import com.semgarcorp.ferreteriaSemGar.modelo.EstadoCaja;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CajaDTO {

    private Integer idCaja; // Solo para operaciones que requieran identificar la caja
    private String nombreCaja; // Solo para creación de cajas nuevas (si es necesario)
    private LocalDateTime fechaApertura;
    private LocalDateTime fechaClausura;
    private BigDecimal saldoInicial;
    private BigDecimal entradas;
    private BigDecimal salidas;
    private BigDecimal saldoFinal;
    private EstadoCaja estado; // "ABIERTA" o "CERRADA"
    private Integer idUsuario; // Usuario que realiza la operación (apertura o cierre)
    private String observaciones; // Solo para el cierre de caja

    public CajaDTO() {
    }

    public CajaDTO(Integer idCaja, String nombreCaja, LocalDateTime fechaApertura, LocalDateTime fechaClausura,
                   BigDecimal saldoInicial, BigDecimal entradas, BigDecimal salidas, BigDecimal saldoFinal,
                   EstadoCaja estado, Integer idUsuario, String observaciones) {
        this.idCaja = idCaja;
        this.nombreCaja = nombreCaja;
        this.fechaApertura = fechaApertura;
        this.fechaClausura = fechaClausura;
        this.saldoInicial = saldoInicial;
        this.entradas = entradas;
        this.salidas = salidas;
        this.saldoFinal = saldoFinal;
        this.estado = estado;
        this.idUsuario = idUsuario;
        this.observaciones = observaciones;
    }

    public Integer getIdCaja() {
        return idCaja;
    }

    public void setIdCaja(Integer idCaja) {
        this.idCaja = idCaja;
    }

    public String getNombreCaja() {
        return nombreCaja;
    }

    public void setNombreCaja(String nombreCaja) {
        this.nombreCaja = nombreCaja;
    }

    public LocalDateTime getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(LocalDateTime fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public LocalDateTime getFechaClausura() {
        return fechaClausura;
    }

    public void setFechaClausura(LocalDateTime fechaClausura) {
        this.fechaClausura = fechaClausura;
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

    public EstadoCaja getEstado() {
        return estado;
    }

    public void setEstado(EstadoCaja estado) {
        this.estado = estado;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}