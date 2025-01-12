package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Entity
public class Venta {

    // Enum para el estado de la venta
    public enum EstadoVenta {
        PENDIENTE,
        COMPLETADA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idVenta;

    @NotNull(message = "La fecha de venta no puede ser nula")
    private LocalDate fechaVenta;

    @Enumerated(EnumType.STRING)
    private EstadoVenta estadoVenta;

    @NotNull(message = "El total de la venta no puede ser nulo")
    @Column(precision = 10, scale = 2)
    private BigDecimal totalVenta;

    @NotNull(message = "La cantidad de la venta no puede ser nula")
    @Column(precision = 10, scale = 2)  // Definido para permitir decimales en la cantidad
    private BigDecimal cantidadVenta;

    @NotNull(message = "El descuento no puede ser nulo")
    @Column(precision = 10, scale = 2)
    private BigDecimal descuento;

    @Column(name = "fecha_modificacion")
    private LocalDate fechaModificacion;

    @NotNull(message = "El código de transacción no puede ser nulo")
    @Column(length = 50)
    private String codigoTransaccion;

    @Lob
    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "idCotizacionProductoInventario")
    private CotizacionProductoInventario cotizacionProductoInventario;

    // Relación con Caja (Many-to-One)
    @ManyToOne
    @JoinColumn(name = "idCaja", nullable = false)
    private Caja caja;

    // Relación con la tabla puente VentaImpuestoCotizacion
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VentaImpuestoCotizacion> ventaImpuestoCotizaciones;

    // Relación con TipoComprobantePago (Many-to-One)
    @ManyToOne
    @JoinColumn(name = "idTipoComprobantePago", nullable = false)  // Referencia a TipoComprobantePago
    private TipoComprobantePago tipoComprobantePago;

    public Venta() {
    }

    public Venta(Integer idVenta, LocalDate fechaVenta, EstadoVenta estadoVenta, BigDecimal totalVenta,
                 BigDecimal cantidadVenta, BigDecimal descuento, LocalDate fechaModificacion, String codigoTransaccion,
                 String observaciones, CotizacionProductoInventario cotizacionProductoInventario, Caja caja,
                 Set<VentaImpuestoCotizacion> ventaImpuestoCotizaciones, TipoComprobantePago tipoComprobantePago) {
        this.idVenta = idVenta;
        this.fechaVenta = fechaVenta;
        this.estadoVenta = estadoVenta;
        this.totalVenta = totalVenta;
        this.cantidadVenta = cantidadVenta;
        this.descuento = descuento;
        this.fechaModificacion = fechaModificacion;
        this.codigoTransaccion = codigoTransaccion;
        this.observaciones = observaciones;
        this.cotizacionProductoInventario = cotizacionProductoInventario;
        this.caja = caja;
        this.ventaImpuestoCotizaciones = ventaImpuestoCotizaciones;
        this.tipoComprobantePago = tipoComprobantePago;  // Inicialización del tipo de comprobante de pago
    }

    // Getters y setters
    public Integer getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Integer idVenta) {
        this.idVenta = idVenta;
    }

    public LocalDate getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDate fechaVenta) {
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

    public BigDecimal getCantidadVenta() {
        return cantidadVenta;
    }

    public void setCantidadVenta(BigDecimal cantidadVenta) {
        this.cantidadVenta = cantidadVenta;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public LocalDate getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDate fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getCodigoTransaccion() {
        return codigoTransaccion;
    }

    public void setCodigoTransaccion(String codigoTransaccion) {
        this.codigoTransaccion = codigoTransaccion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public CotizacionProductoInventario getCotizacionProductoInventario() {
        return cotizacionProductoInventario;
    }

    public void setCotizacionProductoInventario(CotizacionProductoInventario cotizacionProductoInventario) {
        this.cotizacionProductoInventario = cotizacionProductoInventario;
    }

    public Caja getCaja() {
        return caja;
    }

    public void setCaja(Caja caja) {
        this.caja = caja;
    }

    public Set<VentaImpuestoCotizacion> getVentaImpuestoCotizaciones() {
        return ventaImpuestoCotizaciones;
    }

    public void setVentaImpuestoCotizaciones(Set<VentaImpuestoCotizacion> ventaImpuestoCotizaciones) {
        this.ventaImpuestoCotizaciones = ventaImpuestoCotizaciones;
    }

    public TipoComprobantePago getTipoComprobantePago() {
        return tipoComprobantePago;
    }

    public void setTipoComprobantePago(TipoComprobantePago tipoComprobantePago) {
        this.tipoComprobantePago = tipoComprobantePago;
    }
}