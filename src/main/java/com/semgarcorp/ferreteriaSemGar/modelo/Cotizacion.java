package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Cotizacion {

    public enum EstadoCotizacion {
        PENDIENTE,  //0
        CONFIRMADA, //1
        RECHAZADA,  //2
        CANCELADA,  //3
        EXPIRADA    //4
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idCotizacion;

    private LocalDate fechaCotizacion;

    private Double totalCotizacion;

    private EstadoCotizacion estadoCotizacion;

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
    @ManyToOne
    @JoinColumn(name = "idTipoPago", referencedColumnName = "idTipoPago")
    private TipoPago tipoPago;

    public Cotizacion() {
    }

    public Cotizacion(Long idCotizacion, LocalDate fechaCotizacion, Double totalCotizacion,
                      EstadoCotizacion estadoCotizacion, String observaciones, LocalDate fechaModificacion,
                      Trabajador trabajador, Cliente cliente, TipoPago tipoPago) {
        this.idCotizacion = idCotizacion;
        this.fechaCotizacion = fechaCotizacion;
        this.totalCotizacion = totalCotizacion;
        this.estadoCotizacion = estadoCotizacion;
        this.observaciones = observaciones;
        this.fechaModificacion = fechaModificacion;
        this.trabajador = trabajador;
        this.cliente = cliente;
        this.tipoPago = tipoPago;
    }

    public Long getIdCotizacion() {
        return idCotizacion;
    }

    public void setIdCotizacion(Long idCotizacion) {
        this.idCotizacion = idCotizacion;
    }

    public LocalDate getFechaCotizacion() {
        return fechaCotizacion;
    }

    public void setFechaCotizacion(LocalDate fechaCotizacion) {
        this.fechaCotizacion = fechaCotizacion;
    }

    public Double getTotalCotizacion() {
        return totalCotizacion;
    }

    public void setTotalCotizacion(Double totalCotizacion) {
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
}
