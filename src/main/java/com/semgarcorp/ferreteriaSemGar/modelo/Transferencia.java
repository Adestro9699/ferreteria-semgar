package com.semgarcorp.ferreteriaSemGar.modelo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "transferencias")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Transferencia {

    public enum EstadoTransferencia {
        PENDIENTE,     // Transferencia creada pero no ejecutada
        EN_PROCESO,    // Transferencia en curso
        COMPLETADA,    // Transferencia finalizada exitosamente
        CANCELADA      // Transferencia cancelada
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTransferencia;

    @Column(name = "fecha_transferencia", nullable = false)
    private LocalDate fechaTransferencia;

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 20, nullable = false)
    private EstadoTransferencia estadoTransferencia;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDate fechaCreacion;

    @Column(name = "fecha_modificacion")
    private LocalDate fechaModificacion;

    // Almacén de origen
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_almacen_origen", nullable = false)
    private Almacen almacenOrigen;

    // Almacén de destino
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_almacen_destino", nullable = false)
    private Almacen almacenDestino;

    // Trabajador que realiza la transferencia
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_trabajador_origen")
    private Trabajador trabajadorOrigen;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_trabajador_destino")
    private Trabajador trabajadorDestino;

    // Relación con DetalleTransferencia
    @OneToMany(mappedBy = "transferencia", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<DetalleTransferencia> detalles = new ArrayList<>();

    public Transferencia() {
    }

    public Transferencia(Integer idTransferencia, LocalDate fechaTransferencia, String observaciones,
                        EstadoTransferencia estadoTransferencia, LocalDate fechaCreacion, 
                        LocalDate fechaModificacion, Almacen almacenOrigen, Almacen almacenDestino,
                        Trabajador trabajadorOrigen, Trabajador trabajadorDestino) {
        this.idTransferencia = idTransferencia;
        this.fechaTransferencia = fechaTransferencia;
        this.observaciones = observaciones;
        this.estadoTransferencia = estadoTransferencia;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
        this.almacenOrigen = almacenOrigen;
        this.almacenDestino = almacenDestino;
        this.trabajadorOrigen = trabajadorOrigen;
        this.trabajadorDestino = trabajadorDestino;
    }

    public Integer getIdTransferencia() {
        return idTransferencia;
    }

    public void setIdTransferencia(Integer idTransferencia) {
        this.idTransferencia = idTransferencia;
    }

    public LocalDate getFechaTransferencia() {
        return fechaTransferencia;
    }

    public void setFechaTransferencia(LocalDate fechaTransferencia) {
        this.fechaTransferencia = fechaTransferencia;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public EstadoTransferencia getEstadoTransferencia() {
        return estadoTransferencia;
    }

    public void setEstadoTransferencia(EstadoTransferencia estadoTransferencia) {
        this.estadoTransferencia = estadoTransferencia;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDate getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDate fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Almacen getAlmacenOrigen() {
        return almacenOrigen;
    }

    public void setAlmacenOrigen(Almacen almacenOrigen) {
        this.almacenOrigen = almacenOrigen;
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

    public List<DetalleTransferencia> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleTransferencia> detalles) {
        this.detalles = detalles;
    }
}
