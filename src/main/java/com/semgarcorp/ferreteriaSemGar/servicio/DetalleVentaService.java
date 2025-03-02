package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.dto.DetalleVentaDTO;
import com.semgarcorp.ferreteriaSemGar.modelo.DetalleVenta;
import com.semgarcorp.ferreteriaSemGar.modelo.Producto;
import com.semgarcorp.ferreteriaSemGar.modelo.Venta;
import com.semgarcorp.ferreteriaSemGar.repositorio.DetalleVentaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DetalleVentaService {

    private final DetalleVentaRepository detalleVentaRepositorio;

    private ParametroService parametroService; // Inyectar ParametroService

    // Constructor para inyectar el repositorio
    public DetalleVentaService(DetalleVentaRepository detalleVentaRepositorio, ParametroService parametroService) {
        this.detalleVentaRepositorio = detalleVentaRepositorio;
        this.parametroService = parametroService;
    }

    // Listar todos los detalles de venta
    public List<DetalleVenta> listar() {
        return detalleVentaRepositorio.findAll();
    }

    // Obtener un detalle de venta por su ID
    public DetalleVenta obtenerPorId(Integer id) {
        return detalleVentaRepositorio.findById(id).orElse(null);
    }

    // Guardar un nuevo detalle de venta o actualizar uno existente
    public DetalleVenta guardar(DetalleVenta detalleVenta) {
        return detalleVentaRepositorio.save(detalleVenta);
    }

    // Actualizar un detalle de venta existente
    public DetalleVenta actualizar(DetalleVenta detalleVenta) {
        return detalleVentaRepositorio.save(detalleVenta);
    }

    // Eliminar un detalle de venta por su ID
    public void eliminar(Integer id) {
        detalleVentaRepositorio.deleteById(id);
    }

    // Metodo para calcular subtotal sin IGV, IGV aplicado y total
    public Map<String, BigDecimal> calcularValores(BigDecimal precioUnitario, BigDecimal cantidad, BigDecimal descuento) {
        // Obtener el valor del IGV usando ParametroService
        BigDecimal igv = parametroService.obtenerValorIGV();

        // Calcular subtotal sin IGV
        BigDecimal subtotalSinIGV = precioUnitario.multiply(cantidad).subtract(descuento);

        // Calcular IGV aplicado
        BigDecimal igvAplicado = subtotalSinIGV.multiply(igv).divide(new BigDecimal("100"));

        // Calcular total
        BigDecimal total = subtotalSinIGV.add(igvAplicado);

        // Devolver los valores en un Map
        Map<String, BigDecimal> valores = new HashMap<>();
        valores.put("subtotalSinIGV", subtotalSinIGV);
        valores.put("igvAplicado", igvAplicado);
        valores.put("total", total);

        return valores;
    }
/*
    public DetalleVenta guardarDetalleVenta(DetalleVentaDTO detalleVentaDTO) {
        // Obtener la Venta y el Producto desde sus repositorios
        Venta venta = ventaRepository.findById(detalleVentaDTO.getIdVenta())
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        Producto producto = productoRepository.findById(detalleVentaDTO.getIdProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Convertir DTO a entidad
        DetalleVenta detalleVenta = convertirAEntidad(detalleVentaDTO, venta, producto);

        // Calcular los valores
        Map<String, BigDecimal> valores = calcularValores(
                detalleVentaDTO.getPrecioUnitario(),
                detalleVentaDTO.getCantidad(),
                detalleVentaDTO.getDescuento()
        );

        // Asignar los valores calculados a DetalleVenta
        detalleVenta.setSubtotalSinIGV(valores.get("subtotalSinIGV"));
        detalleVenta.setIgvAplicado(valores.get("igvAplicado"));

        // Guardar el DetalleVenta
        detalleVenta = detalleVentaRepositorio.save(detalleVenta);

        // Actualizar el total de la Venta
        BigDecimal totalVenta = venta.getDetallesVenta().stream()
                .map(detalle -> detalle.getSubtotalSinIGV().add(detalle.getIgvAplicado()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        venta.setTotal(totalVenta);
        ventaRepository.save(venta);

        return detalleVenta;
    }
*/
}