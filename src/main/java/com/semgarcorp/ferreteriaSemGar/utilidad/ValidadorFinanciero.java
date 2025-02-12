package com.semgarcorp.ferreteriaSemGar.utilidad;

import java.math.BigDecimal;

public class ValidadorFinanciero {

    public static void validarEntradas(BigDecimal cantidad, BigDecimal precioUnitario, BigDecimal descuento) {
        if (cantidad == null || cantidad.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
        if (precioUnitario == null || precioUnitario.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio unitario debe ser mayor que cero.");
        }
        if (descuento != null && descuento.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El descuento no puede ser negativo.");
        }
    }
}