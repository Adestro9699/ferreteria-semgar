package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.dto.CotizacionDTO;
import com.semgarcorp.ferreteriaSemGar.dto.CotizacionResumenDTO;
import com.semgarcorp.ferreteriaSemGar.dto.VentaDTO;
import com.semgarcorp.ferreteriaSemGar.modelo.*;
import com.semgarcorp.ferreteriaSemGar.repositorio.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

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

    public CotizacionService(CotizacionRepository cotizacionRepositorio, ClienteRepository clienteRepository,
                             TrabajadorRepository trabajadorRepository, TipoPagoRepository tipoPagoRepository,
                             EmpresaRepository empresaRepository, ProductoRepository productoRepository) {
        this.cotizacionRepositorio = cotizacionRepositorio;
        this.clienteRepository = clienteRepository;
        this.trabajadorRepository = trabajadorRepository;
        this.tipoPagoRepository = tipoPagoRepository;
        this.empresaRepository = empresaRepository;
        this.productoRepository = productoRepository;
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

    public Cotizacion actualizar(Cotizacion cotizacion) {
        return cotizacionRepositorio.save(cotizacion);
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
        // Implementar la conversión de Cotizacion a CotizacionDTO
        // Aquí se debe implementar la lógica para convertir Cotizacion a CotizacionDTO
        return null; // Placeholder para la implementación
    }

    private String generarCodigoCotizacion() {
        Long numeroSecuencial = cotizacionRepositorio.count() + 1;
        return String.format("%04d", numeroSecuencial); // Ejemplo: "0001", "0002", ..., "0100"
    }

    public List<CotizacionResumenDTO> obtenerTodasCotizacionesResumen() {
        return cotizacionRepositorio.findAllCotizacionesResumen();
    }
}
