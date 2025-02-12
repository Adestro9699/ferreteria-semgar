package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.dto.DetalleVentaDTO;
import com.semgarcorp.ferreteriaSemGar.dto.VentaDTO;
import com.semgarcorp.ferreteriaSemGar.modelo.*;
import com.semgarcorp.ferreteriaSemGar.repositorio.*;
import com.semgarcorp.ferreteriaSemGar.utilidad.CalculadoraFinanciera;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VentaService {

    private final VentaRepository ventaRepositorio;

    private final ProductoRepository productoRepositorio;

    private final TipoComprobantePagoRepository tipoComprobantePagoRepositorio;

    private final TipoPagoRepository tipoPagoRepositorio;

    private final ProductoService productoService;

    private final CalculadoraFinanciera calculadoraFinanciera;

    private final ClienteRepository clienteRepositorio;

    public VentaService(VentaRepository ventaRepositorio, ProductoRepository productoRepositorio,
                        TipoComprobantePagoRepository tipoComprobantePagoRepositorio, TipoPagoRepository tipoPagoRepositorio,
                        ProductoService productoService, CalculadoraFinanciera calculadoraFinanciera, ClienteRepository clienteRepositorio) {
        this.ventaRepositorio = ventaRepositorio;
        this.productoRepositorio = productoRepositorio;
        this.tipoComprobantePagoRepositorio = tipoComprobantePagoRepositorio;
        this.tipoPagoRepositorio = tipoPagoRepositorio;
        this.productoService = productoService;
        this.calculadoraFinanciera = calculadoraFinanciera;
        this.clienteRepositorio = clienteRepositorio;
    }

    @Transactional
    public VentaDTO registrarVenta(VentaDTO ventaDTO) {
        // 1. Validar los datos de la venta
        if (ventaDTO == null) {
            throw new IllegalArgumentException("La venta no puede ser nula.");
        }
        if (ventaDTO.getDetalles() == null || ventaDTO.getDetalles().isEmpty()) {
            throw new IllegalArgumentException("La venta debe tener al menos un detalle.");
        }

        // 2. Convertir el DTO a una entidad Venta
        Venta venta = convertirAVenta(ventaDTO);

        // 3. Validar el stock de los productos antes de registrar la venta
        for (DetalleVenta detalle : venta.getDetalles()) {
            Producto producto = detalle.getProducto();
            BigDecimal cantidadVendida = detalle.getCantidad(); // Ya es BigDecimal
            BigDecimal stockDisponible = producto.getStock(); // Ya es BigDecimal

            if (stockDisponible.compareTo(cantidadVendida) < 0) { // Comparar stock con cantidad vendida
                throw new IllegalArgumentException("Stock insuficiente para el producto: " + producto.getNombreProducto());
            }
        }

        // 4. Actualizar el stock de los productos
        for (DetalleVenta detalle : venta.getDetalles()) {
            Producto producto = detalle.getProducto();
            BigDecimal cantidadVendida = detalle.getCantidad(); // Ya es BigDecimal
            BigDecimal stockDisponible = producto.getStock(); // Ya es BigDecimal

            BigDecimal nuevoStock = stockDisponible.subtract(cantidadVendida); // Restar el stock

            producto.setStock(nuevoStock); // Guardar directamente el BigDecimal
            productoRepositorio.save(producto); // Actualizar en la BD
        }

        // 5. Guardar la venta en la base de datos
        venta = ventaRepositorio.save(venta);

        // 6. Convertir la entidad Venta guardada a un DTO para devolverla
        return convertirAVentaDTO(venta);
    }

    public BigDecimal obtenerPrecioProducto(Integer idProducto) {
        Producto producto = productoService.obtenerPorId(idProducto);
        return producto.getPrecio();
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

    public VentaDTO convertirAVentaDTO(Venta venta) {
        VentaDTO ventaDTO = new VentaDTO();
        ventaDTO.setSerieComprobante(venta.getSerieComprobante());
        ventaDTO.setNumeroComprobante(venta.getNumeroComprobante());
        ventaDTO.setTipoComprobante(venta.getTipoComprobantePago() != null ? venta.getTipoComprobantePago().getNombre() : null);
        ventaDTO.setTipoPago(venta.getTipoPago() != null ? venta.getTipoPago().getNombre() : null);
        ventaDTO.setFechaVenta(venta.getFechaVenta());

        // Manejo seguro de estado de venta
        ventaDTO.setEstadoVenta(venta.getEstadoVenta() != null ? venta.getEstadoVenta().name() : null);

        // Manejo seguro de cliente
        Cliente cliente = venta.getCliente();
        if (cliente != null) {
            ventaDTO.setNombresCliente(cliente.getNombresCliente());
            ventaDTO.setApellidosCliente(cliente.getApellidosCliente());
            ventaDTO.setDireccionCliente(cliente.getDireccionCliente());
            ventaDTO.setTipoDocumentoCliente(cliente.getTipoDocumento().getNombre());
            ventaDTO.setNumeroDocumentoCliente(
                    cliente.getDniCliente() != null ? cliente.getDniCliente() : cliente.getRuc()
            );
        }

        // Convertir detalles de venta
        List<DetalleVentaDTO> detallesDTO = venta.getDetalles() != null
                ? venta.getDetalles().stream()
                .map(this::convertirADetalleVentaDTO)
                .collect(Collectors.toList())
                : Collections.emptyList();
        ventaDTO.setDetalles(detallesDTO);

        // Calcular subtotal sin impuestos sumando los subtotales de los detalles
        BigDecimal subtotalSinImpuestos = detallesDTO.stream()
                .map(DetalleVentaDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calcular total de impuestos usando la calculadora financiera
        BigDecimal totalImpuestos = calculadoraFinanciera.calcularImpuestos(subtotalSinImpuestos);
        ventaDTO.setImpuestoTotal(totalImpuestos);

        // Calcular total de la venta con los impuestos
        BigDecimal totalVenta = calculadoraFinanciera.calcularTotalFinal(subtotalSinImpuestos, totalImpuestos);
        ventaDTO.setTotalVenta(totalVenta);

        return ventaDTO;
    }

    public Venta convertirAVenta(VentaDTO ventaDTO) {
        Venta venta = new Venta();
        venta.setSerieComprobante(ventaDTO.getSerieComprobante());
        venta.setNumeroComprobante(ventaDTO.getNumeroComprobante());

        // Obtener Tipo de Comprobante
        TipoComprobantePago tipoComprobante = tipoComprobantePagoRepositorio.findByNombre(ventaDTO.getTipoComprobante())
                .orElseThrow(() -> new RuntimeException("Tipo de comprobante no encontrado"));
        venta.setTipoComprobantePago(tipoComprobante);

        // Obtener Tipo de Pago
        TipoPago tipoPago = tipoPagoRepositorio.findByNombreContainingIgnoreCase(ventaDTO.getTipoPago())
                .orElseThrow(() -> new RuntimeException("Tipo de pago no encontrado"));
        venta.setTipoPago(tipoPago);

        venta.setFechaVenta(ventaDTO.getFechaVenta());

        // Normalizar estado de venta (si es un ENUM)
        venta.setEstadoVenta(Venta.EstadoVenta.valueOf(ventaDTO.getEstadoVenta().toUpperCase().replace(" ", "_")));

        // Obtener Cliente con el nuevo campo numeroDocumentoCliente
        Cliente cliente = clienteRepositorio.findByDniClienteOrRuc(ventaDTO.getNumeroDocumentoCliente(), ventaDTO.getNumeroDocumentoCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        venta.setCliente(cliente);

        // Convertir detalles de venta y calcular totales
        List<DetalleVenta> detalles = ventaDTO.getDetalles().stream()
                .map(this::convertirADetalleVenta)
                .collect(Collectors.toList());
        venta.setDetalles(detalles);

        // Calcular subtotal sin impuestos
        BigDecimal subtotalSinImpuestos = detalles.stream()
                .map(DetalleVenta::getSubtotalSinImpuestos)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calcular total de impuestos
        BigDecimal totalImpuestos = calculadoraFinanciera.calcularImpuestos(subtotalSinImpuestos);

        // Calcular total de la venta
        BigDecimal totalVenta = calculadoraFinanciera.calcularTotalFinal(subtotalSinImpuestos, totalImpuestos);
        venta.setTotalVenta(totalVenta);

        return venta;
    }

    public DetalleVentaDTO convertirADetalleVentaDTO(DetalleVenta detalle) {
        DetalleVentaDTO detalleDTO = new DetalleVentaDTO();
        detalleDTO.setIdProducto(detalle.getProducto().getIdProducto());
        detalleDTO.setNombreProducto(detalle.getProducto().getNombreProducto());
        detalleDTO.setUnidadMedida(detalle.getProducto().getUnidadMedida().getAbreviatura());
        detalleDTO.setPrecioUnitario(detalle.getPrecioUnitario());
        detalleDTO.setCantidad(detalle.getCantidad());
        detalleDTO.setDescuento(detalle.getDescuento());

        // Calcular subtotal sin impuestos
        BigDecimal subtotalSinImpuestos = calculadoraFinanciera.calcularSubtotalSinImpuestos(
                detalle.getCantidad(), detalle.getPrecioUnitario(), detalle.getDescuento());

        detalleDTO.setSubtotal(subtotalSinImpuestos);

        return detalleDTO;
    }

    public DetalleVenta convertirADetalleVenta(DetalleVentaDTO detalleDTO) {
        DetalleVenta detalle = new DetalleVenta();
        detalle.setProducto(productoRepositorio.findById(detalleDTO.getIdProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado")));
        detalle.setPrecioUnitario(detalleDTO.getPrecioUnitario());
        detalle.setCantidad(detalleDTO.getCantidad());
        detalle.setDescuento(detalleDTO.getDescuento());

        // Calcular el subtotal sin impuestos usando la CalculadoraFinanciera
        BigDecimal subtotalSinImpuestos = calculadoraFinanciera.calcularSubtotalSinImpuestos(
                detalleDTO.getCantidad(), detalleDTO.getPrecioUnitario(), detalleDTO.getDescuento());

        detalle.setSubtotalSinImpuestos(subtotalSinImpuestos);

        return detalle;
    }
}