package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.*;
import com.semgarcorp.ferreteriaSemGar.repositorio.CotizacionRepository;
import com.semgarcorp.ferreteriaSemGar.repositorio.VentaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CotizacionService {

    private final CotizacionRepository cotizacionRepositorio;

    private final VentaRepository ventaRepositorio;

    public CotizacionService(CotizacionRepository cotizacionRepositorio, VentaRepository ventaRepositorio) {
        this.cotizacionRepositorio = cotizacionRepositorio;
        this.ventaRepositorio = ventaRepositorio;
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

    public Cotizacion convertirVentaACotizacion(Venta venta) {
        // Crear una nueva cotización
        Cotizacion cotizacion = new Cotizacion();
        cotizacion.setFechaCotizacion(LocalDateTime.now());
        cotizacion.setTotalCotizacion(venta.getTotalVenta());
        cotizacion.setEstadoCotizacion(EstadoCotizacion.PENDIENTE); // Estado inicial
        cotizacion.setCliente(venta.getCliente());
        cotizacion.setTrabajador(venta.getTrabajador());
        cotizacion.setTipoPago(venta.getTipoPago());
        cotizacion.setEmpresa(venta.getEmpresa());

        // Copiar los detalles de la venta a la cotización
        List<DetalleCotizacion> detallesCotizacion = new ArrayList<>();
        for (DetalleVenta detalleVenta : venta.getDetalles()) {
            DetalleCotizacion detalleCotizacion = new DetalleCotizacion();
            detalleCotizacion.setProducto(detalleVenta.getProducto());
            detalleCotizacion.setCantidad(detalleVenta.getCantidad());
            detalleCotizacion.setPrecioUnitario(detalleVenta.getPrecioUnitario());
            detalleCotizacion.setDescuento(detalleVenta.getDescuento());
            detalleCotizacion.setSubtotal(detalleVenta.getSubtotal());
            detalleCotizacion.setSubtotalSinIGV(detalleVenta.getSubtotalSinIGV());
            detalleCotizacion.setIgvAplicado(detalleVenta.getIgvAplicado());
            detalleCotizacion.setCotizacion(cotizacion); // Establecer la relación con la cotización
            detallesCotizacion.add(detalleCotizacion);
        }

        cotizacion.setDetalles(detallesCotizacion);

        // Guardar la cotización en la base de datos
        return cotizacionRepositorio.save(cotizacion);
    }

    public Venta convertirCotizacionAVenta(Cotizacion cotizacion) {
        // Crear una nueva venta
        Venta venta = new Venta();
        venta.setFechaVenta(LocalDateTime.now());
        venta.setTotalVenta(cotizacion.getTotalCotizacion());
        venta.setEstadoVenta(EstadoVenta.PENDIENTE); // Estado inicial
        venta.setCliente(cotizacion.getCliente());
        venta.setTrabajador(cotizacion.getTrabajador());
        venta.setTipoPago(cotizacion.getTipoPago());
        venta.setEmpresa(cotizacion.getEmpresa());

        // Copiar los detalles de la cotización a la venta
        List<DetalleVenta> detallesVenta = new ArrayList<>();
        for (DetalleCotizacion detalleCotizacion : cotizacion.getDetalles()) {
            DetalleVenta detalleVenta = new DetalleVenta();
            detalleVenta.setProducto(detalleCotizacion.getProducto());
            detalleVenta.setCantidad(detalleCotizacion.getCantidad());
            detalleVenta.setPrecioUnitario(detalleCotizacion.getPrecioUnitario());
            detalleVenta.setDescuento(detalleCotizacion.getDescuento());
            detalleVenta.setSubtotal(detalleCotizacion.getSubtotal());
            detalleVenta.setSubtotalSinIGV(detalleCotizacion.getSubtotalSinIGV());
            detalleVenta.setIgvAplicado(detalleCotizacion.getIgvAplicado());
            detalleVenta.setVenta(venta); // Establecer la relación con la venta
            detallesVenta.add(detalleVenta);
        }

        venta.setDetalles(detallesVenta);

        // Guardar la venta en la base de datos
        return ventaRepositorio.save(venta);
    }
}
