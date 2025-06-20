package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Cambiado a IDENTITY
    private Integer idVenta;

    @Column(length = 4, nullable = false)
    private String serieComprobante;  // Ejemplo: "F001" o "B001"

    @Column(length = 6, nullable = false)
    private String numeroComprobante; // Ejemplo: "000123"

    @Column(length = 20, nullable = true)
    private String codigoCotizacion;

    @NotNull(message = "La fecha de venta no puede ser nula")
    private LocalDateTime fechaVenta;

    @Enumerated(EnumType.STRING)
    private EstadoVenta estadoVenta;

    @NotNull(message = "El total de la venta no puede ser nulo")
    @Column(precision = 10, scale = 2)
    private BigDecimal totalVenta;

    @Column(precision = 5, scale = 2)
    private BigDecimal valorIgvActual; // Porcentaje del IGV en el momento de la venta (ej: 18.00, 17.00)

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    @Lob
    private String observaciones;

    @Enumerated(EnumType.STRING)
    private Moneda moneda;

    // Relación con Caja (Many-to-One)
    @ManyToOne
    @JoinColumn(name = "idCaja", referencedColumnName = "idCaja", nullable = true)
    private Caja caja;

    // Relación con Empresa (Many-to-One)
    @ManyToOne
    @JoinColumn(name = "idEmpresa", nullable = false)
    private Empresa empresa;

    // Relación con TipoComprobantePago (Many-to-One)
    @ManyToOne
    @JoinColumn(name = "idTipoComprobantePago", nullable = false)
    private TipoComprobantePago tipoComprobantePago;

    // Relación con Trabajador
    @ManyToOne
    @JoinColumn(name = "idTrabajador", referencedColumnName = "idTrabajador")
    private Trabajador trabajador;

    // Relación con Cliente
    @ManyToOne
    @JoinColumn(name = "idCliente", referencedColumnName = "idCliente")
    private Cliente cliente;

    // Relación con Tipo_Pago
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idTipoPago", referencedColumnName = "idTipoPago")
    private TipoPago tipoPago;

    // Relación con DetalleVenta (One-to-Many)
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVenta> detalles;

    public Venta() {
    }

    public Venta(Integer idVenta, String serieComprobante, String numeroComprobante, String codigoCotizacion,
                 LocalDateTime fechaVenta, EstadoVenta estadoVenta, BigDecimal totalVenta,
                 LocalDateTime fechaModificacion, String observaciones, Moneda moneda, Caja caja, Empresa empresa,
                 TipoComprobantePago tipoComprobantePago, Trabajador trabajador, Cliente cliente, TipoPago tipoPago,
                 List<DetalleVenta> detalles) {
        this.idVenta = idVenta;
        this.serieComprobante = serieComprobante;
        this.numeroComprobante = numeroComprobante;
        this.codigoCotizacion = codigoCotizacion;
        this.fechaVenta = fechaVenta;
        this.estadoVenta = estadoVenta;
        this.totalVenta = totalVenta;
        this.fechaModificacion = fechaModificacion;
        this.observaciones = observaciones;
        this.moneda = moneda;
        this.caja = caja;
        this.empresa = empresa;
        this.tipoComprobantePago = tipoComprobantePago;
        this.trabajador = trabajador;
        this.cliente = cliente;
        this.tipoPago = tipoPago;
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

    public String getCodigoCotizacion() {
        return codigoCotizacion;
    }

    public void setCodigoCotizacion(String codigoCotizacion) {
        this.codigoCotizacion = codigoCotizacion;
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

    public BigDecimal getValorIgvActual() {
        return valorIgvActual;
    }

    public void setValorIgvActual(BigDecimal valorIgvActual) {
        this.valorIgvActual = valorIgvActual;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Moneda getMoneda() {
        return moneda;
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }

    public Caja getCaja() {
        return caja;
    }

    public void setCaja(Caja caja) {
        this.caja = caja;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public TipoComprobantePago getTipoComprobantePago() {
        return tipoComprobantePago;
    }

    public void setTipoComprobantePago(TipoComprobantePago tipoComprobantePago) {
        this.tipoComprobantePago = tipoComprobantePago;
    }

    public Trabajador getTrabajador() {
        return trabajador;
    }

    public void setTrabajador(Trabajador trabajador) {
        this.trabajador = trabajador;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public TipoPago getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(TipoPago tipoPago) {
        this.tipoPago = tipoPago;
    }

    public List<DetalleVenta> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
    }
}