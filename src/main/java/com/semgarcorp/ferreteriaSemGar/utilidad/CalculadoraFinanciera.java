package com.semgarcorp.ferreteriaSemGar.utilidad;

import com.semgarcorp.ferreteriaSemGar.modelo.Impuesto;
import com.semgarcorp.ferreteriaSemGar.servicio.ImpuestoService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CalculadoraFinanciera {

    private final ImpuestoService impuestoService;

    public CalculadoraFinanciera(ImpuestoService impuestoService) {
        this.impuestoService = impuestoService;
    }

    /**
     * Calcula el descuento basado en un porcentaje del subtotal.
     */
    public static BigDecimal calcularDescuentoPorcentaje(BigDecimal subtotal, BigDecimal porcentajeDescuento) {
        // Validar que las entradas no sean nulas
        if (subtotal == null || porcentajeDescuento == null) {
            throw new IllegalArgumentException("El subtotal y el porcentaje de descuento deben ser valores válidos.");
        }
        if (porcentajeDescuento.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El porcentaje de descuento no puede ser negativo.");
        }

        // Calcular el descuento
        return subtotal.multiply(porcentajeDescuento.divide(BigDecimal.valueOf(100)))
                .setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Calcula el subtotal sin impuestos.
     */
    public BigDecimal calcularSubtotalSinImpuestos(BigDecimal cantidad, BigDecimal precioUnitario, BigDecimal descuento) {
        // Validar las entradas usando el metodo centralizado
        ValidadorFinanciero.validarEntradas(cantidad, precioUnitario, descuento);

        // Calcular el subtotal sin impuestos
        BigDecimal subtotal = cantidad.multiply(precioUnitario);
        return subtotal.subtract(descuento != null ? descuento : BigDecimal.ZERO)
                .setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Calcula el valor total de los impuestos aplicados.
     */
    public BigDecimal calcularImpuestos(BigDecimal subtotalSinImpuestos) {
        if (subtotalSinImpuestos == null) {
            return BigDecimal.ZERO;
        }

        List<Impuesto> impuestosActivos = impuestoService.obtenerImpuestosActivos();
        BigDecimal totalImpuestos = BigDecimal.ZERO;

        for (Impuesto impuesto : impuestosActivos) {
            BigDecimal valorImpuesto = subtotalSinImpuestos.multiply(impuesto.getPorcentaje().divide(BigDecimal.valueOf(100)))
                    .setScale(2, BigDecimal.ROUND_HALF_UP);
            totalImpuestos = totalImpuestos.add(valorImpuesto);
        }

        return totalImpuestos;
    }

    /**
     * Calcula el total final (subtotal + impuestos).
     */
    public BigDecimal calcularTotalFinal(BigDecimal subtotalSinImpuestos, BigDecimal impuestos) {
        if (subtotalSinImpuestos == null || impuestos == null) {
            return BigDecimal.ZERO;
        }
        return subtotalSinImpuestos.add(impuestos).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Calcula el precio sin impuestos (inverso del total con impuestos).
     */
    public BigDecimal calcularPrecioSinImpuestos(BigDecimal totalConImpuestos, BigDecimal porcentajeImpuesto) {
        if (totalConImpuestos == null || porcentajeImpuesto == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal factorImpuesto = BigDecimal.ONE.add(porcentajeImpuesto.divide(BigDecimal.valueOf(100)));
        return totalConImpuestos.divide(factorImpuesto, 2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Calcula el valor del impuesto aplicado (basado en el precio sin impuestos).
     */
    public BigDecimal calcularImpuestoAplicado(BigDecimal precioSinImpuestos, BigDecimal porcentajeImpuesto) {
        if (precioSinImpuestos == null || porcentajeImpuesto == null) {
            return BigDecimal.ZERO;
        }
        return precioSinImpuestos.multiply(porcentajeImpuesto.divide(BigDecimal.valueOf(100)))
                .setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}