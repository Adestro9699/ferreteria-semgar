package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.dto.DetalleVentaDTO;
import com.semgarcorp.ferreteriaSemGar.dto.VentaDTO;
import com.semgarcorp.ferreteriaSemGar.modelo.*;
import com.semgarcorp.ferreteriaSemGar.repositorio.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VentaService {

    private final VentaRepository ventaRepositorio;

    private final ParametroService parametroService;

    private final VentaRepository ventaRepository;

    private final ProductoRepository productoRepository;

    private final TipoPagoRepository tipoPagoRepository;

    private final EmpresaRepository empresaRepository;

    private final ClienteRepository clienteRepository;

    private final TipoComprobantePagoRepository tipoComprobanteRepository;

    private final TrabajadorRepository trabajadorRepository;

    private final CajaRepository cajaRepository;

    private final ProductoService productoService;

    private final CajaService cajaService;

    public VentaService(VentaRepository ventaRepositorio, ParametroService parametroService,
                        VentaRepository ventaRepository, ProductoRepository productoRepository,
                        TipoPagoRepository tipoPagoRepository, EmpresaRepository empresaRepository,
                        ClienteRepository clienteRepository, TipoComprobantePagoRepository tipoComprobanteRepository,
                        TrabajadorRepository trabajadorRepository, CajaRepository cajaRepository,
                        ProductoService productoService, CajaService cajaService) {
        this.ventaRepositorio = ventaRepositorio;
        this.parametroService = parametroService;
        this.ventaRepository = ventaRepository;
        this.productoRepository = productoRepository;
        this.tipoPagoRepository = tipoPagoRepository;
        this.empresaRepository = empresaRepository;
        this.clienteRepository = clienteRepository;
        this.tipoComprobanteRepository = tipoComprobanteRepository;
        this.trabajadorRepository = trabajadorRepository;
        this.cajaRepository = cajaRepository;
        this.productoService = productoService;
        this.cajaService = cajaService;
    }

    public List<VentaDTO> listar() {
        List<Venta> ventas = ventaRepositorio.findAll(); // Obtener las entidades
        return ventas.stream()
                .map(this::convertirAVentaDTO) // Convertir cada entidad a DTO
                .collect(Collectors.toList());
    }

    public VentaDTO obtenerPorId(Integer id) {
        Venta venta = ventaRepositorio.findById(id).orElse(null); // Obtener la entidad
        if (venta != null) {
            return convertirAVentaDTO(venta); // Convertir la entidad a DTO
        }
        return null; // Si no se encuentra la venta, devolver null
    }

    public Venta guardar(Venta venta) {
        return ventaRepositorio.save(venta);
    }

    public VentaDTO actualizar(Integer id, VentaDTO ventaDTO) {
        Venta ventaExistente = ventaRepositorio.findById(id).orElse(null);
        if (ventaExistente != null) {
            Venta ventaActualizada = convertirAVenta(ventaDTO);
            ventaActualizada.setIdVenta(id);
            Venta ventaGuardada = ventaRepositorio.save(ventaActualizada);
            return convertirAVentaDTO(ventaGuardada);
        }
        return null;
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
            if (ventaDTO.getIdCaja() == null) {
                throw new IllegalArgumentException("Para una venta no PENDIENTE, el idCaja es obligatorio");
            }
            venta.setCaja(buscarEntidadPorId(cajaRepository, ventaDTO.getIdCaja(), "Caja"));
        } else {
            venta.setCaja(null); // Establecer caja como null si la venta está en estado PENDIENTE o idCaja es null
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
    public VentaDTO completarVenta(Integer idVenta, Integer idCaja) {
        // 1. Buscar la venta por ID
        Venta venta = ventaRepository.findById(idVenta)
                .orElseThrow(() -> new EntityNotFoundException("Venta con ID " + idVenta + " no encontrada"));

        // 2. Verificar que la venta esté en estado "Pendiente"
        if (venta.getEstadoVenta() != EstadoVenta.PENDIENTE) {
            throw new IllegalStateException("La venta no está en estado PENDIENTE");
        }

        // 3. Recorrer los detalles de la venta y reducir el stock de los productos
        for (DetalleVenta detalle : venta.getDetalles()) {
            productoService.reducirStock(detalle.getProducto().getIdProducto(), detalle.getCantidad());
        }

        // 4. Generar serie y número (puedes implementar tu propia lógica aquí)
        venta.setSerieComprobante(generarSerie());
        venta.setNumeroComprobante(generarNumero());

        // 5. Asignar el idCaja a la venta
        Caja caja = cajaRepository.findById(idCaja)
                .orElseThrow(() -> new EntityNotFoundException("Caja con ID " + idCaja + " no encontrada"));
        venta.setCaja(caja);

        // 6. Cambiar el estado a "Completada"
        venta.setEstadoVenta(EstadoVenta.COMPLETADA);

        // 7. Guardar la venta actualizada
        venta = ventaRepository.save(venta);

        // 8. Si la venta es en EFECTIVO y está COMPLETADA, registrar el total como entrada en la caja
        if (venta.getTipoPago().getNombre().equalsIgnoreCase("EFECTIVO") && venta.getEstadoVenta() == EstadoVenta.COMPLETADA) {
            cajaService.registrarEntradaPorVenta(venta.getCaja().getIdCaja(), venta.getTotalVenta());
        }

        // 9. Convertir la entidad Venta actualizada a VentaDTO
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

    private BigDecimal calcularSubtotal(BigDecimal precioUnitario, BigDecimal cantidad, BigDecimal descuentoPorcentual) {
        if (precioUnitario == null || cantidad == null || descuentoPorcentual == null) {
            throw new IllegalArgumentException("El precio unitario, la cantidad y el descuento no pueden ser nulos");
        }

        // Calcular el subtotal sin descuento
        BigDecimal subtotalSinDescuento = precioUnitario.multiply(cantidad);

        // Convertir el descuento porcentual a decimal (por ejemplo, 5 -> 0.05)
        BigDecimal descuentoDecimal = descuentoPorcentual.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        // Calcular el monto del descuento
        BigDecimal montoDescuento = subtotalSinDescuento.multiply(descuentoDecimal);

        // Calcular el subtotal con descuento
        return subtotalSinDescuento.subtract(montoDescuento);
    }

    private BigDecimal calcularSubtotalSinIGV(BigDecimal subtotal) {
        if (subtotal == null || subtotal.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El subtotal no puede ser nulo o negativo");
        }

        // Obtener el valor del IGV
        BigDecimal tasaIGV = parametroService.obtenerValorIGV();

        // Calcular el subtotal sin IGV
        return subtotal.divide(BigDecimal.ONE.add(tasaIGV), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal calcularIgvAplicado(BigDecimal subtotal, BigDecimal subtotalSinIGV) {
        if (subtotal == null || subtotalSinIGV == null) {
            throw new IllegalArgumentException("El subtotal y el subtotal sin IGV no pueden ser nulos");
        }
        return subtotal.subtract(subtotalSinIGV);
    }

    private BigDecimal calcularTotalVenta(List<DetalleVenta> detallesVenta) {
        return detallesVenta.stream()
                .map(DetalleVenta::getSubtotal) // Asume que el subtotal ya está calculado
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void asignarValoresCalculados(Venta venta) {
        for (DetalleVenta detalle : venta.getDetalles()) {
            BigDecimal subtotal = calcularSubtotal(detalle.getPrecioUnitario(), detalle.getCantidad(), detalle.getDescuento());
            BigDecimal subtotalSinIGV = calcularSubtotalSinIGV(subtotal);
            BigDecimal igvAplicado = calcularIgvAplicado(subtotal, subtotalSinIGV);

            detalle.setSubtotal(subtotal);
            detalle.setSubtotalSinIGV(subtotalSinIGV);
            detalle.setIgvAplicado(igvAplicado);
        }

        BigDecimal totalVenta = calcularTotalVenta(venta.getDetalles());
        venta.setTotalVenta(totalVenta);
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

        // 5. Asignar valores calculados
        asignarValoresCalculados(venta);

        // 6. Guardar la venta en la base de datos
        venta = ventaRepository.save(venta);

        // 7. Convertir la entidad Venta guardada de vuelta a VentaDTO
        return convertirAVentaDTO(venta);
    }
}