package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;

@Entity
public class InventarioTransferencia {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idInventarioTransferencia;

    @ManyToOne
    @JoinColumn(name = "id_transferencia", nullable = false)
    private Transferencia transferencia;

    @ManyToOne
    @JoinColumn(name = "id_inventario_producto", nullable = false)
    private InventarioProducto inventarioProducto;

    @Column(nullable = false)
    private Integer cantidad;

    public InventarioTransferencia() {
    }

    public InventarioTransferencia(Integer idInventarioTransferencia, Transferencia transferencia, Integer cantidad,
                                   InventarioProducto inventarioProducto) {
        this.idInventarioTransferencia = idInventarioTransferencia;
        this.transferencia = transferencia;
        this.cantidad = cantidad;
        this.inventarioProducto = inventarioProducto;
    }

    public Integer getIdInventarioTransferencia() {
        return idInventarioTransferencia;
    }

    public void setIdInventarioTransferencia(Integer idInventarioTransferencia) {
        this.idInventarioTransferencia = idInventarioTransferencia;
    }

    public Transferencia getTransferencia() {
        return transferencia;
    }

    public void setTransferencia(Transferencia transferencia) {
        this.transferencia = transferencia;
    }

    public InventarioProducto getInventarioProducto() {
        return inventarioProducto;
    }

    public void setInventarioProducto(InventarioProducto inventarioProducto) {
        this.inventarioProducto = inventarioProducto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
