package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class TransaccionBancaria {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idTransaccion;

    @NotNull(message = "La fecha de transacción no puede ser nula")
    @Column
    private LocalDate fechaTransaccion;

    @NotNull(message = "El monto no puede ser nulo")
    @Column(precision = 10, scale = 2)
    private BigDecimal monto;

    @NotNull(message = "El tipo de transacción no puede ser nulo")
    @Column(length = 100)
    private String tipoTransaccion;

    @Column(length = 255)
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;

    // Constructor vacío
    public TransaccionBancaria() {
    }

    // Constructor con parámetros
    public TransaccionBancaria(Integer idTransaccion, LocalDate fechaTransaccion, BigDecimal monto,
                               String tipoTransaccion, String descripcion, Usuario usuario) {
        this.idTransaccion = idTransaccion;
        this.fechaTransaccion = fechaTransaccion;
        this.monto = monto;
        this.tipoTransaccion = tipoTransaccion;
        this.descripcion = descripcion;
        this.usuario = usuario;
    }

    // Getters y setters
    public Integer getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(Integer idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public LocalDate getFechaTransaccion() {
        return fechaTransaccion;
    }

    public void setFechaTransaccion(LocalDate fechaTransaccion) {
        this.fechaTransaccion = fechaTransaccion;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setTipoTransaccion(String tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
