package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class OrdenCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idOrdenCompra;

    private LocalDate fechaOrden;

    private LocalDate fechaEntrega;

    @Column(length = 20)
    private String estadoOrden;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalCompra;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "idProveedor")
    private Proveedor proveedor;

    public OrdenCompra() {
    }

    public OrdenCompra(Integer idOrdenCompra, LocalDate fechaOrden, LocalDate fechaEntrega, String estadoOrden,
                       BigDecimal totalCompra, String observaciones, Proveedor proveedor) {
        this.idOrdenCompra = idOrdenCompra;
        this.fechaOrden = fechaOrden;
        this.fechaEntrega = fechaEntrega;
        this.estadoOrden = estadoOrden;
        this.totalCompra = totalCompra;
        this.observaciones = observaciones;
        this.proveedor = proveedor;
    }

    public Integer getIdOrdenCompra() {
        return idOrdenCompra;
    }

    public void setIdOrdenCompra(Integer idOrdenCompra) {
        this.idOrdenCompra = idOrdenCompra;
    }

    public LocalDate getFechaOrden() {
        return fechaOrden;
    }

    public void setFechaOrden(LocalDate fechaOrden) {
        this.fechaOrden = fechaOrden;
    }

    public LocalDate getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDate fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getEstadoOrden() {
        return estadoOrden;
    }

    public void setEstadoOrden(String estadoOrden) {
        this.estadoOrden = estadoOrden;
    }

    public BigDecimal getTotalCompra() {
        return totalCompra;
    }

    public void setTotalCompra(BigDecimal totalCompra) {
        this.totalCompra = totalCompra;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }
}
