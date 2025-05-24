package com.semgarcorp.ferreteriaSemGar.dto;

import com.semgarcorp.ferreteriaSemGar.modelo.EstadoCotizacion;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CotizacionDTO {
    private Integer idCotizacion;
    private String codigoCotizacion;
    private LocalDateTime fechaCotizacion;
    private BigDecimal totalCotizacion;
    private EstadoCotizacion estadoCotizacion;
    private String observaciones;
    private LocalDateTime fechaModificacion;
    private Integer idTrabajador;
    private Integer idCliente;
    private Integer idTipoPago;
    private Integer idEmpresa;
    private List<DetalleCotizacionDTO> detalles;

    public CotizacionDTO() {
    }

    public CotizacionDTO(Integer idCotizacion, String codigoCotizacion, LocalDateTime fechaCotizacion,
                         BigDecimal totalCotizacion, EstadoCotizacion estadoCotizacion, String observaciones,
                         LocalDateTime fechaModificacion, Integer idTrabajador, Integer idCliente, Integer idTipoPago,
                         Integer idEmpresa, List<DetalleCotizacionDTO> detalles) {
        this.idCotizacion = idCotizacion;
        this.codigoCotizacion = codigoCotizacion;
        this.fechaCotizacion = fechaCotizacion;
        this.totalCotizacion = totalCotizacion;
        this.estadoCotizacion = estadoCotizacion;
        this.observaciones = observaciones;
        this.fechaModificacion = fechaModificacion;
        this.idTrabajador = idTrabajador;
        this.idCliente = idCliente;
        this.idTipoPago = idTipoPago;
        this.idEmpresa = idEmpresa;
        this.detalles = detalles;
    }

    public Integer getIdCotizacion() {
        return idCotizacion;
    }

    public void setIdCotizacion(Integer idCotizacion) {
        this.idCotizacion = idCotizacion;
    }

    public String getCodigoCotizacion() {
        return codigoCotizacion;
    }

    public void setCodigoCotizacion(String codigoCotizacion) {
        this.codigoCotizacion = codigoCotizacion;
    }

    public LocalDateTime getFechaCotizacion() {
        return fechaCotizacion;
    }

    public void setFechaCotizacion(LocalDateTime fechaCotizacion) {
        this.fechaCotizacion = fechaCotizacion;
    }

    public BigDecimal getTotalCotizacion() {
        return totalCotizacion;
    }

    public void setTotalCotizacion(BigDecimal totalCotizacion) {
        this.totalCotizacion = totalCotizacion;
    }

    public EstadoCotizacion getEstadoCotizacion() {
        return estadoCotizacion;
    }

    public void setEstadoCotizacion(EstadoCotizacion estadoCotizacion) {
        this.estadoCotizacion = estadoCotizacion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
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

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public List<DetalleCotizacionDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleCotizacionDTO> detalles) {
        this.detalles = detalles;
    }
}
