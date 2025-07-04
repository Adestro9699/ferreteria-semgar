package com.semgarcorp.ferreteriaSemGar.modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "detalles_transferencia")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetalleTransferencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDetalleTransferencia;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad; // Cantidad transferida

    @Column(name = "precio_unitario", precision = 10, scale = 2)
    private BigDecimal precioUnitario; // Precio unitario al momento de la transferencia

    @Column(name = "observaciones", length = 500)
    private String observaciones; // Observaciones específicas del detalle

    // Relación con Transferencia (cabecera)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_transferencia", nullable = false)
    @JsonIgnore
    private Transferencia transferencia;

    // Relación con Producto
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idProducto", nullable = false)
    private Producto producto;

    public DetalleTransferencia() {
    }

    public DetalleTransferencia(Integer idDetalleTransferencia, Integer cantidad,
                               BigDecimal precioUnitario, String observaciones,
                               Transferencia transferencia, Producto producto) {
        this.idDetalleTransferencia = idDetalleTransferencia;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.observaciones = observaciones;
        this.transferencia = transferencia;
        this.producto = producto;
    }

    public Integer getIdDetalleTransferencia() {
        return idDetalleTransferencia;
    }

    public void setIdDetalleTransferencia(Integer idDetalleTransferencia) {
        this.idDetalleTransferencia = idDetalleTransferencia;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Transferencia getTransferencia() {
        return transferencia;
    }

    public void setTransferencia(Transferencia transferencia) {
        this.transferencia = transferencia;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
} 