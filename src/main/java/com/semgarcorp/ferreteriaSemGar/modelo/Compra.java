package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Compra {

    // Enum para el estado de la compra
    public enum EstadoCompra {
        PENDIENTE,    // La compra está pendiente de confirmación o pago
        COMPLETADA,   // La compra ha sido completada
        CANCELADA     // La compra ha sido cancelada
    }
    public enum TipoDocumento {
        FACTURA,          // Factura
        NOTA_CREDITO,     // Nota de Crédito
        NOTA_DEBITO,      // Nota de Débito
        RECIBO,           // Recibo
        OTRO              // Otro tipo de documento
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCompra; // Identificador único de la compra

    @NotNull(message = "El número de factura no puede estar vacío")
    @Size(max = 50, message = "El número de factura no puede tener más de 50 caracteres")
    private String numeroFactura; // Número de factura o documento

    @NotNull(message = "La fecha de compra no puede estar vacía")
    private LocalDate fechaCompra; // Fecha en la que se realizó la compra

    @NotNull(message = "El tipo de documento no puede estar vacío")
    @Enumerated(EnumType.STRING)
    private TipoDocumento tipoDocumento; // Tipo de documento (Factura, Nota de Crédito, etc.)

    @NotNull(message = "El total de la compra no puede estar vacío")
    @Column(precision = 10, scale = 2)
    private BigDecimal totalCompra; // Monto total de la compra

    @Column(columnDefinition = "TEXT")
    private String observaciones; // Observaciones adicionales sobre la compra

    @NotNull(message = "El estado de la compra no puede estar vacío")
    @Enumerated(EnumType.STRING)
    private EstadoCompra estadoCompra; // Estado de la compra (PENDIENTE, COMPLETADA, CANCELADA)

    @ManyToOne
    @JoinColumn(name = "idProveedor", nullable = false)
    private Proveedor proveedor; // Proveedor asociado a la compra

    // Constructor vacío
    public Compra() {
    }

    // Constructor con parámetros

    public Compra(Integer idCompra, String numeroFactura, LocalDate fechaCompra, TipoDocumento tipoDocumento,
                  BigDecimal totalCompra, String observaciones, EstadoCompra estadoCompra, Proveedor proveedor) {
        this.idCompra = idCompra;
        this.numeroFactura = numeroFactura;
        this.fechaCompra = fechaCompra;
        this.tipoDocumento = tipoDocumento;
        this.totalCompra = totalCompra;
        this.observaciones = observaciones;
        this.estadoCompra = estadoCompra;
        this.proveedor = proveedor;
    }


    // Getters y Setters


    public Integer getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(Integer idCompra) {
        this.idCompra = idCompra;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public LocalDate getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDate fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
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

    public EstadoCompra getEstadoCompra() {
        return estadoCompra;
    }

    public void setEstadoCompra(EstadoCompra estadoCompra) {
        this.estadoCompra = estadoCompra;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }
}