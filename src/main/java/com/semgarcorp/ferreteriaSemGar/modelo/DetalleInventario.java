package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "detalles_inventario")
public class DetalleInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDetalleInventario;

    @Column(name = "stock_sistema", nullable = false)
    private Integer stockSistema; // Stock según ProductoAlmacen (sistema)

    @Column(name = "stock_fisico", nullable = false)
    private Integer stockFisico; // Stock contado físicamente

    @Column(name = "diferencia")
    private Integer diferencia; // stockFisico - stockSistema

    @Column(name = "observaciones", length = 500)
    private String observaciones; // Observaciones sobre la diferencia

    // Relación con Inventario (proceso de conteo al que pertenece)
    @ManyToOne
    @JoinColumn(name = "idInventario", nullable = false)
    private Inventario inventario;

    // Relación con Producto
    @ManyToOne
    @JoinColumn(name = "idProducto", nullable = false)
    private Producto producto;

    public DetalleInventario() {
    }

    public DetalleInventario(Integer idDetalleInventario, Integer stockSistema, Integer stockFisico,
                            Integer diferencia, String observaciones, Inventario inventario, Producto producto) {
        this.idDetalleInventario = idDetalleInventario;
        this.stockSistema = stockSistema;
        this.stockFisico = stockFisico;
        this.diferencia = diferencia;
        this.observaciones = observaciones;
        this.inventario = inventario;
        this.producto = producto;
    }

    public Integer getIdDetalleInventario() {
        return idDetalleInventario;
    }

    public void setIdDetalleInventario(Integer idDetalleInventario) {
        this.idDetalleInventario = idDetalleInventario;
    }

    public Integer getStockSistema() {
        return stockSistema;
    }

    public void setStockSistema(Integer stockSistema) {
        this.stockSistema = stockSistema;
    }

    public Integer getStockFisico() {
        return stockFisico;
    }

    public void setStockFisico(Integer stockFisico) {
        this.stockFisico = stockFisico;
    }

    public Integer getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(Integer diferencia) {
        this.diferencia = diferencia;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Inventario getInventario() {
        return inventario;
    }

    public void setInventario(Inventario inventario) {
        this.inventario = inventario;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
} 