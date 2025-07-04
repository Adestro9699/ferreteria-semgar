package com.semgarcorp.ferreteriaSemGar.modelo;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL) // Ignora campos nulos al serializar
public class Producto {

    public enum EstadoProducto {
        ACTIVO,
        INACTIVO,
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idProducto;

    @Column(length = 255)
    private String nombreProducto; //nombre del producto

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(precision = 10, scale = 2)
    private BigDecimal precio; //precio de venta al cliente

    @Column(length = 11)
    @Transient // No se persiste en BD, se calcula dinámicamente
    private BigDecimal stock; // Stock global calculado sumando todos los ProductoAlmacen.stock

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
    @JoinColumn(name = "idSubcategoria")
    private Subcategoria subcategoria; //a qué subcategoría pertenece el producto

    @ManyToOne
    @JoinColumn(name = "idUnidadMedida") // Nombre de la columna en la tabla Producto
    private UnidadMedida unidadMedida; // Relación con la tabla UnidadMedida

    @OneToMany(mappedBy = "producto") //único atributo agregado para éste flujo de transferencia
    private List<ProductoAlmacen> productoAlmacenes = new ArrayList<>();

    // Relación con DetalleTransferencia
    @OneToMany(mappedBy = "producto")
    private List<DetalleTransferencia> detallesTransferencia = new ArrayList<>();

    // Relación con DetalleInventario
    @OneToMany(mappedBy = "producto")
    private List<DetalleInventario> detallesInventario = new ArrayList<>();

    public Producto() {
    }

    public Producto(Integer idProducto, String nombreProducto, String descripcion, BigDecimal precio, BigDecimal stock,
                    LocalDate fechaModificacion, String imagenURL, EstadoProducto estadoProducto, String codigoSKU,
                    String marca, String material, String codigoBarra, Proveedor proveedor, Subcategoria subcategoria,
                    UnidadMedida unidadMedida) {
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
        this.subcategoria = subcategoria;
        this.unidadMedida = unidadMedida;
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

    public BigDecimal getStock() {
        // Calcular stock total sumando todos los ProductoAlmacen
        if (productoAlmacenes != null && !productoAlmacenes.isEmpty()) {
            int stockTotal = productoAlmacenes.stream()
                .mapToInt(pa -> pa.getStock() != null ? pa.getStock() : 0)
                .sum();
            return new BigDecimal(stockTotal);
        }
        return BigDecimal.ZERO;
    }

    public void setStock(BigDecimal stock) {
        // No se permite setear el stock directamente, se calcula desde ProductoAlmacen
        // Este método se mantiene por compatibilidad pero no hace nada
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

    public Subcategoria getSubcategoria() {
        return subcategoria;
    }

    public void setSubcategoria(Subcategoria subcategoria) {
        this.subcategoria = subcategoria;
    }

    public UnidadMedida getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(UnidadMedida unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public List<ProductoAlmacen> getProductoAlmacenes() {
        return productoAlmacenes;
    }

    public void setProductoAlmacenes(List<ProductoAlmacen> productoAlmacenes) {
        this.productoAlmacenes = productoAlmacenes;
    }

    public List<DetalleTransferencia> getDetallesTransferencia() {
        return detallesTransferencia;
    }

    public void setDetallesTransferencia(List<DetalleTransferencia> detallesTransferencia) {
        this.detallesTransferencia = detallesTransferencia;
    }

    public List<DetalleInventario> getDetallesInventario() {
        return detallesInventario;
    }

    public void setDetallesInventario(List<DetalleInventario> detallesInventario) {
        this.detallesInventario = detallesInventario;
    }
}
