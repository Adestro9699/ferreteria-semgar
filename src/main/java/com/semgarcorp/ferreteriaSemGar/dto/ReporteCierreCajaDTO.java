package com.semgarcorp.ferreteriaSemGar.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReporteCierreCajaDTO {
    private String nombreCaja;
    private String responsable;
    private LocalDateTime fechaApertura;
    private LocalDateTime fechaCierre;
    private BigDecimal saldoInicial;
    private BigDecimal totalEntradas;
    private BigDecimal totalSalidas;
    private BigDecimal saldoFinal;
    
    // Categorías específicas de entradas
    private BigDecimal ventasEfectivo;
    private BigDecimal ingresosManuales;
    
    // Categorías específicas de salidas
    private BigDecimal egresosManuales;
    
    // Listas detalladas de movimientos
    private List<MovimientoReporteDTO> ventasEfectivoList;
    private List<MovimientoReporteDTO> ingresosManualesList;
    private List<MovimientoReporteDTO> egresosManualesList;

    public ReporteCierreCajaDTO() {
        this.ventasEfectivoList = new ArrayList<>();
        this.ingresosManualesList = new ArrayList<>();
        this.egresosManualesList = new ArrayList<>();
    }

    // Clase interna para representar cada movimiento
    public static class MovimientoReporteDTO {
        private String descripcion;
        private BigDecimal monto;

        public MovimientoReporteDTO(String descripcion, BigDecimal monto) {
            this.descripcion = descripcion;
            this.monto = monto;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public BigDecimal getMonto() {
            return monto;
        }

        public void setMonto(BigDecimal monto) {
            this.monto = monto;
        }
    }

    // Getters y Setters
    public String getNombreCaja() {
        return nombreCaja;
    }

    public void setNombreCaja(String nombreCaja) {
        this.nombreCaja = nombreCaja;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
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

    public BigDecimal getVentasEfectivo() {
        return ventasEfectivo;
    }

    public void setVentasEfectivo(BigDecimal ventasEfectivo) {
        this.ventasEfectivo = ventasEfectivo;
    }

    public BigDecimal getIngresosManuales() {
        return ingresosManuales;
    }

    public void setIngresosManuales(BigDecimal ingresosManuales) {
        this.ingresosManuales = ingresosManuales;
    }

    public BigDecimal getEgresosManuales() {
        return egresosManuales;
    }

    public void setEgresosManuales(BigDecimal egresosManuales) {
        this.egresosManuales = egresosManuales;
    }

    public List<MovimientoReporteDTO> getVentasEfectivoList() {
        return ventasEfectivoList;
    }

    public void setVentasEfectivoList(List<MovimientoReporteDTO> ventasEfectivoList) {
        this.ventasEfectivoList = ventasEfectivoList;
    }

    public List<MovimientoReporteDTO> getIngresosManualesList() {
        return ingresosManualesList;
    }

    public void setIngresosManualesList(List<MovimientoReporteDTO> ingresosManualesList) {
        this.ingresosManualesList = ingresosManualesList;
    }

    public List<MovimientoReporteDTO> getEgresosManualesList() {
        return egresosManualesList;
    }

    public void setEgresosManualesList(List<MovimientoReporteDTO> egresosManualesList) {
        this.egresosManualesList = egresosManualesList;
    }
} 