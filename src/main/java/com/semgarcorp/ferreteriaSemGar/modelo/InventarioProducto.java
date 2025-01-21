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
    private Integer stock; //stock específico del producto en un inventario particular

    @Column(name = "fecha_entrada", nullable = false)
    private LocalDate fechaEntrada; //fecha en la que el producto fue ingresado al inventario

    @Column(name = "fecha_salida")
    private LocalDate fechaSalida; //fecha en la que un producto fue retirado del inventario

    @Column(name = "precio_compra", nullable = false)
    private Double precioCompra; //precio al que la empresa adquirió el producto

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
                              Double precioCompra, Producto producto, Inventario inventario,
                              List<InventarioTransferencia> inventarioTransferencias) {
        this.idInventarioProducto = idInventarioProducto;
        this.stock = stock;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.precioCompra = precioCompra;
        this.producto = producto;
        this.inventario = inventario;
        this.inventarioTransferencias = inventarioTransferencias;
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

    public Double getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(Double precioCompra) {
        this.precioCompra = precioCompra;
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
}
