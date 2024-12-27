package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import java.time.LocalDate;

@Entity
public class Nomina {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idNomina;

    @NotNull(message = "El salario base no puede ser nulo")
    @Min(value = 0, message = "El salario base no puede ser menor a cero")
    private Double salarioBase;

    @Min(value = 0, message = "Las bonificaciones no pueden ser menores a cero")
    private Double bonificacion;

    private String conceptoBonificacion;

    @Min(value = 0, message = "El descuento no puede ser menor a cero")
    private Double descuentoNomina;

    private String conceptoDescuento;

    @NotNull(message = "El salario neto no puede ser nulo")
    @Min(value = 0, message = "El salario neto no puede ser menor a cero")
    private Double salarioNeto;

    @NotNull(message = "La fecha de pago no puede ser nula")
    private LocalDate fechaPago;

    @NotNull(message = "El periodo de pago no puede ser nulo")
    private String periodoPago;

    @NotNull(message = "El estado de pago no puede ser nulo")
    private String estadoPago;

    @ManyToOne
    @JoinColumn(name = "idTrabajador", nullable = false)
    private Trabajador trabajador;

    public Nomina() {
    }

    public Nomina(Long idNomina, Double salarioBase, Double bonificacion, String conceptoBonificacion,
                  Double descuentoNomina, String conceptoDescuento, Double salarioNeto, LocalDate fechaPago,
                  String periodoPago, String estadoPago, Trabajador trabajador) {
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

    public Long getIdNomina() {
        return idNomina;
    }

    public void setIdNomina(Long idNomina) {
        this.idNomina = idNomina;
    }

    public Double getSalarioBase() {
        return salarioBase;
    }

    public void setSalarioBase(Double salarioBase) {
        this.salarioBase = salarioBase;
    }

    public Double getBonificacion() {
        return bonificacion;
    }

    public void setBonificacion(Double bonificacion) {
        this.bonificacion = bonificacion;
    }

    public String getConceptoBonificacion() {
        return conceptoBonificacion;
    }

    public void setConceptoBonificacion(String conceptoBonificacion) {
        this.conceptoBonificacion = conceptoBonificacion;
    }

    public Double getDescuentoNomina() {
        return descuentoNomina;
    }

    public void setDescuentoNomina(Double descuentoNomina) {
        this.descuentoNomina = descuentoNomina;
    }

    public String getConceptoDescuento() {
        return conceptoDescuento;
    }

    public void setConceptoDescuento(String conceptoDescuento) {
        this.conceptoDescuento = conceptoDescuento;
    }

    public Double getSalarioNeto() {
        return salarioNeto;
    }

    public void setSalarioNeto(Double salarioNeto) {
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

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }

    public Trabajador getTrabajador() {
        return trabajador;
    }

    public void setTrabajador(Trabajador trabajador) {
        this.trabajador = trabajador;
    }
}
