package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Transferencia {

    public enum EstadoTransferencia {
        ACTIVA,
        INACTIVA,

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idTransferencia;

    @Column
    private Integer cantidad;

    @Column
    private LocalDate fechaTransferencia;

    @Enumerated(EnumType.STRING)
    private EstadoTransferencia estadoTransferencia;

    @Column
    private BigDecimal precioUnitario;

    @ManyToOne
    @JoinColumn(name = "id_almacen_origen")
    private Almacen almacenOrigen;

    @ManyToOne
    @JoinColumn(name = "id_almacen_destino")
    private Almacen almacenDestino;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "id_trabajador_origen")
    private Trabajador trabajadorOrigen;

    @ManyToOne
    @JoinColumn(name = "id_trabajador_destino")
    private Trabajador trabajadorDestino;

    //Relación con InventarioTransferencia
    @OneToMany(mappedBy = "transferencia", cascade = CascadeType.ALL)
    private List<InventarioTransferencia> inventarioTransferencias;

    public Transferencia() {
    }

    public Transferencia(Integer idTransferencia, Integer cantidad, LocalDate fechaTransferencia, EstadoTransferencia estadoTransferencia,
                         BigDecimal precioUnitario, Almacen almacenOrigen, Almacen almacenDestino, Producto producto,
                         Trabajador trabajadorOrigen, List<InventarioTransferencia> inventarioTransferencias, Trabajador trabajadorDestino) {
        this.idTransferencia = idTransferencia;
        this.cantidad = cantidad;
        this.fechaTransferencia = fechaTransferencia;
        this.estadoTransferencia = estadoTransferencia;
        this.precioUnitario = precioUnitario;
        this.almacenOrigen = almacenOrigen;
        this.almacenDestino = almacenDestino;
        this.producto = producto;
        this.trabajadorOrigen = trabajadorOrigen;
        this.inventarioTransferencias = inventarioTransferencias;
        this.trabajadorDestino = trabajadorDestino;
    }

    public Integer getIdTransferencia() {
        return idTransferencia;
    }

    public void setIdTransferencia(Integer idTransferencia) {
        this.idTransferencia = idTransferencia;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDate getFechaTransferencia() {
        return fechaTransferencia;
    }

    public void setFechaTransferencia(LocalDate fechaTransferencia) {
        this.fechaTransferencia = fechaTransferencia;
    }

    public EstadoTransferencia getEstadoTransferencia() {
        return estadoTransferencia;
    }

    public void setEstadoTransferencia(EstadoTransferencia estadoTransferencia) {
        this.estadoTransferencia = estadoTransferencia;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Almacen getAlmacenOrigen() {
        return almacenOrigen;
    }

    public void setAlmacenOrigen(Almacen almacenOrigen) {
        this.almacenOrigen = almacenOrigen;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Almacen getAlmacenDestino() {
        return almacenDestino;
    }

    public void setAlmacenDestino(Almacen almacenDestino) {
        this.almacenDestino = almacenDestino;
    }

    public Trabajador getTrabajadorOrigen() {
        return trabajadorOrigen;
    }

    public void setTrabajadorOrigen(Trabajador trabajadorOrigen) {
        this.trabajadorOrigen = trabajadorOrigen;
    }

    public Trabajador getTrabajadorDestino() {
        return trabajadorDestino;
    }

    public void setTrabajadorDestino(Trabajador trabajadorDestino) {
        this.trabajadorDestino = trabajadorDestino;
    }

    public List<InventarioTransferencia> getInventarioTransferencias() {
        return inventarioTransferencias;
    }

    public void setInventarioTransferencias(List<InventarioTransferencia> inventarioTransferencias) {
        this.inventarioTransferencias = inventarioTransferencias;
    }
}
