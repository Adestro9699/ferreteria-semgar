package com.semgarcorp.ferreteriaSemGar.modelo;


import jakarta.persistence.*;
import org.hibernate.annotations.JoinColumnOrFormula;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Producto {


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

    @Column(columnDefinition = "TINYINT")
    private Integer activo;

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

    public Producto() {
    }

    public Producto(Integer idProducto, String nombreProducto, String descripcion, BigDecimal precio, Integer stock,
                    LocalDate fechaModificacion, String imagenURL, Integer activo, String codigoSKU, String marca,
                    String material, String codigoBarra, Proveedor proveedor, Categoria categoria) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.fechaModificacion = fechaModificacion;
        this.imagenURL = imagenURL;
        this.activo = activo;
        this.codigoSKU = codigoSKU;
        this.marca = marca;
        this.material = material;
        this.codigoBarra = codigoBarra;
        this.proveedor = proveedor;
        this.categoria = categoria;
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

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public LocalDate getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDate fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getImagenURL() {
        return imagenURL;
    }

    public void setImagenURL(String imagenURL) {
        this.imagenURL = imagenURL;
    }

    public Integer getActivo() {
        return activo;
    }

    public void setActivo(Integer activo) {
        this.activo = activo;
    }

    public String getCodigoSKU() {
        return codigoSKU;
    }

    public void setCodigoSKU(String codigoSKU) {
        this.codigoSKU = codigoSKU;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
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

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
