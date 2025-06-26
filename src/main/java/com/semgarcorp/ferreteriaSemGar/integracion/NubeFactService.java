package com.semgarcorp.ferreteriaSemGar.integracion;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.semgarcorp.ferreteriaSemGar.modelo.*;
import com.semgarcorp.ferreteriaSemGar.repositorio.ClienteRepository;
import com.semgarcorp.ferreteriaSemGar.repositorio.ProductoRepository;
import com.semgarcorp.ferreteriaSemGar.repositorio.ParametroRepository;
import com.semgarcorp.ferreteriaSemGar.repositorio.TipoComprobantePagoRepository;
import com.semgarcorp.ferreteriaSemGar.repositorio.ComprobanteNubeFactRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class NubeFactService {
    private static final Logger logger = LoggerFactory.getLogger(NubeFactService.class);

    private final NubeFactClient client;
    private final ObjectMapper objectMapper;
    private final ParametroRepository parametroRepositorio;
    private final ClienteRepository clienteRepositorio;
    private final TipoComprobantePagoRepository tipoComprobantePagoRepositorio;
    private final ComprobanteNubeFactRepository comprobanteNubeFactRepository;

    public NubeFactService(NubeFactClient client, ObjectMapper objectMapper,
                           ParametroRepository parametroRepositorio,
                           ClienteRepository clienteRepositorio,
                           ProductoRepository productoRepositorio,
                           TipoComprobantePagoRepository tipoComprobantePagoRepositorio,
                           ComprobanteNubeFactRepository comprobanteNubeFactRepository,
                           RestTemplate restTemplate) {
        this.client = client;
        this.objectMapper = objectMapper;
        this.parametroRepositorio = parametroRepositorio;
        this.clienteRepositorio = clienteRepositorio;
        this.tipoComprobantePagoRepositorio = tipoComprobantePagoRepositorio;
        this.comprobanteNubeFactRepository = comprobanteNubeFactRepository;
    }

    public String emitirComprobante(Venta venta) {
        validarDatosVenta(venta);

        Cliente cliente = obtenerCliente(venta.getCliente().getIdCliente());
        TipoComprobantePago tipoComprobante = obtenerTipoComprobante(venta.getTipoComprobantePago().getIdTipoComprobantePago());

        Map<String, Object> request = construirRequestBase(venta, cliente, tipoComprobante.getCodigoNubefact());
        request.put("cliente_tipo_de_documento", determinarTipoDocumento(cliente, tipoComprobante.getCodigoNubefact()));
        request.put("items", construirItems(venta.getDetalles()));

        return enviarANubefact(request, venta);
    }

    private void validarDatosVenta(Venta venta) {
        if (venta == null || venta.getDetalles() == null ||
                venta.getCliente() == null || venta.getTipoComprobantePago() == null) {
            throw new IllegalArgumentException("Datos de venta incompletos");
        }
    }

    private Cliente obtenerCliente(Integer idCliente) {
        return clienteRepositorio.findById(idCliente)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
    }

    private TipoComprobantePago obtenerTipoComprobante(Integer idTipoComprobante) {
        return tipoComprobantePagoRepositorio.findById(idTipoComprobante)
                .orElseThrow(() -> new IllegalArgumentException("Tipo de comprobante no encontrado"));
    }

    private int determinarTipoDocumento(Cliente cliente, int codigoNubefact) {
        if (cliente.getTipoDocumento() == null) {
            throw new IllegalStateException("El cliente no tiene tipo de documento definido");
        }

        // Primero validamos si es factura (código 1) y el cliente no tiene RUC
        if (codigoNubefact == 1 && !"RUC".equalsIgnoreCase(cliente.getTipoDocumento().getAbreviatura())) {
            throw new IllegalStateException("Para facturas el cliente debe tener RUC");
        }

        // Luego determinamos el código a enviar a NubeFact
        return "RUC".equalsIgnoreCase(cliente.getTipoDocumento().getAbreviatura()) ? 6 : 1;
    }

    private Map<String, Object> construirRequestBase(Venta venta, Cliente cliente, int codigoNubefact) {
        Map<String, Object> request = new HashMap<>();

        // Datos básicos del comprobante
        request.put("operacion", "generar_comprobante");
        request.put("tipo_de_comprobante", codigoNubefact);
        request.put("serie", venta.getSerieComprobante());
        request.put("numero", venta.getNumeroComprobante());
        request.put("sunat_transaction", 1);

        // Datos del cliente
        request.put("cliente_tipo_de_documento", determinarTipoDocumento(cliente, codigoNubefact));
        request.put("cliente_numero_de_documento", cliente.getNumeroDocumento());
        request.put("cliente_denominacion", obtenerDenominacionCliente(cliente));
        request.put("cliente_direccion", Optional.ofNullable(cliente.getDireccion()).orElse(""));
        request.put("cliente_email", Optional.ofNullable(cliente.getCorreo()).orElse(""));

        // Fechas y moneda
        request.put("fecha_de_emision", venta.getFechaVenta().toString());
        request.put("moneda", Integer.parseInt(venta.getMoneda().getCodigoNubefact())); // 1 = Soles
        request.put("tipo_de_cambio", 3.75);
        request.put("porcentaje_de_igv", obtenerPorcentajeIgv());
        
        // Campos adicionales requeridos por NubeFact
        request.put("enviar_automaticamente_a_la_sunat", "true");
        request.put("enviar_automaticamente_al_cliente", "true");
        request.put("codigo_unico", "");
        request.put("condiciones_de_pago", "");
        request.put("medio_de_pago", "");

        return request;
    }

    private String obtenerDenominacionCliente(Cliente cliente) {
        if (cliente == null) {
            return "N/A";
        }

        // Verificar si es RUC (por nombre o abreviatura)
        boolean esRUC = cliente.getTipoDocumento() != null &&
                ("RUC".equalsIgnoreCase(cliente.getTipoDocumento().getNombre()) ||
                        "RUC".equalsIgnoreCase(cliente.getTipoDocumento().getAbreviatura()));

        if (esRUC) {
            String razonSocial = cliente.getRazonSocial();
            return (razonSocial != null && !razonSocial.trim().isEmpty())
                    ? razonSocial
                    : "Sin Razón Social";
        } else {
            String nombres = cliente.getNombres() != null ? cliente.getNombres() : "";
            String apellidos = cliente.getApellidos() != null ? cliente.getApellidos() : "";
            String nombreCompleto = (nombres + " " + apellidos).trim();
            return nombreCompleto.isEmpty() ? "Sin Nombre" : nombreCompleto;
        }
    }

    private List<Map<String, Object>> construirItems(List<DetalleVenta> detalles) {
        return detalles.stream().map(detalle -> {
            Producto producto = detalle.getProducto();

            Map<String, Object> item = new HashMap<>();

            // Datos básicos
            item.put("unidad_de_medida", "NIU");
            item.put("codigo", obtenerCodigoProducto(producto));
            item.put("descripcion", producto.getNombreProducto());
            item.put("cantidad", detalle.getCantidad());

            // Cálculos consistentes con NubeFact
            BigDecimal precioUnitarioConIgv = detalle.getPrecioUnitario();
            BigDecimal cantidad = detalle.getCantidad();
            BigDecimal descuento = detalle.getDescuento() != null ? detalle.getDescuento() : BigDecimal.ZERO;
            
            // 1. Calcular valor unitario sin IGV
            BigDecimal factorIgv = BigDecimal.ONE.add(
                obtenerPorcentajeIgv().divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
            );
            BigDecimal valorUnitarioSinIgv = precioUnitarioConIgv.divide(factorIgv, 4, RoundingMode.HALF_UP);
            
            // 2. Calcular subtotal sin descuento
            BigDecimal subtotalSinDescuentoSinIgv = valorUnitarioSinIgv.multiply(cantidad);
            
            // 3. Aplicar descuento (como monto directo, no porcentaje)
            BigDecimal montoDescuento = BigDecimal.ZERO;
            if (descuento.compareTo(BigDecimal.ZERO) > 0) {
                // Convertir porcentaje de descuento a monto
                montoDescuento = subtotalSinDescuentoSinIgv.multiply(descuento)
                    .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
            }
            
            // 4. Calcular subtotal con descuento
            BigDecimal subtotalSinIgv = subtotalSinDescuentoSinIgv.subtract(montoDescuento);
            
            // 5. Calcular IGV sobre el subtotal con descuento
            BigDecimal igv = subtotalSinIgv.multiply(obtenerPorcentajeIgv())
                .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
            
            // 6. Calcular total (subtotal sin IGV + IGV)
            BigDecimal total = subtotalSinIgv.add(igv);

            // Asignar valores redondeados
            item.put("valor_unitario", valorUnitarioSinIgv.setScale(2, RoundingMode.HALF_UP));
            item.put("precio_unitario", precioUnitarioConIgv.setScale(2, RoundingMode.HALF_UP));
            item.put("subtotal", subtotalSinIgv.setScale(2, RoundingMode.HALF_UP));
            item.put("tipo_de_igv", 1); // Gravado
            item.put("igv", igv.setScale(2, RoundingMode.HALF_UP));
            item.put("total", total.setScale(2, RoundingMode.HALF_UP));
            
            // Campos adicionales requeridos por NubeFact
            item.put("anticipo_regularizacion", "false");
            item.put("anticipo_documento_serie", "");
            item.put("anticipo_documento_numero", "");
            item.put("codigo_producto_sunat", "10000000"); // Código genérico para productos
            
            // Si hay descuento, agregarlo como monto
            if (descuento.compareTo(BigDecimal.ZERO) > 0) {
                item.put("descuento", montoDescuento.setScale(2, RoundingMode.HALF_UP));
            } else {
                item.put("descuento", "");
            }

            return item;
        }).collect(Collectors.toList());
    }

    private String obtenerCodigoProducto(Producto producto) {
        return Optional.ofNullable(producto.getCodigoBarra())
                .filter(cb -> !cb.isEmpty())
                .orElse(producto.getCodigoSKU());
    }

    private String enviarANubefact(Map<String, Object> request, Venta venta) {
        try {
            // Calcular totales generales
            calcularTotalesGenerales(request);

            String jsonRequest = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
            logger.info("JSON para NubeFact:\n{}", jsonRequest);
            guardarJsonEnArchivo(jsonRequest, "nubefact_request_"+System.currentTimeMillis()+".json");

            String respuesta = client.emitirComprobante(jsonRequest);
            logger.info("Respuesta de NubeFact: {}", respuesta);
            
            // Procesar la respuesta
            Map<String, Object> respuestaMap = objectMapper.readValue(respuesta, Map.class);
            
            // Verificar si hay errores
            if (respuestaMap.containsKey("errors")) {
                String mensajeError = "Error al emitir comprobante en NubeFact: " + respuestaMap.get("errors");
                logger.error(mensajeError);
                throw new RuntimeException(mensajeError);
            }
            
            // Guardar la información del comprobante
            ComprobanteNubeFact comprobante = new ComprobanteNubeFact();
            comprobante.setVenta(venta);
            comprobante.setEnlace((String) respuestaMap.get("enlace"));
            comprobante.setCadenaParaCodigoQr((String) respuestaMap.get("cadena_para_codigo_qr"));
            comprobante.setCodigoDeBarras((String) respuestaMap.get("codigo_de_barras"));
            comprobante.setEnlaceDelPdf((String) respuestaMap.get("enlace_del_pdf"));
            comprobante.setEnlaceDelXml((String) respuestaMap.get("enlace_del_xml"));
            comprobante.setAceptadaPorSunat((Boolean) respuestaMap.get("aceptada_por_sunat"));
            comprobante.setSunatDescription((String) respuestaMap.get("sunat_description"));
            comprobante.setSunatNote((String) respuestaMap.get("sunat_note"));
            comprobante.setSunatResponsecode((String) respuestaMap.get("sunat_responsecode"));
            comprobante.setCodigoHash((String) respuestaMap.get("codigo_hash"));
            comprobante.setDigestValue((String) respuestaMap.get("digest_value"));
            comprobante.setKey((String) respuestaMap.get("key"));

            comprobanteNubeFactRepository.save(comprobante);

            return respuesta;
        } catch (JsonProcessingException e) {
            logger.error("Error al generar JSON para NubeFact", e);
            throw new RuntimeException("Error al generar JSON para NubeFact", e);
        } catch (Exception e) {
            logger.error("Error inesperado al enviar a NubeFact", e);
            throw new RuntimeException("Error inesperado al enviar a NubeFact", e);
        }
    }

    private void calcularTotalesGenerales(Map<String, Object> request) {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> items = (List<Map<String, Object>>) request.get("items");

        BigDecimal totalGravada = items.stream()
                .map(item -> (BigDecimal) item.get("subtotal"))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalIgv = items.stream()
                .map(item -> (BigDecimal) item.get("igv"))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal total = items.stream()
                .map(item -> (BigDecimal) item.get("total"))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        request.put("total_gravada", totalGravada);
        request.put("total_igv", totalIgv);
        request.put("total", total);
    }

    private void guardarJsonEnArchivo(String json, String nombreArchivo) {
        try {
            Path path = Paths.get("logs", nombreArchivo);
            Files.createDirectories(path.getParent());
            Files.write(path, json.getBytes(StandardCharsets.UTF_8));
            logger.info("JSON guardado en: {}", path.toAbsolutePath());
        } catch (Exception e) {
            logger.warn("No se pudo guardar el JSON en archivo", e);
        }
    }

    private BigDecimal obtenerPorcentajeIgv() {
        return parametroRepositorio.findByClave("IGV")
                .map(parametro -> {
                    try {
                        return new BigDecimal(parametro.getValor());
                    } catch (NumberFormatException e) {
                        throw new RuntimeException("Formato inválido para IGV en parámetros");
                    }
                })
                .orElse(new BigDecimal("18.00")); // Valor por defecto
    }

    /**
     * Método de validación para verificar cálculos
     * Usado para debugging y validación de cálculos
     */
    public void validarCalculos(BigDecimal precioUnitario, BigDecimal cantidad, BigDecimal descuento) {
        logger.info("=== VALIDACIÓN DE CÁLCULOS ===");
        logger.info("Precio unitario: {}", precioUnitario);
        logger.info("Cantidad: {}", cantidad);
        logger.info("Descuento: {}%", descuento);
        
        // Cálculos del sistema interno
        BigDecimal subtotalSinDescuento = precioUnitario.multiply(cantidad);
        BigDecimal montoDescuento = subtotalSinDescuento.multiply(descuento)
            .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        BigDecimal subtotalConDescuento = subtotalSinDescuento.subtract(montoDescuento);
        
        BigDecimal factorIgv = BigDecimal.ONE.add(
            obtenerPorcentajeIgv().divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
        );
        BigDecimal subtotalSinIgv = subtotalConDescuento.divide(factorIgv, 4, RoundingMode.HALF_UP);
        BigDecimal igv = subtotalConDescuento.subtract(subtotalSinIgv);
        
        logger.info("Subtotal sin descuento: {}", subtotalSinDescuento);
        logger.info("Monto descuento: {}", montoDescuento);
        logger.info("Subtotal con descuento: {}", subtotalConDescuento);
        logger.info("Subtotal sin IGV: {}", subtotalSinIgv);
        logger.info("IGV: {}", igv);
        logger.info("Total: {}", subtotalConDescuento);
        logger.info("=== FIN VALIDACIÓN ===");
    }

    public String anularComprobante(String serie, String numero, String motivo, String codigoUnico, Integer tipoComprobante) {
        try {
            logger.info("Iniciando anulación de comprobante {}-{}", serie, numero);

            // Construir el JSON de anulación
            Map<String, Object> request = new HashMap<>();
            request.put("operacion", "generar_anulacion");
            request.put("tipo_de_comprobante", tipoComprobante);
            request.put("serie", serie);
            request.put("numero", numero);
            request.put("motivo", motivo);
            request.put("codigo_unico", codigoUnico);

            String jsonRequest = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
            logger.info("JSON para anulación en NubeFact:\n{}", jsonRequest);
            guardarJsonEnArchivo(jsonRequest, "nubefact_anulacion_"+serie+"-"+numero+"_"+System.currentTimeMillis()+".json");

            String respuesta = client.anularComprobante(jsonRequest);
            logger.info("Respuesta de anulación de NubeFact: {}", respuesta);

            return respuesta;
        } catch (Exception e) {
            logger.error("Error al anular comprobante {}-{}", serie, numero, e);
            throw new RuntimeException("Error al anular comprobante", e);
        }
    }
}