package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
//import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Entity
public class Cotizacion {

    public enum EstadoCotizacion {
        PENDIENTE,
        CONFIRMADA,
        RECHAZADA,
        CANCELADA,
        EXPIRADA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idCotizacion;

    @NotNull(message = "La fecha de cotización no puede ser nula")
    private LocalDate fechaCotizacion;

    /*@NotNull(message = "El total de la cotización no puede ser nulo")
    @Column(precision = 10, scale = 2)
    private BigDecimal totalCotizacion; //refleja la suma de todos los productos dentro de la cotizacion*/

    @Enumerated(EnumType.STRING)
    @NotNull(message = "El estado de la cotización no puede ser nulo")
    private EstadoCotizacion estadoCotizacion;

    @Lob  // Esto marca el atributo como tipo TEXT en la base de datos
    private String observaciones;

    private LocalDate fechaModificacion;

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

    // Relación con la tabla puente VentaImpuestoCotizacion
    @OneToMany(mappedBy = "cotizacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VentaImpuestoCotizacion> ventaImpuestoCotizaciones;

    public Cotizacion() {
    }

    public Cotizacion(Integer idCotizacion, LocalDate fechaCotizacion, EstadoCotizacion estadoCotizacion, String observaciones,
                      LocalDate fechaModificacion, Trabajador trabajador, Cliente cliente, TipoPago tipoPago, Set<VentaImpuestoCotizacion> ventaImpuestoCotizaciones) {
        this.idCotizacion = idCotizacion;
        this.fechaCotizacion = fechaCotizacion;
        this.estadoCotizacion = estadoCotizacion;
        this.observaciones = observaciones;
        this.fechaModificacion = fechaModificacion;
        this.trabajador = trabajador;
        this.cliente = cliente;
        this.tipoPago = tipoPago;
        this.ventaImpuestoCotizaciones = ventaImpuestoCotizaciones;
    }

    public Integer getIdCotizacion() {
        return idCotizacion;
    }

    public void setIdCotizacion(Integer idCotizacion) {
        this.idCotizacion = idCotizacion;
    }

    public LocalDate getFechaCotizacion() {
        return fechaCotizacion;
    }

    public void setFechaCotizacion(LocalDate fechaCotizacion) {
        this.fechaCotizacion = fechaCotizacion;
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

    public LocalDate getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDate fechaModificacion) {
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

    public Set<VentaImpuestoCotizacion> getVentaImpuestoCotizaciones() {
        return ventaImpuestoCotizaciones;
    }

    public void setVentaImpuestoCotizaciones(Set<VentaImpuestoCotizacion> ventaImpuestoCotizaciones) {
        this.ventaImpuestoCotizaciones = ventaImpuestoCotizaciones;
    }
}
