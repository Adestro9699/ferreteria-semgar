package com.semgarcorp.ferreteriaSemGar.dto;

import com.semgarcorp.ferreteriaSemGar.modelo.Producto;
import com.semgarcorp.ferreteriaSemGar.modelo.ProductoAlmacen;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * DTO para transferir información de productos sin bucles en JSON.
 * Incluye toda la información del producto y el stock correspondiente.
 */
public class ProductoDTO {

    private Integer idProducto;
    private String nombreProducto;
    private String descripcion;
    private BigDecimal precio;
    private Integer stockTotal;
    private LocalDate fechaModificacion;
    private String imagenURL;
    private String estadoProducto;
    private String codigoSKU;
    private String marca;
    private String material;
    private String codigoBarra;
    
    // Información del proveedor (solo datos básicos)
    private Integer idProveedor;
    private String nombreProveedor;
    private String rucProveedor;
    
    // Información de la subcategoría (solo datos básicos)
    private Integer idSubcategoria;
    private String nombreSubcategoria;
    private Integer idCategoria;
    private String nombreCategoria;
    
    // Información de la unidad de medida (solo datos básicos)
    private Integer idUnidadMedida;
    private String nombreUnidadMedida;
    private String abreviaturaUnidadMedida;
    
    // Stock por almacén
    private Map<String, Integer> stockPorAlmacen;

    // Stock específico del almacén de la sucursal del usuario autenticado
    private Integer stockSucursal;

    // Constructor vacío
    public ProductoDTO() {
        this.stockPorAlmacen = new HashMap<>();
    }

    // Constructor desde entidad Producto
    public ProductoDTO(Producto producto) {
        this();
        
        if (producto != null) {
            this.idProducto = producto.getIdProducto();
            this.nombreProducto = producto.getNombreProducto();
            this.descripcion = producto.getDescripcion();
            this.precio = producto.getPrecio();
            this.fechaModificacion = producto.getFechaModificacion();
            this.imagenURL = producto.getImagenURL();
            this.estadoProducto = producto.getEstadoProducto() != null ? producto.getEstadoProducto().name() : null;
            this.codigoSKU = producto.getCodigoSKU();
            this.marca = producto.getMarca();
            this.material = producto.getMaterial();
            this.codigoBarra = producto.getCodigoBarra();
            
            // Información del proveedor
            if (producto.getProveedor() != null) {
                this.idProveedor = producto.getProveedor().getIdProveedor();
                this.nombreProveedor = producto.getProveedor().getNombre();
                this.rucProveedor = producto.getProveedor().getTelefono(); // Usando teléfono como RUC temporalmente
            }
            
            // Información de la subcategoría
            if (producto.getSubcategoria() != null) {
                this.idSubcategoria = producto.getSubcategoria().getIdSubcategoria();
                this.nombreSubcategoria = producto.getSubcategoria().getNombre();
                
                if (producto.getSubcategoria().getCategoria() != null) {
                    this.idCategoria = producto.getSubcategoria().getCategoria().getIdCategoria();
                    this.nombreCategoria = producto.getSubcategoria().getCategoria().getNombre();
                }
            }
            
            // Información de la unidad de medida
            if (producto.getUnidadMedida() != null) {
                this.idUnidadMedida = producto.getUnidadMedida().getIdUnidadMedida();
                this.nombreUnidadMedida = producto.getUnidadMedida().getNombreUnidad();
                this.abreviaturaUnidadMedida = producto.getUnidadMedida().getAbreviatura();
            }
            
            // Stock total y por almacén
            this.stockTotal = producto.getStock() != null ? producto.getStock().intValue() : 0;
            
            // Mapear stock por almacén
            if (producto.getProductoAlmacenes() != null) {
                for (ProductoAlmacen pa : producto.getProductoAlmacenes()) {
                    if (pa.getAlmacen() != null && pa.getStock() != null) {
                        String nombreAlmacen = pa.getAlmacen().getNombre();
                        this.stockPorAlmacen.put(nombreAlmacen, pa.getStock());
                    }
                }
            }
        }
    }

    // Constructor con stock específico por almacén
    public ProductoDTO(Producto producto, Integer stockEspecifico) {
        this(producto);
        this.stockTotal = stockEspecifico != null ? stockEspecifico : 0;
    }

    // Getters y Setters
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

    public Integer getStockTotal() {
        return stockTotal;
    }

    public void setStockTotal(Integer stockTotal) {
        this.stockTotal = stockTotal;
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

    public String getEstadoProducto() {
        return estadoProducto;
    }

    public void setEstadoProducto(String estadoProducto) {
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

    public Integer getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Integer idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

    public String getRucProveedor() {
        return rucProveedor;
    }

    public void setRucProveedor(String rucProveedor) {
        this.rucProveedor = rucProveedor;
    }

    public Integer getIdSubcategoria() {
        return idSubcategoria;
    }

    public void setIdSubcategoria(Integer idSubcategoria) {
        this.idSubcategoria = idSubcategoria;
    }

    public String getNombreSubcategoria() {
        return nombreSubcategoria;
    }

    public void setNombreSubcategoria(String nombreSubcategoria) {
        this.nombreSubcategoria = nombreSubcategoria;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public Integer getIdUnidadMedida() {
        return idUnidadMedida;
    }

    public void setIdUnidadMedida(Integer idUnidadMedida) {
        this.idUnidadMedida = idUnidadMedida;
    }

    public String getNombreUnidadMedida() {
        return nombreUnidadMedida;
    }

    public void setNombreUnidadMedida(String nombreUnidadMedida) {
        this.nombreUnidadMedida = nombreUnidadMedida;
    }

    public String getAbreviaturaUnidadMedida() {
        return abreviaturaUnidadMedida;
    }

    public void setAbreviaturaUnidadMedida(String abreviaturaUnidadMedida) {
        this.abreviaturaUnidadMedida = abreviaturaUnidadMedida;
    }

    public Map<String, Integer> getStockPorAlmacen() {
        return stockPorAlmacen;
    }

    public void setStockPorAlmacen(Map<String, Integer> stockPorAlmacen) {
        this.stockPorAlmacen = stockPorAlmacen;
    }

    public Integer getStockSucursal() {
        return stockSucursal;
    }

    public void setStockSucursal(Integer stockSucursal) {
        this.stockSucursal = stockSucursal;
    }

    /**
     * Convierte este DTO en una entidad Producto, mapeando solo los IDs de las relaciones.
     */
    public Producto toEntity() {
        Producto producto = new Producto();
        producto.setIdProducto(this.idProducto);
        producto.setNombreProducto(this.nombreProducto);
        producto.setDescripcion(this.descripcion);
        producto.setPrecio(this.precio);
        producto.setFechaModificacion(this.fechaModificacion);
        producto.setImagenURL(this.imagenURL);
        if (this.estadoProducto != null) {
            producto.setEstadoProducto(Producto.EstadoProducto.valueOf(this.estadoProducto));
        }
        producto.setCodigoSKU(this.codigoSKU);
        producto.setMarca(this.marca);
        producto.setMaterial(this.material);
        producto.setCodigoBarra(this.codigoBarra);
        // Relaciones por ID
        if (this.idProveedor != null) {
            var proveedor = new com.semgarcorp.ferreteriaSemGar.modelo.Proveedor();
            proveedor.setIdProveedor(this.idProveedor);
            producto.setProveedor(proveedor);
        }
        if (this.idSubcategoria != null) {
            var subcategoria = new com.semgarcorp.ferreteriaSemGar.modelo.Subcategoria();
            subcategoria.setIdSubcategoria(this.idSubcategoria);
            producto.setSubcategoria(subcategoria);
        }
        if (this.idUnidadMedida != null) {
            var unidad = new com.semgarcorp.ferreteriaSemGar.modelo.UnidadMedida();
            unidad.setIdUnidadMedida(this.idUnidadMedida);
            producto.setUnidadMedida(unidad);
        }
        // El stockTotal y stockPorAlmacen no se mapean aquí, se gestionan por lógica de negocio
        return producto;
    }

    /**
     * Actualiza una entidad Producto existente con los datos de este DTO.
     */
    public void updateEntity(Producto producto) {
        producto.setNombreProducto(this.nombreProducto);
        producto.setDescripcion(this.descripcion);
        producto.setPrecio(this.precio);
        producto.setFechaModificacion(this.fechaModificacion);
        producto.setImagenURL(this.imagenURL);
        if (this.estadoProducto != null) {
            producto.setEstadoProducto(Producto.EstadoProducto.valueOf(this.estadoProducto));
        }
        producto.setCodigoSKU(this.codigoSKU);
        producto.setMarca(this.marca);
        producto.setMaterial(this.material);
        producto.setCodigoBarra(this.codigoBarra);
        // Relaciones por ID
        if (this.idProveedor != null) {
            var proveedor = new com.semgarcorp.ferreteriaSemGar.modelo.Proveedor();
            proveedor.setIdProveedor(this.idProveedor);
            producto.setProveedor(proveedor);
        }
        if (this.idSubcategoria != null) {
            var subcategoria = new com.semgarcorp.ferreteriaSemGar.modelo.Subcategoria();
            subcategoria.setIdSubcategoria(this.idSubcategoria);
            producto.setSubcategoria(subcategoria);
        }
        if (this.idUnidadMedida != null) {
            var unidad = new com.semgarcorp.ferreteriaSemGar.modelo.UnidadMedida();
            unidad.setIdUnidadMedida(this.idUnidadMedida);
            producto.setUnidadMedida(unidad);
        }
        // El stockTotal y stockPorAlmacen no se mapean aquí, se gestionan por lógica de negocio
    }
} 