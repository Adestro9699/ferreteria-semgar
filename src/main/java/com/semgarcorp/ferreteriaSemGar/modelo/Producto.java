package com.semgarcorp.ferreteriaSemGar.modelo;


import jakarta.persistence.*;
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
    private String nombreProducto; //nombre del producto

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(precision = 10, scale = 2)
    private BigDecimal precio; //precio de venta al cliente

    @Column(length = 11)
    private Integer stock; //stock global del producto en todos los inventarios

    private LocalDate fechaModificacion;

    @Column(length = 255)
    private String imagenURL; //imagen del producto

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private EstadoProducto estadoProducto; //estado ACTIVO o INACTIVO del producto

    @Column(length = 50)
    private String codigoSKU;

    @Column(length = 100)
    private String marca; //marca del producto

    @Column(length = 100)
    private String material; //material predominante o de mayor relevancia del producto

    @Column(length = 50)
    private String codigoBarra; //código de barra del producto

    @ManyToOne
    @JoinColumn(name = "idProveedor")
    private Proveedor proveedor; //quien provee el producto

    @ManyToOne
    @JoinColumn(name = "idCategoria")
    private Categoria categoria; //a qué categoría pertenece el producto

    @ManyToOne
    @JoinColumn(name = "idSubcategoria")
    private Subcategoria subcategoria;

    @OneToMany(mappedBy = "producto")
    private List<InventarioProducto> inventarioProductos = new ArrayList<>();

    public Producto() {
    }

    public Producto(Integer idProducto, String nombreProducto, String descripcion, BigDecimal precio, Integer stock,
                    LocalDate fechaModificacion, String imagenURL, EstadoProducto estadoProducto, String codigoSKU,
                    String marca, String material, String codigoBarra, Proveedor proveedor, Categoria categoria,
                    Subcategoria subcategoria, List<InventarioProducto> inventarioProductos) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.fechaModificacion = fechaModificacion;
        this.imagenURL = imagenURL;
        this.estadoProducto = estadoProducto;
        this.codigoSKU = codigoSKU;
        this.marca = marca;
        this.material = material;
        this.codigoBarra = codigoBarra;
        this.proveedor = proveedor;
        this.categoria = categoria;
        this.subcategoria = subcategoria;
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

    public EstadoProducto getEstadoProducto() {
        return estadoProducto;
    }

    public void setEstadoProducto(EstadoProducto estadoProducto) {
        this.estadoProducto = estadoProducto;
    }

    public String getCodigoSKU() {
        return codigoSKU;
    }

    public void setCodigoSKU(String codigoSKU) {
        this.codigoSKU = codigoSKU;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
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

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Subcategoria getSubcategoria() {
        return subcategoria;
    }

    public void setSubcategoria(Subcategoria subcategoria) {
        this.subcategoria = subcategoria;
    }

    public List<InventarioProducto> getInventarioProductos() {
        return inventarioProductos;
    }

    public void setInventarioProductos(List<InventarioProducto> inventarioProductos) {
        this.inventarioProductos = inventarioProductos;
    }
}
