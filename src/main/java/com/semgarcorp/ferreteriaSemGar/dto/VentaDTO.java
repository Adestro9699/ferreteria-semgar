package com.semgarcorp.ferreteriaSemGar.dto;

import com.semgarcorp.ferreteriaSemGar.modelo.EstadoVenta; // Importa la enumeración común
import com.semgarcorp.ferreteriaSemGar.modelo.Moneda;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class VentaDTO {

    private Integer idVenta;
    private String serieComprobante; //no puede ser nulo
    private String numeroComprobante; //no puede ser nulo
    private LocalDateTime fechaVenta;
    private EstadoVenta estadoVenta;
    private BigDecimal totalVenta;
    private LocalDateTime fechaModificacion;
    private Moneda moneda;
    private BigDecimal valorIgvActual; // Porcentaje del IGV en el momento de la venta
    private String observaciones;
    private Integer idCaja;
    private Integer idEmpresa;
    private Integer idTipoComprobantePago;
    private Integer idTrabajador;
    private Integer idCliente;
    private Integer idTipoPago;
    private List<DetalleVentaDTO> detalles;

    public VentaDTO() {
    }

    public VentaDTO(Integer idVenta, String serieComprobante, String numeroComprobante, LocalDateTime fechaVenta,
                    EstadoVenta estadoVenta, BigDecimal totalVenta, LocalDateTime fechaModificacion, Moneda moneda,
                    BigDecimal valorIgvActual, String observaciones, Integer idCaja, Integer idEmpresa,
                    Integer idTipoComprobantePago, Integer idTrabajador, Integer idCliente, Integer idTipoPago,
                    List<DetalleVentaDTO> detalles) {
        this.idVenta = idVenta;
        this.serieComprobante = serieComprobante;
        this.numeroComprobante = numeroComprobante;
        this.fechaVenta = fechaVenta;
        this.estadoVenta = estadoVenta;
        this.totalVenta = totalVenta;
        this.fechaModificacion = fechaModificacion;
        this.moneda = moneda;
        this.valorIgvActual = valorIgvActual;
        this.observaciones = observaciones;
        this.idCaja = idCaja;
        this.idEmpresa = idEmpresa;
        this.idTipoComprobantePago = idTipoComprobantePago;
        this.idTrabajador = idTrabajador;
        this.idCliente = idCliente;
        this.idTipoPago = idTipoPago;
        this.detalles = detalles;
    }

    public Integer getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Integer idVenta) {
        this.idVenta = idVenta;
    }

    public String getSerieComprobante() {
        return serieComprobante;
    }

    public void setSerieComprobante(String serieComprobante) {
        this.serieComprobante = serieComprobante;
    }

    public String getNumeroComprobante() {
        return numeroComprobante;
    }

    public void setNumeroComprobante(String numeroComprobante) {
        this.numeroComprobante = numeroComprobante;
    }

    public LocalDateTime getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDateTime fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public EstadoVenta getEstadoVenta() {
        return estadoVenta;
    }

    public void setEstadoVenta(EstadoVenta estadoVenta) {
        this.estadoVenta = estadoVenta;
    }

    public BigDecimal getTotalVenta() {
        return totalVenta;
    }

    public void setTotalVenta(BigDecimal totalVenta) {
        this.totalVenta = totalVenta;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Moneda getMoneda() {
        return moneda;
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }

    public BigDecimal getValorIgvActual() {
        return valorIgvActual;
    }

    public void setValorIgvActual(BigDecimal valorIgvActual) {
        this.valorIgvActual = valorIgvActual;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Integer getIdCaja() {
        return idCaja;
    }

    public void setIdCaja(Integer idCaja) {
        this.idCaja = idCaja;
    }

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public Integer getIdTipoComprobantePago() {
        return idTipoComprobantePago;
    }

    public void setIdTipoComprobantePago(Integer idTipoComprobantePago) {
        this.idTipoComprobantePago = idTipoComprobantePago;
    }

    public Integer getIdTrabajador() {
        return idTrabajador;
    }

    public void setIdTrabajador(Integer idTrabajador) {
        this.idTrabajador = idTrabajador;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public Integer getIdTipoPago() {
        return idTipoPago;
    }

    public void setIdTipoPago(Integer idTipoPago) {
        this.idTipoPago = idTipoPago;
    }

    public List<DetalleVentaDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVentaDTO> detalles) {
        this.detalles = detalles;
    }
}
