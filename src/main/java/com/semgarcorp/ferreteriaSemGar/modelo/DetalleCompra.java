package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
public class DetalleCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDetalleCompra; // Identificador único del detalle de compra

    @ManyToOne
    @JoinColumn(name = "idCompra", nullable = false)
    private Compra compra; // Relación con la tabla Compra

    @ManyToOne
    @JoinColumn(name = "idProducto", nullable = false)
    private Producto producto; // Relación con la tabla Producto

    @NotNull(message = "La cantidad no puede estar vacía")
    private BigDecimal cantidad; // Cantidad comprada del producto

    @NotNull(message = "El precio unitario no puede estar vacío")
    @Column(precision = 10, scale = 2)
    private BigDecimal precioUnitario; // Precio unitario del producto al momento de la compra

    @Column(precision = 10, scale = 2)
    private BigDecimal subtotal; // Subtotal calculado (cantidad * precioUnitario)

    // Constructor vacío
    public DetalleCompra() {
    }

    // Constructor con parámetros
    public DetalleCompra(Integer idDetalleCompra, Compra compra, Producto producto, BigDecimal cantidad,
                         BigDecimal precioUnitario, BigDecimal subtotal) {
        this.idDetalleCompra = idDetalleCompra;
        this.compra = compra;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
    }

    // Getters y Setters

    public Integer getIdDetalleCompra() {
        return idDetalleCompra;
    }

    public void setIdDetalleCompra(Integer idDetalleCompra) {
        this.idDetalleCompra = idDetalleCompra;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}