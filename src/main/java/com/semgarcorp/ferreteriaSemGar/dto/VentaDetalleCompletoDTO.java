package com.semgarcorp.ferreteriaSemGar.dto;

import com.semgarcorp.ferreteriaSemGar.modelo.EstadoVenta;
import com.semgarcorp.ferreteriaSemGar.modelo.Moneda;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VentaDetalleCompletoDTO {
    private Integer idVenta;
    private String serieComprobante;
    private String numeroComprobante;
    private String tipoComprobante;
    private String tipoPago;
    private BigDecimal totalVenta;
    private LocalDateTime fechaVenta;
    private EstadoVenta estadoVenta;
    private Moneda moneda;
    private BigDecimal valorIgvActual; // Porcentaje del IGV en el momento de la venta
    private String nombresCliente;
    private String apellidosCliente;
    private String razonSocialCliente;
    private String razonSocialEmpresa;
    private String rucEmpresa;
    private String numeroDocumentoCliente;
    private String tipoDocumentoCliente;
    private String direccionCliente;
    private String vendedorNombreCompleto;
    private String cajeroNombreUsuario;
    private List<DetalleVentaDTO> detalles;

    // Constructor vacío (necesario para JPA/Jackson)
    public VentaDetalleCompletoDTO() {
    }

    // Constructor para la consulta JPQL (sin detalles)
    public VentaDetalleCompletoDTO(Integer idVenta, String serieComprobante, String numeroComprobante,
                                   String tipoComprobante, String tipoPago, BigDecimal totalVenta,
                                   LocalDateTime fechaVenta, EstadoVenta estadoVenta, Moneda moneda, BigDecimal valorIgvActual,
                                   String nombresCliente, String apellidosCliente, String razonSocialCliente,
                                   String razonSocialEmpresa, String rucEmpresa, String numeroDocumentoCliente, String tipoDocumentoCliente, String direccionCliente,
                                   String vendedorNombreCompleto, String cajeroNombreUsuario) {
        this.idVenta = idVenta;
        this.serieComprobante = serieComprobante;
        this.numeroComprobante = numeroComprobante;
        this.tipoComprobante = tipoComprobante;
        this.tipoPago = tipoPago;
        this.totalVenta = totalVenta;
        this.fechaVenta = fechaVenta;
        this.estadoVenta = estadoVenta;
        this.moneda = moneda;
        this.valorIgvActual = valorIgvActual;
        this.nombresCliente = nombresCliente;
        this.apellidosCliente = apellidosCliente;
        this.razonSocialCliente = razonSocialCliente;
        this.razonSocialEmpresa = razonSocialEmpresa;
        this.rucEmpresa = rucEmpresa;
        this.numeroDocumentoCliente = numeroDocumentoCliente;
        this.tipoDocumentoCliente = tipoDocumentoCliente;
        this.direccionCliente = direccionCliente;
        this.vendedorNombreCompleto = vendedorNombreCompleto;
        this.cajeroNombreUsuario = cajeroNombreUsuario;
        this.detalles = new ArrayList<>();
    }

    // Constructor completo (opcional, para otros usos)
    public VentaDetalleCompletoDTO(Integer idVenta, String serieComprobante, String numeroComprobante,
                                   String tipoComprobante, String tipoPago, BigDecimal totalVenta,
                                   LocalDateTime fechaVenta, EstadoVenta estadoVenta, Moneda moneda,
                                   String nombresCliente, String apellidosCliente, String razonSocialCliente,
                                   String razonSocialEmpresa, String rucEmpresa, String numeroDocumentoCliente, String tipoDocumentoCliente, String direccionCliente,
                                   List<DetalleVentaDTO> detalles) {
        this(idVenta, serieComprobante, numeroComprobante, tipoComprobante, tipoPago, totalVenta,
                fechaVenta, estadoVenta, moneda, BigDecimal.ZERO, nombresCliente, apellidosCliente,
                razonSocialCliente, razonSocialEmpresa, rucEmpresa, numeroDocumentoCliente, tipoDocumentoCliente, direccionCliente,
                "", "");
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

    public BigDecimal getTotalVenta() {
        return totalVenta;
    }

    public void setTotalVenta(BigDecimal totalVenta) {
        this.totalVenta = totalVenta;
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

    public String getRucEmpresa() {
        return rucEmpresa;
    }

    public void setRucEmpresa(String rucEmpresa) {
        this.rucEmpresa = rucEmpresa;
    }

    public String getNumeroDocumentoCliente() {
        return numeroDocumentoCliente;
    }

    public void setNumeroDocumentoCliente(String numeroDocumentoCliente) {
        this.numeroDocumentoCliente = numeroDocumentoCliente;
    }

    public String getTipoDocumentoCliente() {
        return tipoDocumentoCliente;
    }

    public void setTipoDocumentoCliente(String tipoDocumentoCliente) {
        this.tipoDocumentoCliente = tipoDocumentoCliente;
    }

    public String getDireccionCliente() {
        return direccionCliente;
    }

    public void setDireccionCliente(String direccionCliente) {
        this.direccionCliente = direccionCliente;
    }

    public List<DetalleVentaDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVentaDTO> detalles) {
        this.detalles = detalles;
    }

    public String getVendedorNombreCompleto() {
        return vendedorNombreCompleto;
    }

    public void setVendedorNombreCompleto(String vendedorNombreCompleto) {
        this.vendedorNombreCompleto = vendedorNombreCompleto;
    }

    public String getCajeroNombreUsuario() {
        return cajeroNombreUsuario;
    }

    public void setCajeroNombreUsuario(String cajeroNombreUsuario) {
        this.cajeroNombreUsuario = cajeroNombreUsuario;
    }

    // Método para agregar un detalle individual
    public void agregarDetalle(DetalleVentaDTO detalle) {
        if (detalle != null) {
            if (this.detalles == null) {
                this.detalles = new ArrayList<>();
            }
            this.detalles.add(detalle);
        }
    }

    // equals, hashCode y toString (opcionales pero recomendados)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VentaDetalleCompletoDTO that = (VentaDetalleCompletoDTO) o;
        return Objects.equals(idVenta, that.idVenta) &&
                Objects.equals(serieComprobante, that.serieComprobante) &&
                Objects.equals(numeroComprobante, that.numeroComprobante);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idVenta, serieComprobante, numeroComprobante);
    }

    @Override
    public String toString() {
        return "VentaDetalleCompletoDTO{" +
                "idVenta=" + idVenta +
                ", serieComprobante='" + serieComprobante + '\'' +
                ", numeroComprobante='" + numeroComprobante + '\'' +
                ", totalVenta=" + totalVenta +
                ", fechaVenta=" + fechaVenta +
                '}';
    }
}