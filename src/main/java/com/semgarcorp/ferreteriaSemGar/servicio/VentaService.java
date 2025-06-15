package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.dto.DetalleVentaDTO;
import com.semgarcorp.ferreteriaSemGar.dto.MovimientoCajaDTO;
import com.semgarcorp.ferreteriaSemGar.dto.VentaDTO;
import com.semgarcorp.ferreteriaSemGar.dto.VentaDetalleCompletoDTO;
import com.semgarcorp.ferreteriaSemGar.dto.VentaResumenDTO;
import com.semgarcorp.ferreteriaSemGar.integracion.NubeFactClient;
import com.semgarcorp.ferreteriaSemGar.integracion.NubeFactService;
import com.semgarcorp.ferreteriaSemGar.modelo.*;
import com.semgarcorp.ferreteriaSemGar.repositorio.*;
import com.semgarcorp.ferreteriaSemGar.util.InternetUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class VentaService {

    private final ParametroService parametroService;

    private final VentaRepository ventaRepository;

    private final ProductoRepository productoRepository;

    private final TipoPagoRepository tipoPagoRepository;

    private final EmpresaRepository empresaRepository;

    private final ClienteRepository clienteRepository;

    private final TipoComprobantePagoRepository tipoComprobanteRepository;

    private final TrabajadorRepository trabajadorRepository;

    private final CajaRepository cajaRepository;

    private final CotizacionRepository cotizacionRepository;

    private final ProductoService productoService;

    private final CajaService cajaService;

    private final NubeFactService nubeFactService;

    private final NubeFactClient nubeFactClient;

    public VentaService(ParametroService parametroService, VentaRepository ventaRepository,
            ProductoRepository productoRepository, TipoPagoRepository tipoPagoRepository,
            EmpresaRepository empresaRepository, ClienteRepository clienteRepository,
            TipoComprobantePagoRepository tipoComprobanteRepository, TrabajadorRepository trabajadorRepository,
            CajaRepository cajaRepository, CotizacionRepository cotizacionRepository, ProductoService productoService,
            CajaService cajaService, NubeFactService nubeFactService, NubeFactClient nubeFactClient) {
        this.parametroService = parametroService;
        this.ventaRepository = ventaRepository;
        this.productoRepository = productoRepository;
        this.tipoPagoRepository = tipoPagoRepository;
        this.empresaRepository = empresaRepository;
        this.clienteRepository = clienteRepository;
        this.tipoComprobanteRepository = tipoComprobanteRepository;
        this.trabajadorRepository = trabajadorRepository;
        this.cajaRepository = cajaRepository;
        this.cotizacionRepository = cotizacionRepository;
        this.productoService = productoService;
        this.cajaService = cajaService;
        this.nubeFactService = nubeFactService;
        this.nubeFactClient = nubeFactClient;
    }

    public List<VentaDTO> listar() {
        List<Venta> ventas = ventaRepository.findAll(); // Obtener las entidades
        return ventas.stream()
                .map(this::convertirAVentaDTO) // Convertir cada entidad a DTO
                .collect(Collectors.toList());
    }

    public Optional<VentaDTO> obtenerPorId(Integer id) {
        return ventaRepository.findById(id).map(this::convertirAVentaDTO);
    }

    @Transactional
    public VentaDTO actualizar(Integer id, VentaDTO ventaDTO) {
        // 1. Obtener venta existente (asegurando que está PENDIENTE)
        Venta ventaExistente = ventaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Venta no encontrada con ID: " + id));

        // 2. Validar que la venta esté PENDIENTE (restricción clave)
        if (ventaExistente.getEstadoVenta() != EstadoVenta.PENDIENTE) {
            throw new IllegalStateException("Solo se pueden actualizar ventas en estado PENDIENTE");
        }

        // 3. Validar que no se intente cambiar el estado (opcional, según negocio)
        if (ventaDTO.getEstadoVenta() != null && !ventaDTO.getEstadoVenta().equals(EstadoVenta.PENDIENTE)) {
            throw new IllegalArgumentException(
                    "No se puede cambiar el estado de una venta PENDIENTE mediante esta operación");
        }

        // 4. Actualizar datos complementarios (sin validar serie/número porque es
        // PENDIENTE)
        if (ventaDTO.getIdEmpresa() != null) {
            ventaExistente.setEmpresa(buscarEntidadPorId(empresaRepository, ventaDTO.getIdEmpresa(), "Empresa"));
        }

        if (ventaDTO.getIdCliente() != null) {
            ventaExistente.setCliente(buscarEntidadPorId(clienteRepository, ventaDTO.getIdCliente(), "Cliente"));
        }

        if (ventaDTO.getIdTipoComprobantePago() != null) {
            // ¡Sin validación de serie/número porque es PENDIENTE!
            ventaExistente.setTipoComprobantePago(
                    buscarEntidadPorId(tipoComprobanteRepository, ventaDTO.getIdTipoComprobantePago(),
                            "TipoComprobantePago"));
        }

        if (ventaDTO.getIdTipoPago() != null) {
            ventaExistente.setTipoPago(buscarEntidadPorId(tipoPagoRepository, ventaDTO.getIdTipoPago(), "TipoPago"));
        }

        if (ventaDTO.getIdTrabajador() != null) {
            ventaExistente
                    .setTrabajador(buscarEntidadPorId(trabajadorRepository, ventaDTO.getIdTrabajador(), "Trabajador"));
        }

        // 5. Actualizar campos simples
        if (ventaDTO.getFechaVenta() != null) {
            ventaExistente.setFechaVenta(ventaDTO.getFechaVenta());
        }

        if (ventaDTO.getObservaciones() != null) {
            ventaExistente.setObservaciones(ventaDTO.getObservaciones());
        }

        // 6. Actualizar detalles (sin restricciones porque es PENDIENTE)
        if (ventaDTO.getDetalles() != null) {
            ventaExistente.getDetalles().clear();
            List<DetalleVenta> nuevosDetalles = ventaDTO.getDetalles().stream()
                    .map(detalleDTO -> {
                        DetalleVenta detalle = new DetalleVenta();
                        detalle.setProducto(
                                buscarEntidadPorId(productoRepository, detalleDTO.getIdProducto(), "Producto"));
                        detalle.setCantidad(detalleDTO.getCantidad());
                        detalle.setPrecioUnitario(detalleDTO.getPrecioUnitario());
                        detalle.setDescuento(detalleDTO.getDescuento());
                        detalle.setVenta(ventaExistente);
                        return detalle;
                    })
                    .collect(Collectors.toList());
            ventaExistente.getDetalles().addAll(nuevosDetalles);
        }

        // 7. Recalcular valores (subtotal, IGV, total, etc.)
        asignarValoresCalculados(ventaExistente);

        // 8. Guardar cambios
        Venta ventaActualizada = ventaRepository.save(ventaExistente);
        return convertirAVentaDTO(ventaActualizada);
    }

    public void eliminar(Integer id) {
        ventaRepository.deleteById(id);
    }

    public Venta convertirAVenta(VentaDTO ventaDTO) {
        Venta venta = new Venta();

        // Asignar campos básicos de Venta
        venta.setSerieComprobante(ventaDTO.getSerieComprobante() != null ? ventaDTO.getSerieComprobante() : ""); // Valor
                                                                                                                 // temporal
        venta.setNumeroComprobante(ventaDTO.getNumeroComprobante() != null ? ventaDTO.getNumeroComprobante() : ""); // Valor
                                                                                                                    // temporal
        venta.setFechaVenta(ventaDTO.getFechaVenta());
        venta.setEstadoVenta(ventaDTO.getEstadoVenta() != null ? ventaDTO.getEstadoVenta() : EstadoVenta.PENDIENTE);
        venta.setTotalVenta(ventaDTO.getTotalVenta());
        venta.setFechaModificacion(ventaDTO.getFechaModificacion());
        venta.setObservaciones(ventaDTO.getObservaciones());
        venta.setMoneda(ventaDTO.getMoneda() != null ? ventaDTO.getMoneda() : Moneda.SOLES); // Valor por defecto SOLES

        // Asignar idCaja solo si la venta no está en estado PENDIENTE
        if (ventaDTO.getEstadoVenta() != null && ventaDTO.getEstadoVenta() != EstadoVenta.PENDIENTE) {
            if (ventaDTO.getIdCaja() == null) {
                throw new IllegalArgumentException("Para una venta no PENDIENTE, el idCaja es obligatorio");
            }
            venta.setCaja(buscarEntidadPorId(cajaRepository, ventaDTO.getIdCaja(), "Caja"));
        } else {
            venta.setCaja(null); // Establecer caja como null si la venta está en estado PENDIENTE o idCaja es
                                 // null
        }

        // Cargar entidades relacionadas de forma más eficiente
        venta.setEmpresa(buscarEntidadPorId(empresaRepository, ventaDTO.getIdEmpresa(), "Empresa"));
        venta.setTipoComprobantePago(buscarEntidadPorId(tipoComprobanteRepository, ventaDTO.getIdTipoComprobantePago(),
                "TipoComprobantePago"));
        venta.setTrabajador(buscarEntidadPorId(trabajadorRepository, ventaDTO.getIdTrabajador(), "Trabajador"));
        venta.setCliente(buscarEntidadPorId(clienteRepository, ventaDTO.getIdCliente(), "Cliente"));
        venta.setTipoPago(buscarEntidadPorId(tipoPagoRepository, ventaDTO.getIdTipoPago(), "TipoPago"));

        // Convertir detallesVentaDTO a DetalleVenta (validando que la lista no sea
        // null)
        List<DetalleVenta> detallesVenta = ventaDTO.getDetalles() == null ? new ArrayList<>()
                : ventaDTO.getDetalles().stream()
                        .map(detalleDTO -> {
                            DetalleVenta detalle = new DetalleVenta();
                            detalle.setProducto(
                                    buscarEntidadPorId(productoRepository, detalleDTO.getIdProducto(), "Producto"));
                            detalle.setCantidad(detalleDTO.getCantidad());
                            detalle.setPrecioUnitario(detalleDTO.getPrecioUnitario());
                            detalle.setDescuento(detalleDTO.getDescuento());
                            detalle.setSubtotalSinIGV(
                                    detalleDTO.getSubtotalSinIGV() != null ? detalleDTO.getSubtotalSinIGV()
                                            : BigDecimal.ZERO);
                            detalle.setIgvAplicado(detalleDTO.getIgvAplicado() != null ? detalleDTO.getIgvAplicado()
                                    : BigDecimal.ZERO);
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

        // 3. Validar la caja y asignarla a la venta
        Caja caja = cajaRepository.findById(idCaja)
                .orElseThrow(() -> new EntityNotFoundException("Caja con ID " + idCaja + " no encontrada"));

        // Validar que la caja esté abierta
        if (caja.getEstado() != EstadoCaja.ABIERTA) {
            throw new IllegalStateException("La caja no está abierta");
        }

        // Validar que la caja tenga un responsable
        if (caja.getResponsable() == null) {
            throw new IllegalStateException("La caja no tiene un responsable asignado");
        }

        // Validar que el trabajador vinculado al usuario responsable de la caja coincida con el trabajador de la venta
        Trabajador trabajadorResponsableCaja = caja.getResponsable().getTrabajador();
        if (trabajadorResponsableCaja == null) {
            throw new IllegalStateException("El responsable de la caja no tiene un trabajador vinculado");
        }

        if (!trabajadorResponsableCaja.getIdTrabajador().equals(venta.getTrabajador().getIdTrabajador())) {
            throw new IllegalStateException("El trabajador de la venta no está autorizado para usar esta caja");
        }

        venta.setCaja(caja);

        // --- VALIDACIONES ADICIONALES ---
        // 4. Validar stock antes de generar número
        for (DetalleVenta detalle : venta.getDetalles()) {
            productoService.validarStock(detalle.getProducto().getIdProducto(), detalle.getCantidad());
        }

        // 5. Generar serie y número (antes de emitir el comprobante)
        String serie = generarSerie(venta.getTipoComprobantePago());
        String numero = generarNumero(serie, venta.getTipoComprobantePago());
        venta.setSerieComprobante(serie);
        venta.setNumeroComprobante(numero);

        // 6. Convertir la venta a DTO (ahora con serie y número asignados)
        VentaDTO ventaDTO = convertirAVentaDTO(venta);

        if (!InternetUtils.hayConexion()) {
            throw new IllegalStateException("No hay conexión a Internet. No se puede completar la venta.");
        }

        // 7. Emitir el comprobante en NubeFact
        String respuesta = nubeFactService.emitirComprobante(ventaDTO);
        System.out.println("Respuesta de NubeFact: " + respuesta);

        // 8. Reducir el stock de los productos
        for (DetalleVenta detalle : venta.getDetalles()) {
            productoService.reducirStock(detalle.getProducto().getIdProducto(), detalle.getCantidad());
        }

        // 9. Cambiar el estado a "Completada"
        venta.setEstadoVenta(EstadoVenta.COMPLETADA);
        venta = ventaRepository.save(venta);

        // 10. Registrar entrada en caja si es pago en efectivo
        if (venta.getTipoPago().getNombre().equalsIgnoreCase("EFECTIVO")) {
            cajaService.registrarEntradaPorVenta(venta.getCaja().getIdCaja(), venta.getTotalVenta());
        }

        return convertirAVentaDTO(venta);
    }

    // Mapeo de códigos NubeFact a series
    private static final Map<Integer, String> SERIES_POR_CODIGO = Map.of(
            1, "FFF1",
            2, "BBB1");

    private String generarSerie(TipoComprobantePago tipoComprobante) {
        Integer codigo = tipoComprobante.getCodigoNubefact();

        if (!SERIES_POR_CODIGO.containsKey(codigo)) {
            throw new IllegalArgumentException("Código NubeFact no válido: " + codigo);
        }

        return SERIES_POR_CODIGO.get(codigo);
    }

    // Metodo para obtener el próximo número disponible
    private String generarNumero(String serie, TipoComprobantePago tipoComprobante) {
        // 1. Último número en base de datos para esta serie y tipo
        Integer ultimoNumeroLocal = ventaRepository
                .findMaxNumeroBySerieAndTipo(serie, tipoComprobante.getCodigoNubefact())
                .orElse(0);

        // 2. Verificar en NubeFact
        Integer ultimoNumeroNubeFact = consultarUltimoNumeroEnNubeFact(serie, tipoComprobante);

        // 3. Calcular próximo número
        Integer proximoNumero = Math.max(ultimoNumeroLocal, ultimoNumeroNubeFact) + 1;

        return String.format("%04d", proximoNumero); // Formato 0001, 0002, etc.
    }

    // Metodo para consultar el último número en NubeFact
    private Integer consultarUltimoNumeroEnNubeFact(String serie, TipoComprobantePago tipoComprobante) {
        try {
            // Consulta a NubeFact para obtener comprobantes de esta serie
            String response = nubeFactClient.consultarComprobantes(
                    tipoComprobante.getCodigoNubefact(),
                    serie);

            // Parsear la respuesta para encontrar el máximo número
            return parsearUltimoNumero(response);
        } catch (Exception e) {
            // Si falla la consulta, asumimos 0
            return 0;
        }
    }

    // Metodo para parsear la respuesta de NubeFact
    private Integer parsearUltimoNumero(String jsonResponse) {
        // Implementación básica - deberías adaptarla al formato real de respuesta
        // Este es un ejemplo simplificado
        if (jsonResponse.contains("\"numero\":")) {
            Pattern pattern = Pattern.compile("\"numero\":\"?(\\d+)\"?");
            Matcher matcher = pattern.matcher(jsonResponse);
            List<Integer> numeros = new ArrayList<>();
            while (matcher.find()) {
                numeros.add(Integer.parseInt(matcher.group(1)));
            }
            return numeros.stream().max(Integer::compare).orElse(0);
        }
        return 0;
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
        ventaDTO.setMoneda(venta.getMoneda()); // Asignar la moneda de la entidad al DTO

        // Asignar IDs de entidades relacionadas (con validación de null)
        ventaDTO.setIdCaja(venta.getCaja() != null ? venta.getCaja().getIdCaja() : null);
        ventaDTO.setIdEmpresa(venta.getEmpresa() != null ? venta.getEmpresa().getIdEmpresa() : null);
        ventaDTO.setIdTipoComprobantePago(
                venta.getTipoComprobantePago() != null ? venta.getTipoComprobantePago().getIdTipoComprobantePago()
                        : null);
        ventaDTO.setIdTrabajador(venta.getTrabajador() != null ? venta.getTrabajador().getIdTrabajador() : null);
        ventaDTO.setIdCliente(venta.getCliente() != null ? venta.getCliente().getIdCliente() : null);
        ventaDTO.setIdTipoPago(venta.getTipoPago() != null ? venta.getTipoPago().getIdTipoPago() : null);

        // Convertir detallesVenta a DetalleVentaDTO (con validación de null)
        List<DetalleVentaDTO> detallesVentaDTO = venta.getDetalles() != null ? venta.getDetalles().stream()
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
                    detalleDTO.setSubtotal(detalle.getSubtotal());
                    return detalleDTO;
                })
                .collect(Collectors.toList()) : new ArrayList<>(); // Si detalles es null, se asigna una lista vacía

        ventaDTO.setDetalles(detallesVentaDTO);
        return ventaDTO;
    }

    private BigDecimal calcularSubtotal(BigDecimal precioUnitario, BigDecimal cantidad,
            BigDecimal descuentoPorcentual) {
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
            BigDecimal subtotal = calcularSubtotal(detalle.getPrecioUnitario(), detalle.getCantidad(),
                    detalle.getDescuento());
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

        if (ventaDTO.getMoneda() == null) {
            ventaDTO.setMoneda(Moneda.SOLES); // Establecer valor por defecto
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

    public Page<VentaResumenDTO> listarVentasPendientes(int pagina, int size) {
        Pageable pageable = PageRequest.of(pagina, size);
        return ventaRepository.findAllVentasResumen(
                EstadoVenta.PENDIENTE, // Solo ventas pendientes
                pageable);
    }

    public Page<VentaResumenDTO> listarVentasCompletadasYAnuladas(int pagina, int size) {
        Pageable pageable = PageRequest.of(pagina, size);
        // Usa el nuevo metodo del repositorio con ambos estados
        return ventaRepository.findAllVentasResumenByEstados(
                List.of(EstadoVenta.COMPLETADA, EstadoVenta.ANULADA),
                pageable);
    }

    public VentaDetalleCompletoDTO obtenerVentaDetalleCompleto(Integer idVenta) {
        // 1. Obtener datos principales
        VentaDetalleCompletoDTO ventaDTO = ventaRepository.findVentaDetalleCompletoById(idVenta)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        // 2. Obtener detalles
        List<DetalleVentaDTO> detalles = ventaRepository.findDetallesByVentaId(idVenta);

        // 3. Combinar resultados
        ventaDTO.setDetalles(detalles);

        return ventaDTO;
    }

    private VentaDetalleCompletoDTO convertirAVentaDetalleCompletoDTO(Venta venta) {
        // Crear una instancia del DTO con el constructor por defecto
        VentaDetalleCompletoDTO dto = new VentaDetalleCompletoDTO();

        // Establecer los valores básicos
        dto.setIdVenta(venta.getIdVenta());
        dto.setSerieComprobante(venta.getSerieComprobante());
        dto.setNumeroComprobante(venta.getNumeroComprobante());
        dto.setMoneda(venta.getMoneda());
        dto.setTotalVenta(venta.getTotalVenta());
        dto.setFechaVenta(venta.getFechaVenta());
        dto.setEstadoVenta(venta.getEstadoVenta());

        // Establecer valores de relaciones, manejando posibles nulos
        if (venta.getTipoComprobantePago() != null) {
            dto.setTipoComprobante(venta.getTipoComprobantePago().getNombre());
        } else {
            dto.setTipoComprobante("N/A");
        }

        if (venta.getTipoPago() != null) {
            dto.setTipoPago(venta.getTipoPago().getNombre());
        } else {
            dto.setTipoPago("N/A");
        }

        // Información del cliente
        if (venta.getCliente() != null) {
            dto.setNombresCliente(venta.getCliente().getNombres() != null ? venta.getCliente().getNombres() : "");
            dto.setApellidosCliente(venta.getCliente().getApellidos() != null ? venta.getCliente().getApellidos() : "");
            dto.setRazonSocialCliente(
                    venta.getCliente().getRazonSocial() != null ? venta.getCliente().getRazonSocial() : "");
        }

        // Información de la empresa
        if (venta.getEmpresa() != null) {
            dto.setRazonSocialEmpresa(venta.getEmpresa().getRazonSocial());
        }

        // Convertir y establecer los detalles
        if (venta.getDetalles() != null && !venta.getDetalles().isEmpty()) {
            List<DetalleVentaDTO> detallesDTO = venta.getDetalles().stream()
                    .map(this::convertirADetalleVentaDTO)
                    .toList();
            dto.setDetalles(detallesDTO);
        }

        return dto;
    }

    /**
     * Metodo para convertir una entidad DetalleVenta a DetalleVentaDTO
     */
    private DetalleVentaDTO convertirADetalleVentaDTO(DetalleVenta detalle) {
        DetalleVentaDTO dto = new DetalleVentaDTO();

        dto.setIdDetalleVenta(detalle.getIdDetalleVenta());
        dto.setIdVenta(detalle.getVenta().getIdVenta());
        dto.setIdProducto(detalle.getProducto().getIdProducto());
        dto.setNombreProducto(detalle.getProducto().getNombreProducto());
        // Añadir la unidad de medida (con validación de nulos)
        dto.setUnidadMedida(
                detalle.getProducto().getUnidadMedida() != null
                        ? detalle.getProducto().getUnidadMedida().getNombreUnidad()
                        : "Sin unidad" // Valor por defecto opcional
        );
        dto.setCantidad(detalle.getCantidad());
        dto.setPrecioUnitario(detalle.getPrecioUnitario());
        dto.setDescuento(detalle.getDescuento());
        dto.setSubtotal(detalle.getSubtotal());
        dto.setSubtotalSinIGV(detalle.getSubtotalSinIGV());
        dto.setIgvAplicado(detalle.getIgvAplicado());

        return dto;
    }

    public VentaDTO convertirCotizacionAVenta(String codigoCotizacion) {
        // Buscar la cotización por su código
        Cotizacion cotizacion = cotizacionRepository.findByCodigoCotizacion(codigoCotizacion)
                .orElseThrow(() -> new EntityNotFoundException("Cotización no encontrada"));

        // Crear el DTO de venta (sin persistir)
        VentaDTO ventaDTO = new VentaDTO();

        // Copiar datos generales
        ventaDTO.setIdCliente(cotizacion.getCliente().getIdCliente());
        ventaDTO.setIdTrabajador(cotizacion.getTrabajador().getIdTrabajador());
        ventaDTO.setIdTipoPago(cotizacion.getTipoPago().getIdTipoPago());
        ventaDTO.setIdEmpresa(cotizacion.getEmpresa().getIdEmpresa());
        ventaDTO.setTotalVenta(cotizacion.getTotalCotizacion());
        ventaDTO.setObservaciones("Generado desde cotización: " + codigoCotizacion); // Opcional

        // Copiar detalles (¡Ahora con todos los campos necesarios!)
        List<DetalleVentaDTO> detallesDTO = cotizacion.getDetalles().stream().map(detalle -> {
            DetalleVentaDTO detalleDTO = new DetalleVentaDTO();
            detalleDTO.setIdProducto(detalle.getProducto().getIdProducto());
            detalleDTO.setNombreProducto(detalle.getProducto().getNombreProducto()); // Nombre del producto
            detalleDTO.setUnidadMedida(detalle.getProducto().getUnidadMedida().getNombreUnidad());
            detalleDTO.setCantidad(detalle.getCantidad());
            detalleDTO.setPrecioUnitario(detalle.getPrecioUnitario());
            detalleDTO.setDescuento(detalle.getDescuento());
            detalleDTO.setSubtotal(detalle.getSubtotal());
            detalleDTO.setSubtotalSinIGV(detalle.getSubtotalSinIGV());
            detalleDTO.setIgvAplicado(detalle.getIgvAplicado());
            return detalleDTO;
        }).toList();

        ventaDTO.setDetalles(detallesDTO);

        return ventaDTO;
    }

    public VentaDTO convertirCotizacionAVentaPorId(Integer idCotizacion) {
        // Buscar la cotización por su ID
        Cotizacion cotizacion = cotizacionRepository.findById(idCotizacion)
                .orElseThrow(() -> new EntityNotFoundException("Cotización no encontrada"));

        // Crear el DTO de venta (sin persistir)
        VentaDTO ventaDTO = new VentaDTO();

        // Copiar datos generales
        ventaDTO.setIdCliente(cotizacion.getCliente().getIdCliente());
        ventaDTO.setIdTrabajador(cotizacion.getTrabajador().getIdTrabajador());
        ventaDTO.setIdTipoPago(cotizacion.getTipoPago().getIdTipoPago());
        ventaDTO.setIdEmpresa(cotizacion.getEmpresa().getIdEmpresa());
        ventaDTO.setTotalVenta(cotizacion.getTotalCotizacion());
        ventaDTO.setObservaciones("Generado desde cotización ID: " + idCotizacion); // Cambiado para reflejar ID

        // Copiar detalles (idéntico al original)
        List<DetalleVentaDTO> detallesDTO = cotizacion.getDetalles().stream().map(detalle -> {
            DetalleVentaDTO detalleDTO = new DetalleVentaDTO();
            detalleDTO.setIdProducto(detalle.getProducto().getIdProducto());
            detalleDTO.setNombreProducto(detalle.getProducto().getNombreProducto());
            detalleDTO.setUnidadMedida(detalle.getProducto().getUnidadMedida().getNombreUnidad());
            detalleDTO.setCantidad(detalle.getCantidad());
            detalleDTO.setPrecioUnitario(detalle.getPrecioUnitario());
            detalleDTO.setDescuento(detalle.getDescuento());
            detalleDTO.setSubtotal(detalle.getSubtotal());
            detalleDTO.setSubtotalSinIGV(detalle.getSubtotalSinIGV());
            detalleDTO.setIgvAplicado(detalle.getIgvAplicado());
            return detalleDTO;
        }).toList();

        ventaDTO.setDetalles(detallesDTO);

        return ventaDTO;
    }

    @Transactional
    public VentaDTO anularVenta(Integer idVenta) {
        // 1. Buscar la venta por ID
        Venta venta = ventaRepository.findById(idVenta)
                .orElseThrow(() -> new EntityNotFoundException("Venta con ID " + idVenta + " no encontrada"));

        // 2. Validar que la venta esté en estado COMPLETADA
        if (venta.getEstadoVenta() != EstadoVenta.COMPLETADA) {
            throw new IllegalStateException("Solo se pueden anular ventas en estado COMPLETADA");
        }

        // 3. Validar que tenga serie y número (necesario para anulación en NubeFact)
        if (venta.getSerieComprobante() == null || venta.getNumeroComprobante() == null) {
            throw new IllegalStateException("La venta no tiene serie o número de comprobante");
        }

        // 4. Validar conexión a internet
        if (!InternetUtils.hayConexion()) {
            throw new IllegalStateException("No hay conexión a Internet. No se puede anular la venta.");
        }

        try {
            // 5. Anular en NubeFact (el servicio se encarga de construir el JSON)
            String respuesta = nubeFactService.anularComprobante(
                venta.getSerieComprobante(),
                venta.getNumeroComprobante(),
                "ERROR DEL SISTEMA", // Motivo fijo
                "", // código único vacío por ahora
                venta.getTipoComprobantePago().getCodigoNubefact()
            );

            // 6. Si la anulación en NubeFact fue exitosa, actualizar el estado de la venta
            venta.setEstadoVenta(EstadoVenta.ANULADA);
            venta = ventaRepository.save(venta);

            // 7. Devolver el stock de los productos
            for (DetalleVenta detalle : venta.getDetalles()) {
                productoService.incrementarStock(detalle.getProducto().getIdProducto(), detalle.getCantidad());
            }

            // 8. Si la venta fue en efectivo, registrar la salida en caja
            if (venta.getTipoPago().getNombre().equalsIgnoreCase("EFECTIVO")) {
                MovimientoCajaDTO movimientoDTO = new MovimientoCajaDTO();
                movimientoDTO.setMonto(venta.getTotalVenta());
                movimientoDTO.setTipo(TipoMovimiento.SALIDA);
                movimientoDTO.setObservaciones("Salida por anulación de venta " + venta.getSerieComprobante() + "-" + venta.getNumeroComprobante());
                cajaService.registrarSalidaManual(venta.getCaja().getIdCaja(), movimientoDTO);
            }

            // 9. Devolver la venta actualizada
            return convertirAVentaDTO(venta);

        } catch (Exception e) {
            throw new RuntimeException("Error al anular la venta en NubeFact: " + e.getMessage(), e);
        }
    }
}