package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class TipoComprobantePago {

    // Enum para el estado del comprobante de pago
    public enum Estado {
        ACTIVO,
        INACTIVO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idTipoComprobantePago;

    @Column(length = 150) // Define el campo como VARCHAR(150) en la base de datos
    @NotNull(message = "El nombre del comprobante de pago no puede ser nulo")
    private String nombre; // Cambiado de enum a String

    @NotNull(message = "La descripción no puede ser nula")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "El estado del comprobante de pago no puede ser nulo")
    private Estado estado;

    // Constructor vacío
    public TipoComprobantePago() {
    }

    // Constructor con parámetros
    public TipoComprobantePago(Integer idTipoComprobantePago, String nombre, String descripcion, Estado estado) {
        this.idTipoComprobantePago = idTipoComprobantePago;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    // Getters y Setters
    public Integer getIdTipoComprobantePago() {
        return idTipoComprobantePago;
    }

    public void setIdTipoComprobantePago(Integer idTipoComprobantePago) {
        this.idTipoComprobantePago = idTipoComprobantePago;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}