package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class InventarioProducto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idInventarioProducto;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "fecha_entrada", nullable = false)
    private LocalDate fechaEntrada;

    @Column(name = "fecha_salida")
    private LocalDate fechaSalida;

    @Column(name = "precio_venta", nullable = false)
    private Double precioVenta;

    @Column(name = "precio_compra", nullable = false)
    private Double precioCompra;

    //Relación con Producto
    @ManyToOne
    @JoinColumn(name = "idProducto", nullable = false)
    private Producto producto;

    //Relación con Inventario
    @ManyToOne
    @JoinColumn(name = "idInventario", nullable = false)
    private Inventario inventario;

    //Relación con InventarioTransferencia
    @OneToMany(mappedBy = "inventarioProducto", cascade = CascadeType.ALL)
    private List<InventarioTransferencia> inventarioTransferencias;


    public InventarioProducto() {
    }

    public InventarioProducto(Integer idInventarioProducto, Integer stock, LocalDate fechaEntrada, LocalDate fechaSalida,
                              Double precioVenta, Producto producto, List<InventarioTransferencia> inventarioTransferencias,
                              Inventario inventario, Double precioCompra) {
        this.idInventarioProducto = idInventarioProducto;
        this.stock = stock;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.precioVenta = precioVenta;
        this.producto = producto;
        this.inventarioTransferencias = inventarioTransferencias;
        this.inventario = inventario;
        this.precioCompra = precioCompra;
    }

    public Integer getIdInventarioProducto() {
        return idInventarioProducto;
    }

    public void setIdInventarioProducto(Integer idInventarioProducto) {
        this.idInventarioProducto = idInventarioProducto;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public LocalDate getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(LocalDate fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDate fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public Double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(Double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Inventario getInventario() {
        return inventario;
    }

    public void setInventario(Inventario inventario) {
        this.inventario = inventario;
    }

    public List<InventarioTransferencia> getInventarioTransferencias() {
        return inventarioTransferencias;
    }

    public void setInventarioTransferencias(List<InventarioTransferencia> inventarioTransferencias) {
        this.inventarioTransferencias = inventarioTransferencias;
    }

    public Double getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(Double precioCompra) {
        this.precioCompra = precioCompra;
    }
}
