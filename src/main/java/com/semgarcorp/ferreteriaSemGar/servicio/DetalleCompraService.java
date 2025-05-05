package com.semgarcorp.ferreteriaSemGar.servicio;
                
import com.semgarcorp.ferreteriaSemGar.modelo.Compra;
import com.semgarcorp.ferreteriaSemGar.modelo.DetalleCompra;
import com.semgarcorp.ferreteriaSemGar.modelo.Producto;
import com.semgarcorp.ferreteriaSemGar.repositorio.DetalleCompraRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DetalleCompraService {

    private final DetalleCompraRepository detalleCompraRepositorio;
    private final ProductoService productoService;
    private final CompraService compraService;

    // Constructor
    public DetalleCompraService(DetalleCompraRepository detalleCompraRepositorio,
                                ProductoService productoService,
                                CompraService compraService) {
        this.detalleCompraRepositorio = detalleCompraRepositorio;
        this.productoService = productoService;
        this.compraService = compraService;
    }

    public List<DetalleCompra> listar() {
        return detalleCompraRepositorio.findAll();
    }

    public DetalleCompra obtenerPorId(Integer id) {
        return detalleCompraRepositorio.findById(id).orElse(null);
    }

    @Transactional
    public DetalleCompra guardar(DetalleCompra detalleCompra) {
        // Calcular subtotal
        BigDecimal subtotal = detalleCompra.getCantidad().multiply(detalleCompra.getPrecioUnitario());
        detalleCompra.setSubtotal(subtotal);

        // Actualizar precio de venta del producto
        Producto producto = detalleCompra.getProducto();
        if (producto != null) {
            productoService.actualizarPrecioVenta(
                    producto.getIdProducto(),
                    detalleCompra.getPrecioUnitario(),
                    detalleCompra.getPorcentajeUtilidadManual()
            );
        }

        // Incrementar el stock del producto con la cantidad comprada
        if (producto != null && detalleCompra.getCantidad() != null) {
            BigDecimal cantidadComprada = detalleCompra.getCantidad();
            productoService.incrementarStock(producto.getIdProducto(), cantidadComprada);
        }

        // Guardar el detalle de compra en la base de datos
        DetalleCompra nuevoDetalleCompra = detalleCompraRepositorio.save(detalleCompra);

        // Recalcular el total de la compra asociada
        if (nuevoDetalleCompra.getCompra() != null) {
            compraService.recalcularTotalCompra(nuevoDetalleCompra.getCompra().getIdCompra());
            
            // Cambiar el estado de la compra a COMPLETADA si está en PENDIENTE
            Compra compra = nuevoDetalleCompra.getCompra();
            if (compra.getEstadoCompra() == Compra.EstadoCompra.PENDIENTE) {
                compra.setEstadoCompra(Compra.EstadoCompra.COMPLETADA);
                compraService.actualizar(compra);
            }
        }

        return nuevoDetalleCompra;
    }

    public DetalleCompra actualizar(DetalleCompra detalleCompra) {
        // Calcular el subtotal nuevamente en caso de que se actualicen cantidad o precio unitario
        if (detalleCompra.getCantidad() != null && detalleCompra.getPrecioUnitario() != null) {
            BigDecimal subtotal = detalleCompra.getCantidad().multiply(detalleCompra.getPrecioUnitario());
            detalleCompra.setSubtotal(subtotal);
        }

        // Obtener el producto asociado al detalle de compra
        Producto producto = detalleCompra.getProducto();

        // Actualizar el precio de venta del producto basado en el precio unitario (precio de compra)
        if (producto != null && detalleCompra.getPrecioUnitario() != null) {
            productoService.actualizarPrecioVenta(
                    producto.getIdProducto(),
                    detalleCompra.getPrecioUnitario(),
                    detalleCompra.getPorcentajeUtilidadManual() // <- Nuevo tercer parámetro
            );
        }

        // Incrementar el stock del producto con la cantidad comprada
        if (producto != null && detalleCompra.getCantidad() != null) {
            BigDecimal cantidadComprada = detalleCompra.getCantidad();
            productoService.incrementarStock(producto.getIdProducto(), cantidadComprada);
        }

        // Actualizar el detalle de compra en la base de datos
        DetalleCompra detalleActualizado = detalleCompraRepositorio.save(detalleCompra);

        // Recalcular el total de la compra asociada
        if (detalleActualizado.getCompra() != null) {
            compraService.recalcularTotalCompra(detalleActualizado.getCompra().getIdCompra());
        }

        return detalleActualizado;
    }

    public void eliminar(Integer id) {
        // Buscar el detalle de compra por ID
        DetalleCompra detalleCompra = detalleCompraRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle de compra no encontrado"));

        // Reducir el stock del producto asociado
        Producto producto = detalleCompra.getProducto();
        if (producto != null && detalleCompra.getCantidad() != null) {
            productoService.reducirStock(producto.getIdProducto(), detalleCompra.getCantidad());
        }

        // Eliminar el detalle de compra
        detalleCompraRepositorio.deleteById(id);

        // Recalcular el total de la compra asociada
        if (detalleCompra.getCompra() != null) {
            compraService.recalcularTotalCompra(detalleCompra.getCompra().getIdCompra());
        }
    }
    
    public List<DetalleCompra> obtenerDetallesPorCompra(Integer idCompra) {
        return detalleCompraRepositorio.findByCompraId(idCompra);
    }
}