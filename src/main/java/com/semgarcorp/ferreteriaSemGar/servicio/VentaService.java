package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.dto.DetalleVentaDTO;
import com.semgarcorp.ferreteriaSemGar.dto.VentaDTO;
import com.semgarcorp.ferreteriaSemGar.modelo.*;
import com.semgarcorp.ferreteriaSemGar.repositorio.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VentaService {

    private final VentaRepository ventaRepositorio;

    private final ParametroService parametroService;

    public VentaService(VentaRepository ventaRepositorio, ParametroService parametroService) {
        this.ventaRepositorio = ventaRepositorio;
        this.parametroService = parametroService;
    }

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private TipoPagoRepository tipoPagoRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private TipoComprobantePagoRepository tipoComprobanteRepository;

    @Autowired
    private TrabajadorRepository trabajadorRepository;

    @Autowired
    private CajaRepository cajaRepository;
/*
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
            detalle.setSubtotal(subtotalDetalle);

            BigDecimal igvDetalle = subtotalDetalle.multiply(porcentajeIGV);
            detalle.setImpuesto(igvDetalle);
        }
    }
*/
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

    public Venta convertirAVenta(VentaDTO ventaDTO) {
        Venta venta = new Venta();

        // Asignar campos básicos de Venta
        venta.setSerieComprobante(ventaDTO.getSerieComprobante());
        venta.setNumeroComprobante(ventaDTO.getNumeroComprobante());
        venta.setFechaVenta(ventaDTO.getFechaVenta());
        venta.setEstadoVenta(ventaDTO.getEstadoVenta() != null ? ventaDTO.getEstadoVenta() : EstadoVenta.PENDIENTE);
        venta.setTotalVenta(ventaDTO.getTotalVenta());
        venta.setFechaModificacion(ventaDTO.getFechaModificacion());
        venta.setObservaciones(ventaDTO.getObservaciones());

        // Asignar entidades relacionadas (usando repositorios)
        venta.setCaja(cajaRepository.findById(ventaDTO.getIdCaja())
                .orElseThrow(() -> new EntityNotFoundException("Caja no encontrada")));
        venta.setEmpresa(empresaRepository.findById(ventaDTO.getIdEmpresa())
                .orElseThrow(() -> new EntityNotFoundException("Empresa no encontrada")));
        venta.setTipoComprobantePago(tipoComprobanteRepository.findById(ventaDTO.getIdTipoComprobantePago())
                .orElseThrow(() -> new EntityNotFoundException("TipoComprobantePago no encontrado")));
        venta.setTrabajador(trabajadorRepository.findById(ventaDTO.getIdTrabajador())
                .orElseThrow(() -> new EntityNotFoundException("Trabajador no encontrado")));
        venta.setCliente(clienteRepository.findById(ventaDTO.getIdCliente())
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado")));
        venta.setTipoPago(tipoPagoRepository.findById(ventaDTO.getIdTipoPago())
                .orElseThrow(() -> new EntityNotFoundException("TipoPago no encontrado")));

        // Convertir detallesVentaDTO a DetalleVenta
        List<DetalleVenta> detallesVenta = ventaDTO.getDetalles() != null ?
                ventaDTO.getDetalles().stream()
                        .map(detalleDTO -> {
                            DetalleVenta detalle = new DetalleVenta();
                            detalle.setProducto(productoRepository.findById(detalleDTO.getIdProducto())
                                    .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado")));
                            detalle.setCantidad(detalleDTO.getCantidad());
                            detalle.setPrecioUnitario(detalleDTO.getPrecioUnitario());
                            detalle.setDescuento(detalleDTO.getDescuento());
                            detalle.setSubtotalSinIGV(detalleDTO.getSubtotalSinIGV() != null ? detalleDTO.getSubtotalSinIGV() : BigDecimal.ZERO);
                            detalle.setIgvAplicado(detalleDTO.getIgvAplicado() != null ? detalleDTO.getIgvAplicado() : BigDecimal.ZERO);
                            detalle.setVenta(venta); // Establecer la relación con la venta
                            return detalle;
                        })
                        .collect(Collectors.toList()) :
                new ArrayList<>(); // Si detalles es null, se asigna una lista vacía

        venta.setDetalles(detallesVenta);
        return venta;
    }

    public VentaDTO convertirAVentaDTO(Venta venta) {
        VentaDTO ventaDTO = new VentaDTO();

        // Asignar campos básicos de Venta
        ventaDTO.setIdVenta(venta.getIdVenta());
        ventaDTO.setSerieComprobante(venta.getSerieComprobante());
        ventaDTO.setNumeroComprobante(venta.getNumeroComprobante());
        ventaDTO.setFechaVenta(venta.getFechaVenta());
        ventaDTO.setEstadoVenta(venta.getEstadoVenta()); // Usa la enumeración común
        ventaDTO.setTotalVenta(venta.getTotalVenta());
        ventaDTO.setFechaModificacion(venta.getFechaModificacion());
        ventaDTO.setObservaciones(venta.getObservaciones());

        // Asignar IDs de entidades relacionadas (con validación de null)
        ventaDTO.setIdCaja(venta.getCaja() != null ? venta.getCaja().getIdCaja() : null);
        ventaDTO.setIdEmpresa(venta.getEmpresa() != null ? venta.getEmpresa().getIdEmpresa() : null);
        ventaDTO.setIdTipoComprobantePago(venta.getTipoComprobantePago() != null ? venta.getTipoComprobantePago().getIdTipoComprobantePago() : null);
        ventaDTO.setIdTrabajador(venta.getTrabajador() != null ? venta.getTrabajador().getIdTrabajador() : null);
        ventaDTO.setIdCliente(venta.getCliente() != null ? venta.getCliente().getIdCliente() : null);
        ventaDTO.setIdTipoPago(venta.getTipoPago() != null ? venta.getTipoPago().getIdTipoPago() : null);

        // Convertir detallesVenta a DetalleVentaDTO (con validación de null)
        List<DetalleVentaDTO> detallesVentaDTO = venta.getDetalles() != null ?
                venta.getDetalles().stream()
                        .map(detalle -> {
                            DetalleVentaDTO detalleDTO = new DetalleVentaDTO();
                            detalleDTO.setIdDetalleVenta(detalle.getIdDetalleVenta());
                            detalleDTO.setIdVenta(detalle.getVenta().getIdVenta());
                            detalleDTO.setIdProducto(detalle.getProducto().getIdProducto());
                            detalleDTO.setCantidad(detalle.getCantidad());
                            detalleDTO.setPrecioUnitario(detalle.getPrecioUnitario());
                            detalleDTO.setDescuento(detalle.getDescuento());
                            detalleDTO.setSubtotalSinIGV(detalle.getSubtotalSinIGV());
                            detalleDTO.setIgvAplicado(detalle.getIgvAplicado());
                            return detalleDTO;
                        })
                        .collect(Collectors.toList()) :
                new ArrayList<>(); // Si detalles es null, se asigna una lista vacía

        ventaDTO.setDetalles(detallesVentaDTO);
        return ventaDTO;
    }

    @Transactional
    public VentaDTO guardarVenta(VentaDTO ventaDTO) {
        // 1. Convertir VentaDTO a Venta
        Venta venta = convertirAVenta(ventaDTO);

        // 2. Guardar la venta en la base de datos
        venta = ventaRepository.save(venta);

        // 3. Convertir la entidad Venta guardada de vuelta a VentaDTO
        return convertirAVentaDTO(venta);
    }
}