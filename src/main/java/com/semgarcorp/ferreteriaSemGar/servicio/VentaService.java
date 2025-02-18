package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.*;
import com.semgarcorp.ferreteriaSemGar.repositorio.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class VentaService {

    private final VentaRepository ventaRepositorio;

    private final ParametroService parametroService;

    public VentaService(VentaRepository ventaRepositorio, ParametroService parametroService) {
        this.ventaRepositorio = ventaRepositorio;
        this.parametroService = parametroService;
    }

    public void calcularValoresVenta(Venta venta) {
        // Obtener el valor del IGV
        BigDecimal porcentajeIGV = parametroService.obtenerValorPorClave("IGV");

        // Calcular el subtotal (suma de subtotales de los detalles)
        BigDecimal subtotal = venta.getDetalles().stream()
                .map(detalle -> detalle.getPrecioUnitario().multiply(detalle.getCantidad()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Aplicar el descuento (si existe)
        BigDecimal descuentoTotal = venta.getDetalles().stream()
                .map(DetalleVenta::getDescuento)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal subtotalConDescuento = subtotal.subtract(descuentoTotal);

        // Calcular el IGV (18% del subtotal después del descuento)
        BigDecimal igv = subtotalConDescuento.multiply(porcentajeIGV);

        // Calcular el total (subtotal con descuento + IGV)
        BigDecimal total = subtotalConDescuento.add(igv);

        // Asignar los valores a la venta
        venta.setTotalVenta(total);

        // Asignar el subtotal y el IGV a cada detalle
        for (DetalleVenta detalle : venta.getDetalles()) {
            BigDecimal subtotalDetalle = detalle.getPrecioUnitario().multiply(detalle.getCantidad());
            detalle.setSubtotalSinImpuestos(subtotalDetalle);

            BigDecimal igvDetalle = subtotalDetalle.multiply(porcentajeIGV);
            detalle.setImpuesto(igvDetalle);
        }
    }

    public List<Venta> listar() {
        return ventaRepositorio.findAll();
    }

    public Venta obtenerPorId(Integer id) {
        return ventaRepositorio.findById(id).orElse(null);
    }

    public Venta guardar(Venta venta) {
        return ventaRepositorio.save(venta);
    }

    public Venta actualizar(Venta venta) {
        return ventaRepositorio.save(venta);
    }

    public void eliminar(Integer id) {
        ventaRepositorio.deleteById(id);
    }
}