package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Cotizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Cambiado a IDENTITY
    private Integer idCotizacion;

    private String codigoCotizacion;

    @NotNull(message = "La fecha de cotización no puede ser nula")
    private LocalDateTime fechaCotizacion;

    @NotNull(message = "El total de la cotización no puede ser nulo")
    @Column(precision = 10, scale = 2)
    private BigDecimal totalCotizacion; // Refleja la suma de todos los productos dentro de la cotización

    @Enumerated(EnumType.STRING)
    @NotNull(message = "El estado de la cotización no puede ser nulo")
    private EstadoCotizacion estadoCotizacion;

    @Lob  // Esto marca el atributo como tipo TEXT en la base de datos
    private String observaciones;

    private LocalDateTime fechaModificacion;

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

    // Relación con Empresa (Many-to-One)
    @ManyToOne
    @JoinColumn(name = "idEmpresa", nullable = false)
    private Empresa empresa;

    // Relación con DetalleCotizacion (One-to-Many)
    @OneToMany(mappedBy = "cotizacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleCotizacion> detalles;

    public Cotizacion() {
    }

    public Cotizacion(Integer idCotizacion, String codigoCotizacion, LocalDateTime fechaCotizacion,
                      BigDecimal totalCotizacion, EstadoCotizacion estadoCotizacion, String observaciones,
                      LocalDateTime fechaModificacion, Trabajador trabajador, Cliente cliente, TipoPago tipoPago,
                      Empresa empresa, List<DetalleCotizacion> detalles) {
        this.idCotizacion = idCotizacion;
        this.codigoCotizacion = codigoCotizacion;
        this.fechaCotizacion = fechaCotizacion;
        this.totalCotizacion = totalCotizacion;
        this.estadoCotizacion = estadoCotizacion;
        this.observaciones = observaciones;
        this.fechaModificacion = fechaModificacion;
        this.trabajador = trabajador;
        this.cliente = cliente;
        this.tipoPago = tipoPago;
        this.empresa = empresa;
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

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public List<DetalleCotizacion> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleCotizacion> detalles) {
        this.detalles = detalles;
    }
}