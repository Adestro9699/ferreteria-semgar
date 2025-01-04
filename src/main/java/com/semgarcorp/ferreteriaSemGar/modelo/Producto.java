package com.semgarcorp.ferreteriaSemGar.modelo;


import jakarta.persistence.*;
import org.hibernate.annotations.JoinColumnOrFormula;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Producto {

    public enum EstadoProducto {
        ACTIVO,
        INACTIVO,
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idProducto;

    @Column(length = 255)
    private String nombreProducto;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(length = 11)
    private Integer stock;

    private LocalDate fechaModificacion;

    @Column(length = 255)
    private String imagenURL;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private EstadoProducto estadoProducto;

    @Column(length = 50)
    private String codigoSKU;

    @Column(length = 100)
    private String marca;

    @Column(length = 100)
    private String material;

    @Column(length = 50)
    private String codigoBarra;

    @ManyToOne
    @JoinColumn(name = "idProveedor")
    private Proveedor proveedor;

    @ManyToOne
    @JoinColumn(name = "idCategoria")
    private Categoria categoria;

    @OneToMany(mappedBy = "producto")
    private List<InventarioProducto> inventarioProductos = new ArrayList<>();

    public Producto() {
    }

    public Producto(Integer idProducto, String nombreProducto, BigDecimal precio, String descripcion, Integer stock,
                    String imagenURL, LocalDate fechaModificacion, EstadoProducto estadoProducto, String codigoBarra,
                    Proveedor proveedor, String material, String marca, String codigoSKU, Categoria categoria,
                    List<InventarioProducto> inventarioProductos) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.precio = precio;
        this.descripcion = descripcion;
        this.stock = stock;
        this.imagenURL = imagenURL;
        this.fechaModificacion = fechaModificacion;
        this.estadoProducto = estadoProducto;
        this.codigoBarra = codigoBarra;
        this.proveedor = proveedor;
        this.material = material;
        this.marca = marca;
        this.codigoSKU = codigoSKU;
        this.categoria = categoria;
        this.inventarioProductos = inventarioProductos;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public LocalDate getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDate fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getCodigoSKU() {
        return codigoSKU;
    }

    public void setCodigoSKU(String codigoSKU) {
        this.codigoSKU = codigoSKU;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public EstadoProducto getEstadoProducto() {
        return estadoProducto;
    }

    public void setEstadoProducto(EstadoProducto estadoProducto) {
        this.estadoProducto = estadoProducto;
    }

    public String getImagenURL() {
        return imagenURL;
    }

    public void setImagenURL(String imagenURL) {
        this.imagenURL = imagenURL;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getCodigoBarra() {
        return codigoBarra;
    }

    public void setCodigoBarra(String codigoBarra) {
        this.codigoBarra = codigoBarra;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public List<InventarioProducto> getInventarioProductos() {
        return inventarioProductos;
    }

    public void setInventarioProductos(List<InventarioProducto> inventarioProductos) {
        this.inventarioProductos = inventarioProductos;
    }
}
