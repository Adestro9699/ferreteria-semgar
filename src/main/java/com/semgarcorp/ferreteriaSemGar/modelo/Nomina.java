package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Nomina {

    public enum EstadoPagoNomina {
        PENDIENTE,
        PAGADO,
        CANCELADO,
        EN_PROCESO,
        FALLIDO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idNomina;

    @NotNull(message = "El salario base no puede ser nulo")
    @Min(value = 0, message = "El salario base no puede ser menor a cero")
    @Column(precision = 10, scale = 2) // Para decimal(10,2) en la base de datos
    private BigDecimal salarioBase;

    @Min(value = 0, message = "Las bonificaciones no pueden ser menores a cero")
    @Column(precision = 10, scale = 2) // Para decimal(10,2) en la base de datos
    private BigDecimal bonificacion;

    @Column(length = 255) // varchar(255)
    private String conceptoBonificacion;

    @Min(value = 0, message = "El descuento no puede ser menor a cero")
    @Column(precision = 10, scale = 2) // Para decimal(10,2) en la base de datos
    private BigDecimal descuentoNomina;

    @Column(length = 255) // varchar(255)
    private String conceptoDescuento;

    @NotNull(message = "El salario neto no puede ser nulo")
    @Min(value = 0, message = "El salario neto no puede ser menor a cero")
    @Column(precision = 10, scale = 2) // Para decimal(10,2) en la base de datos
    private BigDecimal salarioNeto;

    @NotNull(message = "La fecha de pago no puede ser nula")
    private LocalDate fechaPago;

    @NotNull(message = "El periodo de pago no puede ser nulo")
    @Column(length = 20) // varchar(20)
    private String periodoPago;

    @NotNull(message = "El estado de pago no puede ser nulo")
    @Enumerated(EnumType.STRING) // Guarda el nombre del enum como texto
    private EstadoPagoNomina estadoPago;

    @ManyToOne
    @JoinColumn(name = "idTrabajador", nullable = false) //que nullable sea falso significa que el campo idTrabajador no puede ser null
    private Trabajador trabajador;

    public Nomina() {
    }

    public Nomina(Integer idNomina, BigDecimal salarioBase, BigDecimal bonificacion, String conceptoBonificacion,
                  BigDecimal descuentoNomina, String conceptoDescuento, BigDecimal salarioNeto, LocalDate fechaPago,
                  String periodoPago, EstadoPagoNomina estadoPago, Trabajador trabajador) {
        this.idNomina = idNomina;
        this.salarioBase = salarioBase;
        this.bonificacion = bonificacion;
        this.conceptoBonificacion = conceptoBonificacion;
        this.descuentoNomina = descuentoNomina;
        this.conceptoDescuento = conceptoDescuento;
        this.salarioNeto = salarioNeto;
        this.fechaPago = fechaPago;
        this.periodoPago = periodoPago;
        this.estadoPago = estadoPago;
        this.trabajador = trabajador;
    }

    public Integer getIdNomina() {
        return idNomina;
    }

    public void setIdNomina(Integer idNomina) {
        this.idNomina = idNomina;
    }

    public BigDecimal getSalarioBase() {
        return salarioBase;
    }

    public void setSalarioBase(BigDecimal salarioBase) {
        this.salarioBase = salarioBase;
    }

    public BigDecimal getBonificacion() {
        return bonificacion;
    }

    public void setBonificacion(BigDecimal bonificacion) {
        this.bonificacion = bonificacion;
    }

    public String getConceptoBonificacion() {
        return conceptoBonificacion;
    }

    public void setConceptoBonificacion(String conceptoBonificacion) {
        this.conceptoBonificacion = conceptoBonificacion;
    }

    public BigDecimal getDescuentoNomina() {
        return descuentoNomina;
    }

    public void setDescuentoNomina(BigDecimal descuentoNomina) {
        this.descuentoNomina = descuentoNomina;
    }

    public String getConceptoDescuento() {
        return conceptoDescuento;
    }

    public void setConceptoDescuento(String conceptoDescuento) {
        this.conceptoDescuento = conceptoDescuento;
    }

    public BigDecimal getSalarioNeto() {
        return salarioNeto;
    }

    public void setSalarioNeto(BigDecimal salarioNeto) {
        this.salarioNeto = salarioNeto;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getPeriodoPago() {
        return periodoPago;
    }

    public void setPeriodoPago(String periodoPago) {
        this.periodoPago = periodoPago;
    }

    public EstadoPagoNomina getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(EstadoPagoNomina estadoPago) {
        this.estadoPago = estadoPago;
    }

    public Trabajador getTrabajador() {
        return trabajador;
    }

    public void setTrabajador(Trabajador trabajador) {
        this.trabajador = trabajador;
    }
}
