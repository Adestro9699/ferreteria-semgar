package com.semgarcorp.ferreteriaSemGar.dto;

import com.semgarcorp.ferreteriaSemGar.modelo.DetalleTransferencia;
import com.semgarcorp.ferreteriaSemGar.modelo.Producto;

import java.math.BigDecimal;

/**
 * DTO para transferir información de detalles de transferencia sin bucles en JSON.
 */
public class DetalleTransferenciaDTO {

    private Integer idDetalleTransferencia;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private String observaciones;
    
    // Información del producto
    private Integer idProducto;
    private String nombreProducto;
    private String descripcion;
    private BigDecimal precioProducto;
    private String codigoSKU;
    private String marca;
    private String material;
    private String codigoBarra;
    
    // Información de la unidad de medida
    private Integer idUnidadMedida;
    private String nombreUnidadMedida;
    private String abreviaturaUnidadMedida;

    public DetalleTransferenciaDTO() {
    }

    public DetalleTransferenciaDTO(DetalleTransferencia detalle) {
        this.idDetalleTransferencia = detalle.getIdDetalleTransferencia();
        this.cantidad = detalle.getCantidad();
        this.precioUnitario = detalle.getPrecioUnitario();
        this.observaciones = detalle.getObservaciones();
        
        // Información del producto
        if (detalle.getProducto() != null) {
            Producto producto = detalle.getProducto();
            this.idProducto = producto.getIdProducto();
            this.nombreProducto = producto.getNombreProducto();
            this.descripcion = producto.getDescripcion();
            this.precioProducto = producto.getPrecio();
            this.codigoSKU = producto.getCodigoSKU();
            this.marca = producto.getMarca();
            this.material = producto.getMaterial();
            this.codigoBarra = producto.getCodigoBarra();
            
            // Información de la unidad de medida
            if (producto.getUnidadMedida() != null) {
                this.idUnidadMedida = producto.getUnidadMedida().getIdUnidadMedida();
                this.nombreUnidadMedida = producto.getUnidadMedida().getNombreUnidad();
                this.abreviaturaUnidadMedida = producto.getUnidadMedida().getAbreviatura();
            }
        }
    }

    /**
     * Convierte este DTO en una entidad DetalleTransferencia
     */
    public DetalleTransferencia toEntity() {
        DetalleTransferencia detalle = new DetalleTransferencia();
        detalle.setIdDetalleTransferencia(this.idDetalleTransferencia);
        detalle.setCantidad(this.cantidad);
        detalle.setPrecioUnitario(this.precioUnitario);
        detalle.setObservaciones(this.observaciones);
        
        // Crear producto por ID
        if (this.idProducto != null) {
            Producto producto = new Producto();
            producto.setIdProducto(this.idProducto);
            detalle.setProducto(producto);
        }
        
        return detalle;
    }

    // Getters y Setters
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

    public BigDecimal getPrecioProducto() {
        return precioProducto;
    }

    public void setPrecioProducto(BigDecimal precioProducto) {
        this.precioProducto = precioProducto;
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
} 