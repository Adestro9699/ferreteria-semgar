package com.semgarcorp.ferreteriaSemGar.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class VentaDTO {
    private String serieComprobante;
    private String numeroComprobante;
    private String tipoComprobante;
    private String tipoPago;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaVenta;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal totalVenta;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal impuestoTotal;

    private String estadoVenta;

    // Información del cliente
    private String nombresCliente;
    private String apellidosCliente;
    private String direccionCliente;
    private String tipoDocumentoCliente;
    private String numeroDocumentoCliente;

    // Otros atributos relevantes
    private List<DetalleVentaDTO> detalles;

    public VentaDTO() {
    }

    public VentaDTO(String serieComprobante, String numeroComprobante, String tipoComprobante, String tipoPago,
                    LocalDateTime fechaVenta, BigDecimal totalVenta, BigDecimal impuestoTotal, String estadoVenta,
                    String nombresCliente, String apellidosCliente, String direccionCliente, String tipoDocumentoCliente,
                    String numeroDocumentoCliente, List<DetalleVentaDTO> detalles) {
        this.serieComprobante = serieComprobante;
        this.numeroComprobante = numeroComprobante;
        this.tipoComprobante = tipoComprobante;
        this.tipoPago = tipoPago;
        this.fechaVenta = fechaVenta;
        this.totalVenta = totalVenta;
        this.impuestoTotal = impuestoTotal;
        this.estadoVenta = estadoVenta;
        this.nombresCliente = nombresCliente;
        this.apellidosCliente = apellidosCliente;
        this.direccionCliente = direccionCliente;
        this.tipoDocumentoCliente = tipoDocumentoCliente;
        this.numeroDocumentoCliente = numeroDocumentoCliente;
        this.detalles = detalles;
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

    public String getTipoComprobante() {
        return tipoComprobante;
    }

    public void setTipoComprobante(String tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    public LocalDateTime getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDateTime fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public BigDecimal getTotalVenta() {
        return totalVenta;
    }

    public void setTotalVenta(BigDecimal totalVenta) {
        this.totalVenta = totalVenta;
    }

    public BigDecimal getImpuestoTotal() {
        return impuestoTotal;
    }

    public void setImpuestoTotal(BigDecimal impuestoTotal) {
        this.impuestoTotal = impuestoTotal;
    }

    public String getEstadoVenta() {
        return estadoVenta;
    }

    public void setEstadoVenta(String estadoVenta) {
        this.estadoVenta = estadoVenta;
    }

    public String getNombresCliente() {
        return nombresCliente;
    }

    public void setNombresCliente(String nombresCliente) {
        this.nombresCliente = nombresCliente;
    }

    public String getApellidosCliente() {
        return apellidosCliente;
    }

    public void setApellidosCliente(String apellidosCliente) {
        this.apellidosCliente = apellidosCliente;
    }

    public String getDireccionCliente() {
        return direccionCliente;
    }

    public void setDireccionCliente(String direccionCliente) {
        this.direccionCliente = direccionCliente;
    }

    public String getTipoDocumentoCliente() {
        return tipoDocumentoCliente;
    }

    public void setTipoDocumentoCliente(String tipoDocumentoCliente) {
        this.tipoDocumentoCliente = tipoDocumentoCliente;
    }

    public String getNumeroDocumentoCliente() {
        return numeroDocumentoCliente;
    }

    public void setNumeroDocumentoCliente(String numeroDocumentoCliente) {
        this.numeroDocumentoCliente = numeroDocumentoCliente;
    }

    public List<DetalleVentaDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVentaDTO> detalles) {
        this.detalles = detalles;
    }
}

