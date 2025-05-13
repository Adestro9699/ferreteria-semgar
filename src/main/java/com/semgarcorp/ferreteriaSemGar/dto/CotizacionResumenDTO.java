package com.semgarcorp.ferreteriaSemGar.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CotizacionResumenDTO {
    private Integer idCotizacion;
    private String codigoCotizacion;
    private LocalDateTime fechaCotizacion;
    private BigDecimal totalCotizacion;
    private String clienteNombres;
    private String clienteApellidos;
    private String clienteRazonSocial;
    private String empresaRazonSocial;

    public CotizacionResumenDTO() {
    }

    public CotizacionResumenDTO(
            Integer idCotizacion,
            String codigoCotizacion,
            LocalDateTime fechaCotizacion,
            BigDecimal totalCotizacion,
            String clienteNombres,
            String clienteApellidos,
            String clienteRazonSocial,
            String empresaRazonSocial
    ) {
        this.idCotizacion = idCotizacion;
        this.codigoCotizacion = codigoCotizacion;
        this.fechaCotizacion = fechaCotizacion;
        this.totalCotizacion = totalCotizacion;
        this.clienteNombres = clienteNombres;
        this.clienteApellidos = clienteApellidos;
        this.clienteRazonSocial = clienteRazonSocial;
        this.empresaRazonSocial = empresaRazonSocial;
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

    public String getClienteNombres() {
        return clienteNombres;
    }

    public void setClienteNombres(String clienteNombres) {
        this.clienteNombres = clienteNombres;
    }

    public String getClienteApellidos() {
        return clienteApellidos;
    }

    public void setClienteApellidos(String clienteApellidos) {
        this.clienteApellidos = clienteApellidos;
    }

    public String getClienteRazonSocial() {
        return clienteRazonSocial;
    }

    public void setClienteRazonSocial(String clienteRazonSocial) {
        this.clienteRazonSocial = clienteRazonSocial;
    }

    public String getEmpresaRazonSocial() {
        return empresaRazonSocial;
    }

    public void setEmpresaRazonSocial(String empresaRazonSocial) {
        this.empresaRazonSocial = empresaRazonSocial;
    }
}
