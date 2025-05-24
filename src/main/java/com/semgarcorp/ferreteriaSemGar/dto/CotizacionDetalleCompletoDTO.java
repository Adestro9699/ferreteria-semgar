package com.semgarcorp.ferreteriaSemGar.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CotizacionDetalleCompletoDTO {
    private Integer idCotizacion;
    private String tipoPago;
    private BigDecimal totalCotizacion;
    private LocalDateTime fechaCotizacion;
    private String nombresCliente;
    private String apellidosCliente;
    private String razonSocialCliente;
    private String razonSocialEmpresa;
    private String nombreTrabajador;
    private String apellidoTrabajador;
    private List<DetalleCotizacionDTO> detalles;

    public CotizacionDetalleCompletoDTO() {
    }

    public CotizacionDetalleCompletoDTO(Integer idCotizacion, String tipoPago, BigDecimal totalCotizacion,
                                        LocalDateTime fechaCotizacion, String nombresCliente, String apellidosCliente,
                                        String razonSocialCliente, String razonSocialEmpresa, String nombreTrabajador,
                                        String apellidoTrabajador, List<DetalleCotizacionDTO> detalles) {
        this.idCotizacion = idCotizacion;
        this.tipoPago = tipoPago;
        this.totalCotizacion = totalCotizacion;
        this.fechaCotizacion = fechaCotizacion;
        this.nombresCliente = nombresCliente;
        this.apellidosCliente = apellidosCliente;
        this.razonSocialCliente = razonSocialCliente;
        this.razonSocialEmpresa = razonSocialEmpresa;
        this.nombreTrabajador = nombreTrabajador;
        this.apellidoTrabajador = apellidoTrabajador;
        this.detalles = detalles;
    }

    public Integer getIdCotizacion() {
        return idCotizacion;
    }

    public void setIdCotizacion(Integer idCotizacion) {
        this.idCotizacion = idCotizacion;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    public BigDecimal getTotalCotizacion() {
        return totalCotizacion;
    }

    public void setTotalCotizacion(BigDecimal totalCotizacion) {
        this.totalCotizacion = totalCotizacion;
    }

    public LocalDateTime getFechaCotizacion() {
        return fechaCotizacion;
    }

    public void setFechaCotizacion(LocalDateTime fechaCotizacion) {
        this.fechaCotizacion = fechaCotizacion;
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

    public String getRazonSocialCliente() {
        return razonSocialCliente;
    }

    public void setRazonSocialCliente(String razonSocialCliente) {
        this.razonSocialCliente = razonSocialCliente;
    }

    public String getRazonSocialEmpresa() {
        return razonSocialEmpresa;
    }

    public void setRazonSocialEmpresa(String razonSocialEmpresa) {
        this.razonSocialEmpresa = razonSocialEmpresa;
    }

    public String getNombreTrabajador() {
        return nombreTrabajador;
    }

    public void setNombreTrabajador(String nombreTrabajador) {
        this.nombreTrabajador = nombreTrabajador;
    }

    public String getApellidoTrabajador() {
        return apellidoTrabajador;
    }

    public void setApellidoTrabajador(String apellidoTrabajador) {
        this.apellidoTrabajador = apellidoTrabajador;
    }

    public List<DetalleCotizacionDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleCotizacionDTO> detalles) {
        this.detalles = detalles;
    }
}
