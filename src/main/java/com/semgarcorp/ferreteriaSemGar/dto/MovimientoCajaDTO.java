package com.semgarcorp.ferreteriaSemGar.dto;

import com.semgarcorp.ferreteriaSemGar.modelo.TipoMovimiento;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class MovimientoCajaDTO {

    @NotNull(message = "El monto no puede ser nulo")
    @Positive(message = "El monto debe ser un valor positivo")
    private BigDecimal monto;

    private TipoMovimiento tipo;

    private String observaciones;

    // Getters y setters
    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public TipoMovimiento getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimiento tipo) {
        this.tipo = tipo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}