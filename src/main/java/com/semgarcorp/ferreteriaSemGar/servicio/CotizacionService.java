package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.dto.*;
import com.semgarcorp.ferreteriaSemGar.modelo.*;
import com.semgarcorp.ferreteriaSemGar.repositorio.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CotizacionService {

    private final CotizacionRepository cotizacionRepositorio;
    private final ClienteRepository clienteRepository;
    private final TrabajadorRepository trabajadorRepository;
    private final TipoPagoRepository tipoPagoRepository;
    private final EmpresaRepository empresaRepository;
    private final ProductoRepository productoRepository;
    private final ParametroService parametroService;

    public CotizacionService(CotizacionRepository cotizacionRepositorio, ClienteRepository clienteRepository,
                             TrabajadorRepository trabajadorRepository, TipoPagoRepository tipoPagoRepository,
                             EmpresaRepository empresaRepository, ProductoRepository productoRepository,
                             ParametroService parametroService) {
        this.cotizacionRepositorio = cotizacionRepositorio;
        this.clienteRepository = clienteRepository;
        this.trabajadorRepository = trabajadorRepository;
        this.tipoPagoRepository = tipoPagoRepository;
        this.empresaRepository = empresaRepository;
        this.productoRepository = productoRepository;
        this.parametroService = parametroService;
    }

    // Listar todas las cotizaciones
    public List<Cotizacion> listar() {
        return cotizacionRepositorio.findAll();
    }

    // Obtener una cotización por su ID
    public Cotizacion obtenerPorId(Integer id) {
        return cotizacionRepositorio.findById(id).orElse(null);
    }

    // Guardar una nueva cotización o actualiza una existente
    public Cotizacion guardar(Cotizacion cotizacion) {
        return cotizacionRepositorio.save(cotizacion);
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

    private BigDecimal calcularTotalCotizacion(List<DetalleCotizacion> detallesCotizacion) {
        return detallesCotizacion.stream()
                .map(DetalleCotizacion::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void asignarValoresCalculados(Cotizacion cotizacion) {
        for (DetalleCotizacion detalle : cotizacion.getDetalles()) {
            BigDecimal subtotal = calcularSubtotal(detalle.getPrecioUnitario(), detalle.getCantidad(),
                    detalle.getDescuento());
            BigDecimal subtotalSinIGV = calcularSubtotalSinIGV(subtotal);
            BigDecimal igvAplicado = calcularIgvAplicado(subtotal, subtotalSinIGV);

            detalle.setSubtotal(subtotal);
            detalle.setSubtotalSinIGV(subtotalSinIGV);
            detalle.setIgvAplicado(igvAplicado);
        }

        BigDecimal totalCotizacion = calcularTotalCotizacion(cotizacion.getDetalles());
        cotizacion.setTotalCotizacion(totalCotizacion);
    }

    @Transactional
    public CotizacionDTO actualizar(Integer id, CotizacionDTO cotizacionDTO) {
        // 1. Obtener y validar cotización existente
        Cotizacion cotizacionExistente = cotizacionRepositorio.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cotización no encontrada"));

        // 2. Validar estado PENDIENTE
        if (cotizacionExistente.getEstadoCotizacion() != EstadoCotizacion.PENDIENTE) {
            throw new IllegalStateException("Solo se pueden actualizar cotizaciones PENDIENTES");
        }

        // 3. Actualizar datos complementarios
        if (cotizacionDTO.getIdCliente() != null) {
            cotizacionExistente.setCliente(
                    buscarEntidadPorId(clienteRepository, cotizacionDTO.getIdCliente(), "Cliente"));
        }

        if (cotizacionDTO.getIdEmpresa() != null) {
            cotizacionExistente.setEmpresa(
                    buscarEntidadPorId(empresaRepository, cotizacionDTO.getIdEmpresa(), "Empresa"));
        }

        if (cotizacionDTO.getIdTrabajador() != null) {
            cotizacionExistente.setTrabajador(
                    buscarEntidadPorId(trabajadorRepository, cotizacionDTO.getIdTrabajador(), "Trabajador"));
        }

        if (cotizacionDTO.getIdTipoPago() != null) {
            cotizacionExistente.setTipoPago(
                    buscarEntidadPorId(tipoPagoRepository, cotizacionDTO.getIdTipoPago(), "TipoPago"));
        }

        // 4. Actualizar campos simples
        if (cotizacionDTO.getObservaciones() != null) {
            cotizacionExistente.setObservaciones(cotizacionDTO.getObservaciones());
        }

        // 5. Actualizar detalles (productos)
        if (cotizacionDTO.getDetalles() != null) {
            cotizacionExistente.getDetalles().clear();

            cotizacionDTO.getDetalles().forEach(detalleDTO -> {
                DetalleCotizacion detalle = new DetalleCotizacion();
                detalle.setProducto(
                        buscarEntidadPorId(productoRepository, detalleDTO.getIdProducto(), "Producto"));
                detalle.setCantidad(detalleDTO.getCantidad());
                detalle.setPrecioUnitario(detalleDTO.getPrecioUnitario());
                detalle.setDescuento(detalleDTO.getDescuento() != null ?
                        detalleDTO.getDescuento() : BigDecimal.ZERO);
                detalle.setCotizacion(cotizacionExistente);
                cotizacionExistente.getDetalles().add(detalle);
            });
        }

        // 6. Recalcular valores (subtotal, IGV, total, etc.)
        asignarValoresCalculados(cotizacionExistente);
        cotizacionExistente.setFechaModificacion(LocalDateTime.now());

        // 7. Guardar cambios
        Cotizacion cotizacionActualizada = cotizacionRepositorio.save(cotizacionExistente);

        // 8. Obtener el detalle completo actualizado
        CotizacionDetalleCompletoDTO detalleCompleto = cotizacionRepositorio.findCotizacionDetalleCompletoById(id)
                .orElseThrow(() -> new RuntimeException("Error al obtener el detalle de la cotización actualizada"));

        // 9. Obtener los detalles actualizados
        List<DetalleCotizacionDTO> detalles = cotizacionRepositorio.findDetallesByCotizacionId(id);
        detalleCompleto.setDetalles(detalles);

        // 10. Convertir a DTO y retornar
        return convertirACotizacionDTO(cotizacionActualizada);
    }

    private <T, ID> T buscarEntidadPorId(JpaRepository<T, ID> repository, ID id, String nombreEntidad) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(nombreEntidad + " no encontrado"));
    }

    // Eliminar una cotización por su ID
    public void eliminar(Integer id) {
        cotizacionRepositorio.deleteById(id);
    }

    public CotizacionDTO convertirVentaACotizacion(VentaDTO ventaDTO) {
        // Crear una nueva cotización
        Cotizacion cotizacion = new Cotizacion();
        cotizacion.setCodigoCotizacion(String.valueOf(cotizacionRepositorio.count() + 1));
        cotizacion.setFechaCotizacion(LocalDateTime.now());
        cotizacion.setTotalCotizacion(ventaDTO.getTotalVenta());
        cotizacion.setEstadoCotizacion(EstadoCotizacion.PENDIENTE);
        cotizacion.setCliente(clienteRepository.findById(ventaDTO.getIdCliente())
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado")));
        cotizacion.setTrabajador(trabajadorRepository.findById(ventaDTO.getIdTrabajador())
                .orElseThrow(() -> new EntityNotFoundException("Trabajador no encontrado")));
        cotizacion.setTipoPago(tipoPagoRepository.findById(ventaDTO.getIdTipoPago())
                .orElseThrow(() -> new EntityNotFoundException("Tipo de pago no encontrado")));
        cotizacion.setEmpresa(empresaRepository.findById(ventaDTO.getIdEmpresa())
                .orElseThrow(() -> new EntityNotFoundException("Empresa no encontrada")));
        cotizacion.setObservaciones(ventaDTO.getObservaciones());

        // Convertir detalles de venta a detalles de cotización
        List<DetalleCotizacion> detallesCotizacion = ventaDTO.getDetalles().stream().map(detalleDTO -> {
            DetalleCotizacion detalleCotizacion = new DetalleCotizacion();
            detalleCotizacion.setProducto(productoRepository.findById(detalleDTO.getIdProducto())
                    .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado")));
            detalleCotizacion.setCantidad(detalleDTO.getCantidad());
            detalleCotizacion.setPrecioUnitario(detalleDTO.getPrecioUnitario());
            detalleCotizacion.setDescuento(detalleDTO.getDescuento());
            detalleCotizacion.setSubtotal(detalleDTO.getSubtotal());
            detalleCotizacion.setSubtotalSinIGV(detalleDTO.getSubtotalSinIGV());
            detalleCotizacion.setIgvAplicado(detalleDTO.getIgvAplicado());
            detalleCotizacion.setCotizacion(cotizacion);
            return detalleCotizacion;
        }).toList();

        cotizacion.setDetalles(detallesCotizacion);

        // Guardar la cotización y convertirla a DTO
        Cotizacion cotizacionGuardada = cotizacionRepositorio.save(cotizacion);
        return convertirACotizacionDTO(cotizacionGuardada);
    }

    // Metodo auxiliar para convertir una entidad Cotizacion a CotizacionDTO
    private CotizacionDTO convertirACotizacionDTO(Cotizacion cotizacion) {
        if (cotizacion == null) {
            return null;
        }

        CotizacionDTO cotizacionDTO = new CotizacionDTO();

        // Mapear campos básicos
        cotizacionDTO.setIdCotizacion(cotizacion.getIdCotizacion());
        cotizacionDTO.setCodigoCotizacion(cotizacion.getCodigoCotizacion());
        cotizacionDTO.setFechaCotizacion(cotizacion.getFechaCotizacion());
        cotizacionDTO.setTotalCotizacion(cotizacion.getTotalCotizacion());
        cotizacionDTO.setObservaciones(cotizacion.getObservaciones());
        cotizacionDTO.setEstadoCotizacion(cotizacion.getEstadoCotizacion());

        // Mapear relaciones (IDs)
        if (cotizacion.getCliente() != null) {
            cotizacionDTO.setIdCliente(cotizacion.getCliente().getIdCliente());
        }
        if (cotizacion.getEmpresa() != null) {
            cotizacionDTO.setIdEmpresa(cotizacion.getEmpresa().getIdEmpresa());
        }
        if (cotizacion.getTrabajador() != null) {
            cotizacionDTO.setIdTrabajador(cotizacion.getTrabajador().getIdTrabajador());
        }
        if (cotizacion.getTipoPago() != null) {
            cotizacionDTO.setIdTipoPago(cotizacion.getTipoPago().getIdTipoPago());
        }

        // Mapear detalles
        if (cotizacion.getDetalles() != null && !cotizacion.getDetalles().isEmpty()) {
            List<DetalleCotizacionDTO> detallesDTO = new ArrayList<>();

            for (DetalleCotizacion detalle : cotizacion.getDetalles()) {
                DetalleCotizacionDTO detalleDTO = new DetalleCotizacionDTO();
                detalleDTO.setIdDetalleCotizacion(detalle.getIdDetalleCotizacion());
                detalleDTO.setIdProducto(detalle.getProducto().getIdProducto());
                detalleDTO.setNombreProducto(detalle.getProducto().getNombreProducto());
                detalleDTO.setUnidadMedida(detalle.getProducto().getUnidadMedida().getNombreUnidad());
                detalleDTO.setCantidad(detalle.getCantidad());
                detalleDTO.setPrecioUnitario(detalle.getPrecioUnitario());
                detalleDTO.setDescuento(detalle.getDescuento());
                detalleDTO.setSubtotal(detalle.getSubtotal());
                detalleDTO.setSubtotalSinIGV(detalle.getSubtotalSinIGV());
                detalleDTO.setIgvAplicado(detalle.getIgvAplicado());

                detallesDTO.add(detalleDTO);
            }

            cotizacionDTO.setDetalles(detallesDTO);
        }

        return cotizacionDTO;
    }

    private String generarCodigoCotizacion() {
        Long numeroSecuencial = cotizacionRepositorio.count() + 1;
        return String.format("%04d", numeroSecuencial); // Ejemplo: "0001", "0002", ..., "0100"
    }

    public List<CotizacionResumenDTO> obtenerTodasCotizacionesResumen() {
        return cotizacionRepositorio.findAllCotizacionesResumen();
    }

    public CotizacionDetalleCompletoDTO obtenerCotizacionDetalleCompleto(Integer idCotizacion) {
        // 1. Obtener datos principales de la cotización
        CotizacionDetalleCompletoDTO cotizacionDTO = cotizacionRepositorio.findCotizacionDetalleCompletoById(idCotizacion)
                .orElseThrow(() -> new RuntimeException("Cotización no encontrada"));

        // 2. Obtener detalles de la cotización
        List<DetalleCotizacionDTO> detalles = cotizacionRepositorio.findDetallesByCotizacionId(idCotizacion);

        // 3. Combinar resultados
        cotizacionDTO.setDetalles(detalles);

        return cotizacionDTO;
    }
}
