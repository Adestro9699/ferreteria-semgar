package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.dto.DetalleVentaDTO;
import com.semgarcorp.ferreteriaSemGar.dto.VentaDTO;
import com.semgarcorp.ferreteriaSemGar.modelo.*;
import com.semgarcorp.ferreteriaSemGar.repositorio.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
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
        venta.setSerieComprobante(ventaDTO.getSerieComprobante() != null ? ventaDTO.getSerieComprobante() : ""); // Valor temporal
        venta.setNumeroComprobante(ventaDTO.getNumeroComprobante() != null ? ventaDTO.getNumeroComprobante() : ""); // Valor temporal
        venta.setFechaVenta(ventaDTO.getFechaVenta());
        venta.setEstadoVenta(ventaDTO.getEstadoVenta() != null ? ventaDTO.getEstadoVenta() : EstadoVenta.PENDIENTE);
        venta.setTotalVenta(ventaDTO.getTotalVenta());
        venta.setFechaModificacion(ventaDTO.getFechaModificacion());
        venta.setObservaciones(ventaDTO.getObservaciones());

        // Asignar idCaja solo si la venta no está en estado PENDIENTE
        if (ventaDTO.getEstadoVenta() != null && ventaDTO.getEstadoVenta() != EstadoVenta.PENDIENTE) {
            venta.setCaja(buscarEntidadPorId(cajaRepository, ventaDTO.getIdCaja(), "Caja"));
        } else {
            venta.setCaja(null); // No asignar caja si la venta está en estado PENDIENTE
        }

        // Cargar entidades relacionadas de forma más eficiente
        venta.setEmpresa(buscarEntidadPorId(empresaRepository, ventaDTO.getIdEmpresa(), "Empresa"));
        venta.setTipoComprobantePago(buscarEntidadPorId(tipoComprobanteRepository, ventaDTO.getIdTipoComprobantePago(), "TipoComprobantePago"));
        venta.setTrabajador(buscarEntidadPorId(trabajadorRepository, ventaDTO.getIdTrabajador(), "Trabajador"));
        venta.setCliente(buscarEntidadPorId(clienteRepository, ventaDTO.getIdCliente(), "Cliente"));
        venta.setTipoPago(buscarEntidadPorId(tipoPagoRepository, ventaDTO.getIdTipoPago(), "TipoPago"));

        // Convertir detallesVentaDTO a DetalleVenta (validando que la lista no sea null)
        List<DetalleVenta> detallesVenta = ventaDTO.getDetalles() == null ?
                new ArrayList<>() :
                ventaDTO.getDetalles().stream()
                        .map(detalleDTO -> {
                            DetalleVenta detalle = new DetalleVenta();
                            detalle.setProducto(buscarEntidadPorId(productoRepository, detalleDTO.getIdProducto(), "Producto"));
                            detalle.setCantidad(detalleDTO.getCantidad());
                            detalle.setPrecioUnitario(detalleDTO.getPrecioUnitario());
                            detalle.setDescuento(detalleDTO.getDescuento());
                            detalle.setSubtotalSinIGV(detalleDTO.getSubtotalSinIGV() != null ? detalleDTO.getSubtotalSinIGV() : BigDecimal.ZERO);
                            detalle.setIgvAplicado(detalleDTO.getIgvAplicado() != null ? detalleDTO.getIgvAplicado() : BigDecimal.ZERO);
                            detalle.setVenta(venta); // Establecer la relación con la venta
                            return detalle;
                        })
                        .collect(Collectors.toList());

        venta.setDetalles(detallesVenta);
        return venta;
    }

    // Metodo auxiliar para buscar entidades y lanzar excepción si no se encuentran
    private <T> T buscarEntidadPorId(JpaRepository<T, Integer> repository, Integer id, String nombreEntidad) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(nombreEntidad + " con ID " + id + " no encontrada"));
    }

    @Transactional
    public VentaDTO completarVenta(Integer idVenta) {
        // 1. Buscar la venta por ID
        Venta venta = ventaRepository.findById(idVenta)
                .orElseThrow(() -> new EntityNotFoundException("Venta con ID " + idVenta + " no encontrada"));

        // 2. Verificar que la venta esté en estado "Pendiente"
        if (venta.getEstadoVenta() != EstadoVenta.PENDIENTE) {
            throw new IllegalStateException("La venta no está en estado PENDIENTE");
        }

        // 3. Generar serie y número (puedes implementar tu propia lógica aquí)
        venta.setSerieComprobante(generarSerie());
        venta.setNumeroComprobante(generarNumero());

        // 4. Cambiar el estado a "Completada"
        venta.setEstadoVenta(EstadoVenta.COMPLETADA);

        // 5. Guardar la venta actualizada
        venta = ventaRepository.save(venta);

        // 6. Convertir la entidad Venta actualizada a VentaDTO
        return convertirAVentaDTO(venta);
    }

    private String generarSerie() {
        // Lógica para generar la serie (por ejemplo, "F001")
        return "F001";
    }

    private String generarNumero() {
        // Lógica para generar el número (por ejemplo, "0001")
        return "0001";
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
        // 1. Validar que la venta esté en estado "Pendiente"
        if (ventaDTO.getEstadoVenta() != null && ventaDTO.getEstadoVenta() != EstadoVenta.PENDIENTE) {
            throw new IllegalArgumentException("Una nueva venta debe estar en estado PENDIENTE");
        }

        // 2. Validar que no se envíen serie y número
        if (ventaDTO.getSerieComprobante() != null || ventaDTO.getNumeroComprobante() != null) {
            throw new IllegalArgumentException("No se pueden enviar serie y número para una venta en estado PENDIENTE");
        }

        // 3. Validar que no se envíe idCaja mientras la venta esté en estado PENDIENTE
        if (ventaDTO.getIdCaja() != null) {
            throw new IllegalArgumentException("No se puede enviar idCaja para una venta en estado PENDIENTE");
        }

        // 4. Convertir VentaDTO a Venta
        Venta venta = convertirAVenta(ventaDTO);

        // 5. Guardar la venta en la base de datos
        venta = ventaRepository.save(venta);

        // 6. Convertir la entidad Venta guardada de vuelta a VentaDTO
        return convertirAVentaDTO(venta);
    }
}